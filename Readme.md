
# Pattern BPMN

This project provides a lot of BPMN sample working with jBPM 7.X

### Project available

* AsyncTestProcess
* ExceptionTestProcess
* HumanTaskTestProcess
* SignalTestProcess
* SyncTestProcess
* TimerTestProcess

### Command Line w/ Kie-Server

You can follow all command to use some BPMN Process

#### Env Variable

export KIE_URL=http://localhost:8080

#### Swagger 

curl -u admin:admin ${KIE_URL}/rest/swagger.json 


#### List of command

> curl -u kieserver:kieserver123_ -X POST "${KIE_URL}/rest/server/containers/pattern/processes/TimerTestProcess/instances" -H "accept: application/json" -H "content-type: application/json" -d "{ \"booleanIn\": true, \"integerIn\": 123, \"stringIn\": \"TimerTestProcess\" , \"timerIn\": \"PT10S\" }"

> curl -u kieserver:kieserver123_ -X POST "${KIE_URL}/rest/server/containers/pattern/processes/AsyncTestProcess/instances" -H "accept: application/json" -H "content-type: application/json" -d "{ \"booleanIn\": true, \"integerIn\": 123, \"stringIn\": \"AsyncTestProcess\" }"

> curl -u kieserver:kieserver123_ -X POST "${KIE_URL}/rest/server/containers/pattern/processes/SyncTestProcess/instances" -H "accept: application/json" -H "content-type: application/json" -d "{ \"booleanIn\": true, \"integerIn\": 123, \"stringIn\": \"SyncTestProcess\" }"

> curl -u kieserver:kieserver123_ -X POST "${KIE_URL}/rest/server/containers/pattern/processes/HumanTaskTestProcess/instances" -H "accept: application/json" -H "content-type: application/json" -d "{ \"booleanIn\": true, \"integerIn\": 123, \"stringIn\": \"HumanTaskTestProcess\" }"

> curl -u kieserver:kieserver123_ -X POST "${KIE_URL}/rest/server/containers/pattern/processes/SignalTestProcess/instances" -H "accept: application/json" -H "content-type: application/json" -d "{ \"booleanIn\": true, \"integerIn\": 123, \"stringIn\": \"SignalTestProcess\" }"

> curl -u kieserver:kieserver123_ -X POST "${KIE_URL}/rest/server/containers/pattern/processes/instances/signal/ProcessSignal" -H "accept: application/json" -H "content-type: application/json" -d "\"migrationSignal\""

> curl -u kieserver:kieserver123_ -X PUT "${KIE_URL}/rest/server/containers/pattern/tasks/1/states/completed?auto-progress=true" -H "accept: application/json" -H "content-type: application/json" -d "{ \"booleanTask\": true, \"intergerTask\": 321, \"stringTask\": \"myStringTask\"}"