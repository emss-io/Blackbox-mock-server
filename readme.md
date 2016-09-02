# Blackbox Mock Server Testing

### Description
This repo shows how to enable blackbox testing for the `MockRestServiceServer`. Using the convenience methods of this 
class `MockRestServiceServer.createServer()` will cause the expectations to be ordered. 

When running blackbox integration testing, the order of execution for `RestTemplate` requests is often not important. 
This code demonstrates how to disable this order verification.

### Execution
Run `./gradlew test` and you will see one test succeed and the other fail. The test that succeed has the order verification
 disabled while the test that fails has the order verification enabled.