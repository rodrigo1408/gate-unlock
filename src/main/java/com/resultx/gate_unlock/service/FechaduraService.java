package com.resultx.gate_unlock.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.resultx.gate_unlock.domain.Fechadura;
import com.resultx.gate_unlock.repository.FechaduraRepository;

@Service
public class FechaduraService {
	@Autowired
	private FechaduraRepository fec;

	public Optional<Fechadura> getUserByUsername(String username) {
		return fec.findByUsername(username);
	}
}
