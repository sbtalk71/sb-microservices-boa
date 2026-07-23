package com.demo.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.spring.entities.Emp;

public interface EmpRepository extends JpaRepository<Emp, Integer> {

	
}
