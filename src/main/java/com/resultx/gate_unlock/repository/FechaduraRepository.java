package com.resultx.gate_unlock.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository; 

import com.resultx.gate_unlock.domain.Fechadura;

public interface FechaduraRepository extends JpaRepository<Fechadura, Long>{
	Optional<Fechadura> findByUsername(String username);
}