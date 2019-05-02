package com.ibank.automation.invoicesusecase.app;

import com.workfusion.intake.core.Module;
import org.codejargon.feather.Provides;
import java.util.Map;
import javax.inject.Named;

public class ModuleInvoicePlane implements Module {

    private Map<String, String> params;

    public ModuleInvoicePlane(final Map<String, String> botConfigParams) {
        this.params = botConfigParams;
    }
    
    @Provides
    @Named("botConfigParams")
    public Map<String, String> params() {
        return params;
    }
    
    @Provides
    @Named("defaultS3Bucket")
    public String s3Bucket() {
        return "doc-upload";
    }

}
