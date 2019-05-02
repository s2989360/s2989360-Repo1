package com.ibank.automation.invoicesusecase.processor;

import com.ibank.automation.invoicesusecase.rpa.InvoicePlaneBusinessRobot;
import com.ibank.automation.system.invoiceplane.to.ProductTO;
import com.workfusion.intake.api.domain.Document;
import com.workfusion.intake.api.domain.SingleField;
import com.workfusion.intake.processor.DocumentProcessor;
import com.workfusion.rpa.core.security.SecurityUtils;

import groovy.lang.Binding;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;

import java.util.Map;

public class CreateProductProcessor extends DocumentProcessor {
	
	@Inject
    private final SecurityUtils securityUtils;
    
	@Inject
	private final Logger logger;
	
    @Inject
    public CreateProductProcessor(SecurityUtils securityUtils, Logger logger, Binding binding, @Named("botConfigParams") Map<String, String> params) {
    	super(binding, params);
    	this.securityUtils = securityUtils;
        this.logger = logger;
    }

    @Override
    protected Document processDocument(Document document) {        
		ProductTO product = mapDocumentToProduct(document);	
		new InvoicePlaneBusinessRobot(securityUtils, logger, params).addProduct(product);		
        return document;
    }
    
    private ProductTO mapDocumentToProduct(Document document) {
    	ProductTO product = new ProductTO();

    	SingleField familySingleField = (SingleField)document.getExtractedFields().get("family");
    	String family = (familySingleField == null) ? "" : familySingleField.getSingleValue().toString();    
    	product.setFamily(family);

    	SingleField priceSingleField = (SingleField)document.getExtractedFields().get("price");
    	String price = (priceSingleField == null) ? "" : priceSingleField.getSingleValue().toString();
    	product.setPrice(price);
    	
    	SingleField descriptionSingleField = (SingleField)document.getExtractedFields().get("description");
    	String description = (descriptionSingleField == null) ? "" : descriptionSingleField.getSingleValue().toString();
    	product.setProductDescription(description);
    	
    	SingleField productNameSingleField = (SingleField)document.getExtractedFields().get("product_name");
    	String productName = (productNameSingleField == null) ? "" : productNameSingleField.getSingleValue().toString();
    	product.setProductName(productName);
    	
    	SingleField skuSingleField = (SingleField)document.getExtractedFields().get("sku");
    	String sku = (skuSingleField == null) ? "" : skuSingleField.getSingleValue().toString();
    	product.setSku(sku);
    	
    	SingleField taxRateSingleField = (SingleField)document.getExtractedFields().get("tax_rate");
    	String taxRate = (taxRateSingleField == null) ? "" : taxRateSingleField.getSingleValue().toString();
    	product.setTaxRate(taxRate);   	
    	
    	return product;
    }

}