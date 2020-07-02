
List of command


> curl -u kieserver:kieserver123_ -X POST "http://localhost:8080/rest/server/containers/pattern/processes/TimerTestProcess/instances" -H "accept: application/json" -H "content-type: application/json" -d "{ \"booleanIn\": true, \"integerIn\": 123, \"stringIn\": \"TimerTestProcess\" , \"timerIn\": \"PT10S\" }"

> curl -u kieserver:kieserver123_ -X POST "http://localhost:8080/rest/server/containers/pattern/processes/AsyncTestProcess/instances" -H "accept: application/json" -H "content-type: application/json" -d "{ \"booleanIn\": true, \"integerIn\": 123, \"stringIn\": \"AsyncTestProcess\" }"

> curl -u kieserver:kieserver123_ -X POST "http://localhost:8080/rest/server/containers/pattern/processes/SyncTestProcess/instances" -H "accept: application/json" -H "content-type: application/json" -d "{ \"booleanIn\": true, \"integerIn\": 123, \"stringIn\": \"SyncTestProcess\" }"

> curl -u kieserver:kieserver123_ -X POST "http://localhost:8080/rest/server/containers/pattern/processes/HumanTaskTestProcess/instances" -H "accept: application/json" -H "content-type: application/json" -d "{ \"booleanIn\": true, \"integerIn\": 123, \"stringIn\": \"HumanTaskTestProcess\" }"

> curl -u kieserver:kieserver123_ -X POST "http://localhost:8080/rest/server/containers/pattern/processes/SignalTestProcess/instances" -H "accept: application/json" -H "content-type: application/json" -d "{ \"booleanIn\": true, \"integerIn\": 123, \"stringIn\": \"SignalTestProcess\" }"

curl -u kieserver:kieserver123_ -X POST "http://localhost:8080/rest/server/containers/pattern/processes/instances/signal/ProcessSignal" -H "accept: application/json" -H "content-type: application/json" -d "\"migrationSignal\""

curl -u kieserver:kieserver123_ -X PUT "http://localhost:8080/rest/server/containers/pattern/tasks/1/states/completed?auto-progress=true" -H "accept: application/json" -H "content-type: application/json" -d "{ \"booleanTask\": true, \"intergerTask\": 321, \"stringTask\": \"myStringTask\"}"