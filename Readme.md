
# Pattern BPMN

This project provides a lot of BPMN sample working with jBPM 7.X

### Project available

* AsyncTestProcess
* ExceptionTestProcess
* HumanTaskTestProcess
* SignalTestProcess
* SyncTestProcess
* TimerTestProcess


### Build Maven Project

#### Build project for RHPAM product version

	mvn clean deploy -Predhat-product 

#### Build project for jBPM community version

	mvn clean deploy -Pjbpm-community

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


curl -u ${KIE_AUTH}  -X GET "http://localhost:8090/rest/server/containers/pattern_3.0.0/processes/definitions/TimerTestProcess/variables" -H  "accept: application/json"

curl -u ${KIE_AUTH}  -X GET "http://localhost:8090/rest/server/containers/pattern_3.0.0/processes/definitions/ExceptionTestProcess/variables" -H  "accept: application/json"


curl -u ${KIE_AUTH} -X PUT "http://localhost:8090/rest/server/queries/definitions/GetProcessWithPerson2" -H  "accept: application/json" -H  "Content-Type: application/json" -d "{ \"query-name\" : \"GetProcessWithPerson2\", \"query-source\" : \"java:jboss/datasources/FFFFF\", \"query-expression\" : \"SELECT PROCESSINSTANCELOG.*, PERSON.EMAIL, PERSON.NAME, PERSON.COUNTRY FROM  PROCESSINSTANCELOG,  PERSON WHERE   PERSON.EMAIL IS NOT NULL\", \"query-target\" : \"CUSTOM\"}"
curl -u ${KIE_AUTH} -X PUT "http://localhost:8090/rest/server/queries/definitions/GetProcessWithPerson" -H  "accept: application/json" -H  "Content-Type: application/json" -d "{ \"query-name\" : \"GetProcessWithPerson\", \"query-source\" : \"java:jboss/datasources/ExampleDS\", \"query-expression\" : \"SELECT PROCESSINSTANCELOG.*, PERSON.EMAIL, PERSON.NAME, PERSON.COUNTRY FROM  PROCESSINSTANCELOG,  PERSON WHERE   PERSON.EMAIL IS NOT NULL\", \"query-target\" : \"CUSTOM\"}"



curl -u ${KIE_AUTH} -H  "accept: application/json" -X GET "http://localhost:8090/rest/server/queries/definitions/GetProcessWithPerson/data?mapper=RawList&page=0&pageSize=10"  | jq


curl -u ${KIE_AUTH} -H  "accept: application/json" -X GET "http://localhost:8090/rest/server/queries/definitions/GetProcessWithPerson2/data?mapper=RawList&page=0&pageSize=10"  | jq



curl -u ${KIE_AUTH} -X GET "http://localhost:8090/rest/server/queries/tasks/instances?page=0&pageSize=10&sortOrder=true" -H  "accept: application/json"



curl -u ${KIE_AUTH} -X POST "${KIE_URL}/rest/server/containers/${KIE_CONTAINER}/processes/PersonTestProcess/instances" -H "accept: application/json" -H "content-type: application/json" -d "{ \"personIn\": {\"net.a.g.jbpm.pattern.model.Person\" : { \"name\" : \"TOTO TOTO\", \"country\" : \"France\" , \"email\" : \"jean@totoo.net\" } } } " 