package com.demo.spring;




public class EmpDTO  {
	@Override
	public String toString() {
		return "EmpDTO [id=" + id + ", name=" + name + ", city=" + city + ", salary=" + salary + "]";
	}
	private Integer id;
	private String name;
	private String city;
	private double salary;
	public EmpDTO() {
		// TODO Auto-generated constructor stub
	}
	public EmpDTO(Integer id,String name, String city,double salary){
		this.id=id;
		this.name=name;
		this.city=city;
		this.salary=salary;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public double getSalary() {
		return salary;
	}
	public void setSalary(double salary) {
		this.salary = salary;
	}
	
	
}
