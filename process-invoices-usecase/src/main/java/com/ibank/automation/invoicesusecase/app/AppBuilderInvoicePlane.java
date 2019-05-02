package com.ibank.automation.invoicesusecase.app;

import com.workfusion.intake.core.Module;
import groovy.lang.Binding;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppBuilderInvoicePlane {
	
    private Binding context;
    
    private Map<String, String> params = new HashMap<>();
    
    private List<Module> overrideModules;
    
    private Object injectContext;

    public AppBuilderInvoicePlane(Binding context) {
        this.context = context;
    }

    public AppBuilderInvoicePlane params(Map<String, String> params) {
        this.setParams(params);
        return this;
    }

    public AppBuilderInvoicePlane override(Module... modules) {
        overrideModules = Arrays.asList(modules);
        return this;
    }

    public AppBuilderInvoicePlane injectFields(Object context) {
        this.injectContext = context;
        return this;
    }

    public AppInvoicePlane get() {
        Module myInvoicePlaneModule = new ModuleInvoicePlane(params);
        Module securityModule = new SecurityModule(context);

        List<Module> modules = Arrays.asList(myInvoicePlaneModule, securityModule);
        return new AppInvoicePlane(context, params, modules, overrideModules, injectContext);
    }
    
	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	
}