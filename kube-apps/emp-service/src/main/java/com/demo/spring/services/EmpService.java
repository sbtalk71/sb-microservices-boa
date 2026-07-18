package com.demo.spring.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.demo.spring.EmpRepository;
import com.demo.spring.entities.Emp;
import com.demo.spring.exceptions.EmpNotFoundException;

import io.micrometer.observation.annotation.Observed;

@Service
public class EmpService {

	private EmpRepository empRepository;

	public EmpService(EmpRepository empRepository) {
		this.empRepository = empRepository;
		System.out.println("Repository class : "+empRepository.getClass().getName());
	}
	
	Logger logger=LoggerFactory.getLogger(this.getClass().getName());
	
	@Observed(name = "emp.find.one")
	public Emp findOneEmp(Integer id) {
		Optional<Emp> empOp= empRepository.findById(id);
		
		if(empOp.isPresent()) {
			logger.info("checking if emp is present with id {}",id);
			return empOp.get();
		}else {
			logger.info("emp not present with id {}",id);
			throw new EmpNotFoundException("Emp Not Found..");
		}
		
		//return empRepository.findById(id).orElseThrow(RuntimeException::new);
	}
	
	@Observed(name="emp.find.all")
	public List<Emp> getAll(){
		logger.info("getting all the employees data");
		return empRepository.findAll();
	}
	
	
	public Emp saveEmp(Emp e) {
		return empRepository.save(e);
	}
	
	public Emp updateEmp(Integer id,Emp e) {
		
		if(empRepository.existsById(id)) {
			e.setEmpId(id);
		return empRepository.save(e);
		}else {
			throw new EmpNotFoundException("EMP Not found");
		}
	}
	
	
	
}
