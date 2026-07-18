package com.demo.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.demo.spring.entity.Emp;

public interface EmpRepository extends JpaRepository<Emp, Integer>{

	@Transactional
	@Modifying
	@Query("update Emp e set e.salary=e.salary+?2 where e.empId=?1")
	public void updateSalary(Integer id,double amount);
	
}
