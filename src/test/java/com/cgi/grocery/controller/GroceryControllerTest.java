package com.cgi.grocery.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.ResourceUtils;

import com.cgi.grocery.model.Grocery;
import com.cgi.grocery.service.GroceryService;
import com.cgi.grocery.service.XlsxFileReadService;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(GroceryController.class)
public class GroceryControllerTest {

	@Autowired
	MockMvc mockMvc;
	@MockBean
	GroceryService groceryservice;
	@MockBean
	XlsxFileReadService xlsxFileReadService;

	@Test
	@DisplayName("Unit test for get groceries by name")
	public void getGroceriesByNameTest() throws Exception {
		List<Grocery> list = new ArrayList<Grocery>();
		Grocery grocery = new Grocery();
		grocery.setItemName("Lime Local");
		grocery.setPrice(97);
		grocery.setDate("18-07-2012");
		list.add(grocery);
		when(groceryservice.getGroceriesByName(anyString())).thenReturn(list);
		mockMvc.perform(get("/getGroceriesByName").param("itemName", grocery.getItemName())).andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(1))).andExpect(jsonPath("$[0].price", Matchers.is(97.0)));
	}

	@Test
	@DisplayName("Unit test for get all groceries items")
	public void getAllGroceryItemsTest() throws Exception {
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

		when(groceryservice.getGroceryItems()).thenReturn(list);
		mockMvc.perform(get("/getAllGroceries")).andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(2)));
	}
	
	@Test
	@DisplayName("Unit test for get groceries based on limit")
	public void getGroceriesByLimtTest() throws Exception {
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

		when(groceryservice.getGroceryItemsByLimit(anyInt(),anyInt())).thenReturn(list);
		mockMvc.perform(get("/getGroceriesByLimit").param("offSet", "0").param("limit","1")).andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(2)));
	}

	@Test
	@DisplayName("Unit test for download CSV report")
	public void downloadGroceiesCSVReportTest() throws Exception {
		File file = ResourceUtils.getFile("classpath:testGroceries.xlsx");
		when(groceryservice.generateCSVReport()).thenReturn(file);
		MvcResult result = mockMvc.perform(get("/downloadGroceriesCSVReport")).andExpect(status().isOk()).andReturn();
		assertEquals(8848, result.getResponse().getContentAsByteArray().length);
	}

}
