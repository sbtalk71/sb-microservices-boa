package com.demo.spring;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.spring.entities.Emp;

public interface EmpRepository extends JpaRepository<Emp, Integer> {

	
}
