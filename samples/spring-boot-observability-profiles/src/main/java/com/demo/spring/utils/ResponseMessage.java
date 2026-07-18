package com.demo.spring.utils;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ResponseMessage {

	private String status;

	public ResponseMessage() {
		// TODO Auto-generated constructor stub
	}

	public ResponseMessage(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
