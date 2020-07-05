package net.a.g.jbpm.pattern.service;

import java.util.Map;

import org.kie.api.runtime.process.WorkItem;

public class ExceptionService {
        
    public String throwException(String message) throws IllegalAccessException {
     //   throw new RuntimeException("Service failed with input: " + message );
        throw new IllegalAccessException("Service failed with input: " + message );
    }

    public void handleException(WorkItem workItem) {
        System.out.println( "Handling exception caused by work item '" + workItem.getName() + "' (id: " + workItem.getId() + ")");
        
        Map<String, Object> params = workItem.getParameters();
        Throwable throwable = (Throwable) params.get("jbpm.workitem.exception");
        throwable.printStackTrace();
           
    }

}
