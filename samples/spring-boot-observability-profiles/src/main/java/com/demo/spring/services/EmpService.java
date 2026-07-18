package com.demo.spring.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.spring.EmpExistsException;
import com.demo.spring.EmpNotFoundException;
import com.demo.spring.entity.Emp;
import com.demo.spring.entity.EmpList;
import com.demo.spring.repositories.EmpRepository;

@Service
public class EmpService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	EmpRepository empRepository;

	public EmpList getAllEmp() {
		EmpList empList = new EmpList();
		empList.setEmpList(empRepository.findAll());
		return empList;
	}

	public Emp findEmpById(Integer id) {
		return empRepository.findById(id).orElseThrow(() -> new EmpNotFoundException("Emp Not Found"));
	}

	public Emp saveToDb(Emp e) {
		if (empRepository.existsById(e.getEmpId())) {
			logger.info("Employee with id {} already exists in database", e.getEmpId());
			throw new EmpExistsException("Employee Exists");
		} else {
			logger.info("Employee with id {} saved in database", e.getEmpId());
			Emp savedEmp = empRepository.save(e);
			return savedEmp;
		}
	}

	public Emp updateEmp(Emp e) {
		if (empRepository.existsById(e.getEmpId())) {

			Emp updatedEmp = empRepository.save(e);
			logger.info("Employee with id {} updated in database", e.getEmpId());

			return updatedEmp;
		} else {
			logger.info("Employee with id {} Not Found in database", e.getEmpId());
			throw new EmpNotFoundException("Employee Not Found ");

		}
	}
	
	public void updateSalary(Integer id, double amount) {
		if (empRepository.existsById(id)) {

			empRepository.updateSalary(id,amount);
			logger.info("Employee with id {} updated in database", id);

		} else {
			logger.info("Employee with id {} Not Found in database", id);
			throw new EmpNotFoundException("Employee Not Found ");

		}
	}
}
