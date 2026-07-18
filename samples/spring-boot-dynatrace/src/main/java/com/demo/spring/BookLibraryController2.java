package com.demo.spring;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.observation.annotation.Observed;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class BookLibraryController2 {

	MeterRegistry registry;
	Counter counter;
	Counter listCounter;
	Timer timer;
	public BookLibraryController2(MeterRegistry registry){
		this.registry=registry;
		this.counter=registry.counter("book.find.one");
		this.listCounter=registry.counter("book.find.list");
	}
	@GetMapping("findBook2")

	public String findBookById() {
		counter.increment();
		return "Book Found";
	}
	
	@GetMapping("listBooks2")
	public List<String> listAllBooks(){

		listCounter.increment();
		
		return Arrays.asList("book1","book2");
	}
}
