package com.ibank.automation.invoicesusecase.app;

import com.ibank.automation.invoicesusecase.processor.TransactionImporter;
import com.workfusion.intake.api.domain.Transaction;
import com.workfusion.intake.core.App;
import com.workfusion.intake.core.Module;
import com.workfusion.intake.service.TransactionService;
import com.workfusion.intake.service.serialization.BusinessProcessFormats;
import groovy.lang.Binding;
import java.util.List;
import java.util.Map;

public class AppInvoicePlane extends App {

    AppInvoicePlane(Binding context, Map<String, String> params, List<Module> additionalModules, List<Module> overrideModules, Object injectContext) {
        super(context, additionalModules, overrideModules, injectContext);
    }

    public static AppBuilderInvoicePlane init(final Binding binding) {
        return new AppBuilderInvoicePlane(binding);
    }

    public String exportTransactionAsJson(final String transactionId) {
        TransactionService transactionService = getInstance(TransactionService.class);
        Transaction transaction = transactionService.byId(transactionId);
        return (transaction != null) ? BusinessProcessFormats.toJson(transaction) : "{}";
    }

    public Transaction importTransactionFromJson(final String transactionId, final String json) {
        TransactionImporter transactionImporter = getInstance(TransactionImporter.class);
        Transaction parsedTransaction = BusinessProcessFormats.fromJson(json);
        Transaction importedTransaction = transactionImporter.transform(parsedTransaction);

        TransactionService transactionService = getInstance(TransactionService.class);
        transactionService.update(importedTransaction);
        return importedTransaction;
    }

}