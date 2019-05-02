package com.ibank.automation.invoicesusecase.processor;

import java.io.IOException;
import java.util.UUID;
import javax.inject.Inject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.http.MediaType;
import com.workfusion.intake.api.domain.Document;
import com.workfusion.intake.api.domain.Transaction;
import com.workfusion.intake.processor.TransactionProcessor;
import com.workfusion.rpa.core.storage.S3Manager;

public class TransactionImporter implements TransactionProcessor {
	
    private S3Manager s3Manager;

    @Inject
    public TransactionImporter(S3Manager s3Manager) {
        this.s3Manager = s3Manager;
    }

    @Override
    public Transaction transform(Transaction parsedTransaction) {
        try {
            for (Document document : parsedTransaction.getDocs()) {
                if (isContent(document.getTextLink())) {
                    String generatedLink = uploadFile(document.getTextLink());
                    document.setTextLink(generatedLink);
                }
                if (isContent(document.getTaggedTextLink())) {
                    String generatedLink = uploadFile(document.getTaggedTextLink());
                    document.setTaggedTextLink(generatedLink);
                }
            }
            return parsedTransaction;
        } catch (IOException e) {
            throw new RuntimeException("Cannot import transaction", e);
        }
    }

    private String uploadFile(String content) throws IOException {
        return s3Manager.putFile(UUID.randomUUID().toString() + ".html", IOUtils.toInputStream(content, "UTF-8"), MediaType.TEXT_HTML_VALUE);
    }

    private boolean isContent(String link) {
        return StringUtils.isNotEmpty(link) && !UrlValidator.getInstance().isValid(link);
    }
    
}