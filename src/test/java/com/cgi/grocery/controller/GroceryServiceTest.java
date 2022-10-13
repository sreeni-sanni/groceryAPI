package com.cgi.grocery.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import com.cgi.grocery.model.Grocery;
import com.cgi.grocery.service.GroceryService;
import com.cgi.grocery.service.XlsxFileReadService;


@ExtendWith(MockitoExtension.class)
public class GroceryServiceTest {
	
	@Autowired
	GroceryService service;
	
	@Mock
	XlsxFileReadService xlsxFileReadService;
	
	@BeforeEach
    public void setService() {
        MockitoAnnotations.openMocks(this);
        service = new GroceryService(xlsxFileReadService);
    }
	@Test
	@DisplayName("unit testing for get grocery items ")
	public void getGroceryItemsTest() {
		List<Grocery> list = new ArrayList<Grocery>();
		Grocery grocery = new Grocery();
		grocery.setItemName("Lime Local");
		grocery.setPrice(97);
		grocery.setDate("18-07-2012");
		list.add(grocery);
		Grocery grocery_v1 = new Grocery();
		grocery_v1.setItemName("Little gourd");
		grocery_v1.setPrice(15);
		grocery_v1.setDate("18-07-2012");
		list.add(grocery_v1);
		
		when(xlsxFileReadService.getData()).thenReturn(list);
		List<Grocery> data=service.getGroceryItems();
		assertEquals(data.size(),2);
	}
	@Test
	@DisplayName("unit testing for get groceries by name")
	public void getGroceriesByNameTest() {
		List<Grocery> list = new ArrayList<Grocery>();
		Grocery grocery = new Grocery();
		grocery.setItemName("Lime Local");
		grocery.setPrice(97);
		grocery.setDate("18-07-2012");
		list.add(grocery);
		
		when(xlsxFileReadService.getData()).thenReturn(list); 
		List<Grocery> data=service.getGroceriesByName("Lime Local");
		assertEquals(data.get(0).getDate(),"18-07-2012");
	}
	@Test
	public void generateCSVReportTest() throws Exception {
		List<Grocery> list = new ArrayList<Grocery>();
		Grocery grocery = new Grocery();
		grocery.setItemName("Lime Local");
		grocery.setPrice(97);
		grocery.setDate("18-07-2012");
		list.add(grocery);
		when(xlsxFileReadService.getData()).thenReturn(list);
		File report=service.generateCSVReport();
		assertEquals(report.exists(),true);
		
	}

}
