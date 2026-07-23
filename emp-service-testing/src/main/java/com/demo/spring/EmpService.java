package com.demo.spring;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.demo.spring.entities.Emp;
import com.demo.spring.exceptions.EmpNotFoundException;
import com.demo.spring.repositories.EmpRepository;

import io.micrometer.observation.annotation.Observed;

@Service
public class EmpService {
	private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	private EmpRepository empRepository;

	public EmpService(EmpRepository empRepository) {
		this.empRepository = empRepository;
	}

	@Cacheable(value = "employees")
	@Observed(name = "list.emp.all")
	public List<Emp> listAllEmps() {
		logger.info("connecting to database to get the list of employees..");
		return empRepository.findAll();
	}

	@Cacheable(value = "employees",key = "#id")
	@Observed(name = "find.one.emp")
	public Emp findEmpById(Integer id) {
		Optional<Emp> empOp = empRepository.findById(id);
		logger.debug("connecting to database to get employee with id {}", id);
		if (empOp.isPresent()) {
			logger.info("Fetched employee with id {}", id);
			return empOp.get();
		} else {
			logger.warn(" employee with id {} not found", id);
			throw new EmpNotFoundException("Emp Not found");
		}
	}
	
	@CachePut(value = "employees", key = "#emp.empId")
	public Emp save(Emp emp) {
		logger.info("Employee saved to database");
		Emp e= empRepository.save(emp);
		return e;
	}
	
	public boolean delete(Integer id) {
		if(empRepository.existsById(id)) {
			empRepository.deleteById(id);
			return true;
		}else {
			logger.warn("employee with id {} not found", id);
			throw new EmpNotFoundException("Emp Not found");
		}
	}

}
