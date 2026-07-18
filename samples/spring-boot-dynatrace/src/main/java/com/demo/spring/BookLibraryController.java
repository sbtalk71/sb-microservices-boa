package com.demo.spring;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.micrometer.core.annotation.Counted;
import io.micrometer.observation.annotation.Observed;

@RestController
public class BookLibraryController {

	@GetMapping("findBook")
	@Observed(name="book.find.one")
	public String findBookById() {
		return "Bookd Found";
	}
	
	@GetMapping("listBooks")
	@Observed(name="book.find.list")
	public List<String> listAllBooks(){
		return Arrays.asList("book1","book2");
	}
}
