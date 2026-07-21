package com.demo.spring.entities;

import java.io.Serializable;

import jakarta.persistence.*;

@Entity
@Table(name="MYEMP")
public class Emp implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="EMPNO")
	private Integer empId;
	
	@Column(name="NAME")
	private String name;
	
	@Column(name="ADDRESS")
	private String city;
	
	@Column(name="SALARY")
	private Double salary;

	public Emp() {
		// TODO Auto-generated constructor stub
	}

	public Emp(String name, String city, Double salary) {
		this.name = name;
		this.city = city;
		this.salary = salary;
	}

	public Integer getEmpId() {
		return empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Double getSalary() {
		return salary;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
	}

}
