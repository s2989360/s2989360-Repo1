package com.ibank.automation.invoicesusecase.supplier;

import com.ibank.automation.invoicesusecase.rpa.InvoicePlaneBusinessRobot;
import com.workfusion.intake.api.connector.TransactionSupplier;
import com.workfusion.intake.api.domain.Document;
import com.workfusion.intake.api.domain.Transaction;
import com.workfusion.rpa.core.security.SecurityUtils;
import org.slf4j.Logger;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TransactionSupplierCollectProducts implements TransactionSupplier  {

    private final SecurityUtils securityUtils;
    
    private final Logger logger ;
    
    private Map<String, String> params;

    /**
     * To enable dependency injection, you need to apply @Inject annotation to your constructor.
     * @param logger will be injected without any configuration,
     * @param securityUtils injection was configured in com.workfusion.intakequickstart.core.SecurityModule
     * */
    @Inject
    public TransactionSupplierCollectProducts(final SecurityUtils securityUtils, final Logger logger, @Named("botConfigParams") Map<String, String> params) {
        this.logger = logger;
        this.securityUtils = securityUtils;
        this.params = params;
    }

    @Override
    public Collection<Transaction> get() {
        Collection<Transaction> transactions = new ArrayList<>();
        Transaction transaction = new Transaction();
        transaction.setId(uuid());
        transaction.setDocs(createDocuments());
        transactions.add(transaction);

        return transactions;
    }

    private List<Document> createDocuments() {
        final List<Document> collect = new InvoicePlaneBusinessRobot(securityUtils, logger, params).parseProductsToDocuments();
        return collect;
    }

    private String uuid() {
        return UUID.randomUUID().toString();
    }
    
}