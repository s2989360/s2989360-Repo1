package com.ibank.automation.invoicesusecase.app;

import com.google.inject.Inject;
import com.workfusion.intake.core.Module;
import com.workfusion.rpa.core.security.SecurityUtils;
import groovy.lang.Binding;
import org.codejargon.feather.Provides;

public class SecurityModule implements Module {

    private final Binding binding;

    @Inject
    public SecurityModule(Binding binding) {
        this.binding = binding;
    }

    @Provides
    public SecurityUtils securityUtils() {
        return new SecurityUtils(binding);
    }


}
