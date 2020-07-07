
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
export KIE_AUTH=admin:admin  or kieserver:kieserver123_

#### Swagger 

curl -u ${KIE_AUTH} ${KIE_URL}/rest/swagger.json 


#### List of command

> curl -u ${KIE_AUTH} -X POST "${KIE_URL}/rest/server/containers/pattern/processes/TimerTestProcess/instances" -H "accept: application/json" -H "content-type: application/json" -d "{ \"booleanIn\": true, \"integerIn\": 123, \"stringIn\": \"TimerTestProcess\" , \"timerIn\": \"PT10S\" }"

> curl -u ${KIE_AUTH} -X POST "${KIE_URL}/rest/server/containers/pattern/processes/AsyncTestProcess/instances" -H "accept: application/json" -H "content-type: application/json" -d "{ \"booleanIn\": true, \"integerIn\": 123, \"stringIn\": \"AsyncTestProcess\" }"

> curl -u ${KIE_AUTH} -X POST "${KIE_URL}/rest/server/containers/pattern/processes/SyncTestProcess/instances" -H "accept: application/json" -H "content-type: application/json" -d "{ \"booleanIn\": true, \"integerIn\": 123, \"stringIn\": \"SyncTestProcess\" }"

> curl -u ${KIE_AUTH} -X POST "${KIE_URL}/rest/server/containers/pattern/processes/HumanTaskTestProcess/instances" -H "accept: application/json" -H "content-type: application/json" -d "{ \"booleanIn\": true, \"integerIn\": 123, \"stringIn\": \"HumanTaskTestProcess\" }"

> curl -u ${KIE_AUTH} -X POST "${KIE_URL}/rest/server/containers/pattern/processes/SignalTestProcess/instances" -H "accept: application/json" -H "content-type: application/json" -d "{ \"booleanIn\": true, \"integerIn\": 123, \"stringIn\": \"SignalTestProcess\" }"

> curl -u ${KIE_AUTH} -X POST "${KIE_URL}/rest/server/containers/pattern/processes/instances/signal/ProcessSignal" -H "accept: application/json" -H "content-type: application/json" -d "\"migrationSignal\""

> curl -u ${KIE_AUTH} -X PUT "${KIE_URL}/rest/server/containers/pattern/tasks/1/states/completed?auto-progress=true" -H "accept: application/json" -H "content-type: application/json" -d "{ \"booleanTask\": true, \"intergerTask\": 321, \"stringTask\": \"myStringTask\"}"