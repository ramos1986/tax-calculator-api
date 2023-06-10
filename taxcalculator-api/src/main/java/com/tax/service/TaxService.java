package com.tax.service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.tax.model.VAT;

@Component
public class TaxService {
	
	ArrayList<Integer> availableRates = new ArrayList<>(Arrays.asList(10, 13, 20));
	private static final String INVALID_RATE = "Invalid rate (available options: 10, 13, 20).";
	private static final String INVALID_INPUT = "Invalid input, you can only fill one field besides the rate: Net, Gross or Tax.";
	private static final Integer ROUND_VALUE = 2;
	
	public ResponseEntity<Object> calculateTax(VAT vat) {
		Integer countFields = 0;
		for (Field field : vat.getClass().getDeclaredFields()) {
			try {
				field.setAccessible(true);
				var fieldValue = field.get(vat);
				var fieldName = field.getName();
				
				if (fieldName.equals("rate")) {
					Integer value = Integer.parseInt(fieldValue.toString());
					if (!availableRates.contains(value)) {
						return new ResponseEntity<>(INVALID_RATE, 
								HttpStatus.BAD_REQUEST);
					}
				} else if (fieldValue != null) {
					countFields++;
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}
		
		if (countFields != 1) {
			return new ResponseEntity<>(INVALID_INPUT, HttpStatus.BAD_REQUEST);
		}		
		return new ResponseEntity<>(calculateVAT(vat), HttpStatus.OK);
	}
	
	private VAT calculateVAT(VAT vat) {
		if (vat.getTax() == null && vat.getGross() == null) {
			vat.setTax(vat.getNet() * vat.getRate()/100);
			vat.setGross(vat.getTax() + vat.getNet());
		} else if (vat.getNet() == null && vat.getGross() == null) {
			vat.setNet((vat.getTax() / vat.getRate())*100);
			vat.setGross(vat.getTax() + vat.getNet());
		} else if (vat.getNet() == null && vat.getTax() == null) {
			Double taxFromGross = Double.valueOf(vat.getRate())/100 
					/ (1 + Double.valueOf(vat.getRate())/100);
			vat.setTax(vat.getGross() * taxFromGross);
			vat.setNet(vat.getGross() - vat.getTax());
		}
		
		vat.setTax(new BigDecimal(vat.getTax()).setScale(ROUND_VALUE, RoundingMode.HALF_UP).doubleValue());
		vat.setNet(new BigDecimal(vat.getNet()).setScale(ROUND_VALUE, RoundingMode.HALF_UP).doubleValue());
		vat.setGross(new BigDecimal(vat.getGross()).setScale(ROUND_VALUE, RoundingMode.HALF_UP).doubleValue());  
		
		return vat;
	}
	
}
