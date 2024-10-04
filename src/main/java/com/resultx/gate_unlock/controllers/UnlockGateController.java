package com.resultx.gate_unlock.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.resultx.gate_unlock.domain.Fechadura;
import com.resultx.gate_unlock.repository.FechaduraRepository;

@RestController
@RequestMapping("/api/gate")
public class UnlockGateController {

	private static final Logger logger = LoggerFactory.getLogger(UnlockGateController.class); 

	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Autowired
	private FechaduraRepository fechaduraRepository; // Repositório que acessa o banco

	@PostMapping("unlock")
	public ResponseEntity<String> unlockate( @RequestParam(required = true) String code,  @RequestParam(required = true) String username) {
		try {
			// Busca a fechadura no banco de dados
			Fechadura fechadura = fechaduraRepository.findByUsername(username)
					.orElseThrow(() -> new IllegalArgumentException("Fechadura não encontrada."));

			String encryptedCode = fechadura.getCode();

			if (encryptedCode == null || encryptedCode.isEmpty()) {
				logger.warn("Nenhuma chave foi passada.");
				return new ResponseEntity<>("Chave vazia.", HttpStatus.BAD_REQUEST);
			}

			// Verifica se o código fornecido corresponde ao código encriptado
			if (passwordEncoder.matches(code, encryptedCode)) {
				logger.info("Portão destravado com sucesso.");
				return new ResponseEntity<>("Portão destravado com sucesso!", HttpStatus.OK);
			} else {
				logger.warn("Tentativa de destravamento com código incorreto.");
				return new ResponseEntity<>("Código incorreto.", HttpStatus.FORBIDDEN);
			}

		} catch (IllegalArgumentException e) {
			logger.warn(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			logger.error("Erro ao tentar destravar o portão: " + e.getMessage());
			return new ResponseEntity<>("Erro ao tentar destravar o portão.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	} 
	
	@PostMapping("register")
	public ResponseEntity<String> registerUser(@RequestBody Fechadura fec) {
		try {
			// Verifica se o nome ou código estão vazios
			if (fec.getUsername() == null || fec.getUsername().isEmpty() || fec.getCode() == null
					|| fec.getCode().isEmpty()) {
				return new ResponseEntity<>("Nome e código são obrigatórios.", HttpStatus.BAD_REQUEST);
			}

			// Encripta o código antes de
			String encryptedCode = passwordEncoder.encode(fec.getCode());
			fec.setCode(encryptedCode); 

			// Salva o usuário no banco de dados
			fechaduraRepository.save(fec);
			return new ResponseEntity<>("Usuário registrado com sucesso.", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Erro ao registrar o usuário: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
