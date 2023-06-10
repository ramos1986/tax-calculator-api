package com.tax.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tax.model.VAT;
import com.tax.service.TaxService;

@RestController
@RequestMapping("/v1/tax")
public class TaxController {
	
	@Autowired
	TaxService service;
	
	@GetMapping("/austria")
	public String list() {
		return "Hello";
	}
	
	@PostMapping("/austria")
	public ResponseEntity<Object> calculate(@RequestBody VAT vat) {
		//VAT vat = new VAT();
		//return new ResponseEntity<String>("Hello World", responseHeaders, HttpStatus.CREATED);
		return service.calculateTax(vat);
		
		//return new ResponseEntity<VATRecord>(vat, HttpStatus.OK);
	}

}
