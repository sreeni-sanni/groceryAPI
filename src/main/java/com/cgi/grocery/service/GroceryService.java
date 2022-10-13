package com.cgi.grocery.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cgi.grocery.exception.ResourceNotFoundException;
import com.cgi.grocery.model.Grocery;
import com.opencsv.CSVWriter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GroceryService {

	private static final Logger logger = LoggerFactory.getLogger(GroceryService.class);

	XlsxFileReadService xlsxFileReadService;

	public GroceryService(XlsxFileReadService xlsxFileReadService) {
		super();
		this.xlsxFileReadService = xlsxFileReadService;
	}

	public List<Grocery> getGroceryItems() {
		return getSortedGroceryData(xlsxFileReadService.getData());
	}

	public List<Grocery> getGroceriesByName(String name) {
		List<Grocery> list = xlsxFileReadService.getData().stream()
				.filter(item -> item.getItemName().matches("(.*)" + name + "(.*)")).collect(Collectors.toList());
		if (list.isEmpty()) {
			logger.info("No Item found with " + name);
			throw new ResourceNotFoundException("No Item found with " + name);
		}
		return list;
	}

	public File generateCSVReport() throws Exception {
		File file = new File("Groceries.csv");
		List<Grocery> groceryList = getSortedGroceryData(xlsxFileReadService.getData());
		try (CSVWriter writer = new CSVWriter(new FileWriter(file))) {
			writer.writeAll(getReportData(groceryList));
		} catch (IOException e) {
			logger.error("Error occured while processing CSV file ", e);
			throw new Exception("Error occured while processing CSV file " + e.getMessage());
		}
		return file;
	}

	private List<Grocery> getSortedGroceryData(List<Grocery> data) {
		return data.stream().filter(item -> !item.getItemName().equals(""))
				.sorted(Comparator.comparing(Grocery::getItemName)
						.thenComparing(Grocery::getPrice, Comparator.reverseOrder())
						.thenComparing(Grocery::getDate, Comparator.reverseOrder()))
				.collect(Collectors.toList());
	}

	private List<String[]> getReportData(List<Grocery> data) {
		List<String[]> list = new ArrayList<>();
		String[] headers = { "itemName", "price", "date" };
		list.add(headers);
		data.forEach(item -> {
			list.add(new String[] { item.getItemName(), String.valueOf(item.getPrice()), item.getDate() });
		});
		return list;
	}
}
