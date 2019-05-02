package com.ibank.automation.invoicesusecase.processor;

import com.workfusion.intake.api.domain.Document;
import com.workfusion.intake.api.domain.Field;
import com.workfusion.intake.processor.DocumentProcessor;
import com.workfusion.rpa.core.security.SecurityUtils;

import groovy.lang.Binding;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;

import java.util.Map;

public class CreateProductProcessorSetError extends DocumentProcessor {
	
	private static final String ERROR_PARAM_NAME = "error";
	
	private static final String STATUS_PARAM_NAME = "status";
	
    @Inject
    public CreateProductProcessorSetError(SecurityUtils securityUtils, Logger logger, Binding binding, @Named("botConfigParams") Map<String, String> params) {
    	super(binding, params);
    }

    @Override
    protected Document processDocument(Document document) {        
    	document.getExtractedFields().put(ERROR_PARAM_NAME, Field.of(params.get(ERROR_PARAM_NAME)));
    	document.getExtractedFields().put(STATUS_PARAM_NAME, Field.of(params.get(STATUS_PARAM_NAME)));	
        return document;
    }

}