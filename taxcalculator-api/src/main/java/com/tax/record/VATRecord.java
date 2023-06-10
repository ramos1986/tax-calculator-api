package com.tax.record;

public record VATRecord (Integer rate,
	Float net,
	Float tax,
	Float gross) {
}
