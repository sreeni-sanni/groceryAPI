package com.cgi.grocery.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cgi.grocery.exception.ResourceNotFoundException;
import com.cgi.grocery.model.Grocery;
import com.cgi.grocery.service.GroceryService;

@RestController
public class GroceryController {

	private final GroceryService groceryService;
	
	public GroceryController(GroceryService groceryService) {
		super();
		this.groceryService = groceryService;
	}

	@GetMapping("/getGroceriesByName")
	public ResponseEntity<List<Grocery>> getGroceriesByName(@RequestParam("itemName") String name) throws ResourceNotFoundException {
		return ResponseEntity.ok(groceryService.getGroceriesByName(name));
	}

	@GetMapping("/downloadGroceriesCSVReport")
	public ResponseEntity<Resource> downloadGroceriesCSVReport() throws Exception {

		File file = groceryService.generateCSVReport();
		Path path = Paths.get(file.getAbsolutePath());
		ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
		file.deleteOnExit();
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName())
				.contentLength(file.length()).contentType(MediaType.parseMediaType("application/octet-stream"))
				.body(resource);
	}

	@GetMapping("/getAllGroceryItems")
	public ResponseEntity<List<Grocery>> getAllGroceryItems() throws Exception{
		return ResponseEntity.ok(groceryService.getGroceryItems());
	}
}
