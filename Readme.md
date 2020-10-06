
# Pattern BPMN

This project provides a lot of BPMN sample working with jBPM 7.X

### Project available

* AsyncTestProcess
* ExceptionTestProcess
* ExceptionToErrorProcess
* HumanTaskTestProcess
* SignalTestProcess
* SyncTestProcess
* TimerTestProcess
* JavaScriptProcess
* MonitoringProcess


### Build Maven Project



#### Build project for jBPM community version

Don't change anything and execute : 

	mvn clean package
	
#### Build project for RHPAM product version

Change `dependencyManagement` by using `RHPAM Product` block into pom.xml and execute : 

	mvn clean package

### Command Line w/ Kie-Server

You can follow all commands to use some BPMN Process

#### Env Variable


	export KIE_URL=http://localhost:8080

	export KIE_AUTH=admin:admin  or kieserver:kieserver123_
	
	export KIE_CONTAINER=pattern_3.0.0


#### Swagger 

```
curl -u ${KIE_AUTH} ${KIE_URL}/rest/swagger.json 
```

#### List of command

##### TimerProcess

```
curl -u ${KIE_AUTH} -X POST "${KIE_URL}/rest/server/containers/${KIE_CONTAINER}/processes/TimerTestProcess/instances" -H "accept: application/json" -H "content-type: application/json" -d "{ \"booleanIn\": true, \"integerIn\": 123, \"stringIn\": \"TimerTestProcess\" , \"timerIn\": \"PT10S\" }"
```
##### AsyncProcess

```
curl -u ${KIE_AUTH} -X POST "${KIE_URL}/rest/server/containers/${KIE_CONTAINER}/processes/AsyncTestProcess/instances" -H "accept: application/json" -H "content-type: application/json" -d "{ \"booleanIn\": true, \"integerIn\": 123, \"stringIn\": \"AsyncTestProcess\" }"
```
##### SyncProcess
```
curl -u ${KIE_AUTH} -X POST "${KIE_URL}/rest/server/containers/${KIE_CONTAINER}/processes/SyncTestProcess/instances" -H "accept: application/json" -H "content-type: application/json" -d "{ \"booleanIn\": true, \"integerIn\": 123, \"stringIn\": \"SyncTestProcess\" }"
```
##### HumanTaskProcess
```
curl -u ${KIE_AUTH} -X POST "${KIE_URL}/rest/server/containers/${KIE_CONTAINER}/processes/HumanTaskTestProcess/instances" -H "accept: application/json" -H "content-type: application/json" -d "{ \"booleanIn\": true, \"integerIn\": 123, \"stringIn\": \"HumanTaskTestProcess\" }"
```

```
curl -u ${KIE_AUTH} -X PUT "${KIE_URL}/rest/server/containers/${KIE_CONTAINER}/tasks/1/states/completed?auto-progress=true" -H "accept: application/json" -H "content-type: application/json" -d "{ \"booleanTask\": true, \"intergerTask\": 321, \"stringTask\": \"myStringTask\"}"
```

##### SignalProcess
```
curl -u ${KIE_AUTH} -X POST "${KIE_URL}/rest/server/containers/${KIE_CONTAINER}/processes/SignalTestProcess/instances" -H "accept: application/json" -H "content-type: application/json" -d "{ \"booleanIn\": true, \"integerIn\": 123, \"stringIn\": \"SignalTestProcess\" }"
```

```
curl -u ${KIE_AUTH} -X POST "${KIE_URL}/rest/server/containers/${KIE_CONTAINER}/processes/instances/signal/ProcessSignal" -H "accept: application/json" -H "content-type: application/json" -d "\"migrationSignal\""
```

##### PersonProcess

```
curl -u ${KIE_AUTH} -X POST "${KIE_URL}/rest/server/containers/${KIE_CONTAINER}/processes/PersonTestProcess/instances" -H "accept: application/json" -H "content-type: application/json" -d "{ \"personIn\": {\"net.a.g.jbpm.pattern.model.Person\" : { \"name\" : \"Jean Paul\", \"country\" : \"France\" , \"email\" : \"jean@paul.net\" } } } " 
```

##### ExceptionToErrorProcess

```
curl -u ${KIE_AUTH} -X POST "${KIE_URL}/rest/server/containers/${KIE_CONTAINER}/processes/ExceptionToErrorProcess/instances" -H "accept: application/json" -H "content-type: application/json"
```
