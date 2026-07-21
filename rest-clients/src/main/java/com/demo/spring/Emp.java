package com.demo.spring;

public class Emp {

	private Integer empId;
	private String name;
	private String city;
	private Double salary;
	
	public Emp() {
		
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
