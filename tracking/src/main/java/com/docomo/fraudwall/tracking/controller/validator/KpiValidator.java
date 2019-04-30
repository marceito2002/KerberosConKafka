package com.docomo.fraudwall.tracking.controller.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.docomo.fraudwall.tracking.generated.model.Kpis;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class KpiValidator implements Validator {


	
	private static final Logger logger = LoggerFactory.getLogger(KpiValidator.class);
	
	
	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

	@Override
	public void validate(Object target, Errors errors) {
		if (target instanceof Kpis) {			
			validateKpi (target);
		}
		
		
	}
	private void validateKpi(Object target) {
		logger.info("validateKpi");
		Kpis dataKpi = (Kpis) target;
//		if ( dataKpi.getTimestampUtc().equals("")) {
//			
//		}

	}


}
