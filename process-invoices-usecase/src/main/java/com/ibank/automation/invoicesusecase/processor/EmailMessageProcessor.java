package com.ibank.automation.invoicesusecase.processor;

import com.workfusion.intake.api.domain.Document;
import com.workfusion.intake.api.domain.Field;
import com.workfusion.intake.api.domain.Transaction;
import com.workfusion.intake.processor.TransactionProcessor;
import com.ibank.automation.invoicesusecase.service.ExcelService;
import com.ibank.automation.system.invoiceplane.to.ProductTO;
import com.workfusion.rpa.core.storage.S3Manager;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EmailMessageProcessor implements TransactionProcessor {

	private S3Manager s3Manager;
	
	private final String EXPECTED_FILE_EXTENSION = "xlsx";

    @Inject
    public EmailMessageProcessor(final S3Manager s3Manager) {
    	this.s3Manager = s3Manager;
    }
    
    @Override
    public Transaction transform(final Transaction transaction) {
        if (!transaction.getDocs().isEmpty()) {
        	for (Document doc : transaction.getDocs()) {
        		if (doc!=null && doc.getOriginalDocumentLink()!=null && doc.getOriginalDocumentLink().contains(EXPECTED_FILE_EXTENSION)) {
        			
        			byte[] content = s3Manager.getContent(doc.getOriginalDocumentLink());
        		
        			try {
        				List<ProductTO> products = ExcelService.parseProducts(content);
            			List<Document> newProductDocs = new ArrayList<Document>();
                        for(ProductTO product : products) {                    	
                        	Document newDoc = mapProductToDocument(product);
                            newProductDocs.add(newDoc);
                        }
                        
                        transaction.setDocs(newProductDocs);
                        return transaction;
                        
        			} catch (Exception e) {
						e.printStackTrace();
					}
        				      			
        		}
        	}        	         
        }
        return transaction;
    }
    
    private Document mapProductToDocument(ProductTO productTO){
        Document document = new Document();
        document.setId(uuid());
        document.setName(productTO.getProductName());
        document.getExtractedFields().put("family", Field.of(productTO.getFamily()) );
        document.getExtractedFields().put("price", Field.of(productTO.getPrice()) );
        document.getExtractedFields().put("description", Field.of(productTO.getProductDescription()) );
        document.getExtractedFields().put("product_name", Field.of(productTO.getProductName()) );
        document.getExtractedFields().put("sku", Field.of(productTO.getSku()) );
        document.getExtractedFields().put("tax_rate", Field.of(productTO.getTaxRate()) );
        document.setType(ProductTO.class.getName());
        return document;
    }
    
    private String uuid() {
        return UUID.randomUUID().toString();
    }

}