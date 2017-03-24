package com.capco.communicator.schema;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractProcessingContext {

    private List<String> errorLog = new ArrayList<>();

    /**
     * Inserts new error log to the errorLog list
     * */
    public abstract void addErrorLog(String log);

    protected List<String> getErrorLog(){
        return errorLog;
    }
}
