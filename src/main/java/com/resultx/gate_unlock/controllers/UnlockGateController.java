package com.resultx.gate_unlock.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gate")
public class UnlockGateController {

	@SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(UnlockGateController.class);
	private static final String SECRET_CODE = "1234";
	
	@PostMapping("unlock")
	public ResponseEntity<String> unlockate(@RequestParam String code){
		if(SECRET_CODE.equals(code)) {
			logger.info("Portão destravado com sucesso.");
			return new  ResponseEntity<>("Portão destravado com sucesso!", HttpStatus.OK);
		}else {
			logger.warn("Tentativa de destravamento com código incorreto.");
			return new ResponseEntity<>("Código incorreto.", HttpStatus.FORBIDDEN);
		}
	}
	
	
}
