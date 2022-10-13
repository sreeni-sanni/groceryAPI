package com.cgi.grocery.model;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class Grocery {

	private String itemName;

	private float price;

	private String date;

}
