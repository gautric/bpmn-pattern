 entityManagerFactory (type of this parameter is javax.persistence.EntityManagerFactory) 

 runtimeManager (type of this parameter is org.kie.api.runtime.manager.RuntimeManager) 

 kieSession (type of this parameter is org.kie.api.KieServices) 

 taskService (type of this parameter is org.kie.api.task.TaskService) 

 executorService (type of this parameter is org.kie.internal.executor.api.ExecutorService) 

 ksession taskService runtimeManager classLoader entityManagerFactory 

 ExecutorService executorService = (ExecutorService) ServiceRegistry.get().service(ServiceRegistry.EXECUTOR_SERVICE); 
