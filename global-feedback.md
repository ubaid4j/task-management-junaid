* Do not use lombok as it is simply add difference in source code and compiled one.
  * You may use Java Records and Constructor Injection.
* Use ResponseEntity from Spring framework instead of AppResponse
* Do not send Entity back to Client, use DTO pattern.
* Use Records
* Implement transaction
* Use better naming conventions like instead of CreateTaskResponse, use TaskDTO or TaskRequestDTO etc (see src/main/java/com/example/java_final_assignment/service/response)