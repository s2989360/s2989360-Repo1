package com.ibank.automation.invoicesusecase.supplier;

import com.workfusion.intake.api.connector.TransactionSupplier;
import com.workfusion.intake.api.domain.Document;
import com.workfusion.intake.api.domain.Transaction;
import com.workfusion.rpa.core.storage.S3Manager;
import org.springframework.http.MediaType;
import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

public class TransactionSupplierGenerate5Products implements TransactionSupplier {

    private S3Manager s3Manager;

    @Inject
    public TransactionSupplierGenerate5Products(S3Manager s3Manager) {
        this.s3Manager = s3Manager;
    }

    @Override
    public Collection<Transaction> get() {
        Collection<Transaction> transactions = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Transaction transaction = new Transaction();
            transaction.setId(uuid());
            transaction.setDocs(Arrays.asList(createDocument()));
            transactions.add(transaction);
        }
        return transactions;
    }

    private Document createDocument() {
        Document document = new Document();
        document.setId(uuid());
        document.setName("Example Document");
        document.setTextLink(generateAndUploadDocument());
        return document;
    }

    private String generateAndUploadDocument() {
        return s3Manager.putFile(
                uuid() + ".txt",
                new ByteArrayInputStream("Some very important text".getBytes()),
                MediaType.TEXT_PLAIN_VALUE);
    }

    private String uuid() {
        return UUID.randomUUID().toString();
    }
    
}