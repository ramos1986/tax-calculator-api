package com.tax.model;

import lombok.Data;

@Data
public class VAT {
	
	private Integer rate;
	private Double net;
	private Double tax;
	private Double gross;
}
