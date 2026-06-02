## Functional Requirements

**Tasks**

1. Users can:  
   1. ✅ Retrieve all tasks assigned to themselves.   
   2. ✅ Retrieve a specific task assigned to themselves.  
   3. ⚠️ Update the status of their own tasks only.
      > **Partial fail:** endpoint works for their own tasks, but when a USER updates a task not assigned to them the app returns `500 "Task not found"` instead of `403`. The service should either throw a 403 or return the task as "not found" with a 404 — a 500 leaks that an unhandled exception occurred.
2. Manager can:  
   1. ✅ Create a task  
   2. ✅ Assign task to its team member  
   3. ✅ Update tasks created by themselves.  
   4. ✅ View all tasks of its team (or created by themselves)   
3. Admin can:  
   1. ✅ View all tasks

**Roles**

1. ✅ The system defines three fixed roles: ADMIN, MANAGER, USER  
2. ❌ Only ADMIN can retrieve the list of all roles.
   > **Fail:** No roles endpoint exists anywhere in the codebase. There is no `RoleController` and no `/role` or `/roles` route. This requirement is completely unimplemented.

**Users**

1. Admin can:  
   1. ✅ Create any user (Admin, Manager, User)  
   2. ✅ Restrict any user (Manager, User)  
   3. ❌ Retrieve all users (Manager, User)
      > **Fail:** `POST /user/user-details` retrieves a **single** user by UUID — there is no paginated list endpoint for all users. The requirement asks for listing all users.
2. Manager can:  
   1. ✅ Retrieve only assigned users (User)  
3. User can:  
   1. ✅ Get their own User information

---

## Non Functional Requirements

1. ✅ Business logic resides in the service layer; controllers and repositories are clean.
2. ✅ Database schema managed via Liquibase (11 changesets).
3. ✅ Pagination/Sorting implemented. Default page size is 5, configurable per request.
4. ✅ DTO pattern used for request and response objects.
5. ❌ Mappers for conversions between DTOs and entities.
   > **Fail:** No dedicated mapper classes found (no MapStruct, no ModelMapper, no hand-rolled `XxxMapper` classes). DTOs are constructed inline inside service methods, mixing mapping logic into business logic.
6. ❌ Transactional boundaries enforced.
   > **Fail:** No `@Transactional` annotations found anywhere in the codebase. Multi-step writes (e.g., `addMembersInTeam` saves a list of `TeamMember` records) are not wrapped in a transaction — a partial failure leaves the database in an inconsistent state.
7. ✅ UUIDs used to expose the external identity of domain objects.
8. ❌ Comprehensive security validation.
   > **Fail (critical):** On login, validate user and then generate JWT token and use existing Spring Security Filters to validate JWT token without validating user credentials from DB. 
9. ⚠️ Standardized return types:
    > Use `ResponseEntity` with DTO Type
10. ⚠️ Unused code removal.
    > **Partial:** Several issues — (a) `JwtAuthenticationFilter` body still contains a dead debug block (`//===== FOR CHECKING =======`); (b) `AuthService.login()` has 12 lines of commented-out `authenticationManager` code left in production code; (c) `@Autowired` + `@AllArgsConstructor` duplicate on `CustomUserDetailService`.
11. ✅ User roles verified by Spring Security via JWT tokens (`@PreAuthorize`).
12. ✅ Logging configured — console pattern with traceId, `RequestFlowLoggingAspect` for AOP-based request tracing, Hibernate SQL logging enabled.
13. ❌ No Docker Compose file is provided.
14. ❌ Swagger UI integration.
    > **Fail:** No `springdoc-openapi` (or equivalent) dependency in `pom.xml`. `GET /swagger-ui/index.html` returns 401. No API documentation is available for on-the-fly testing.
15. ⚠️ Proper error handling.
    > **Partial:** Role-boundary violations correctly return 403 (e.g., ADMIN creating a task, USER accessing manager endpoints). However: (a) cross-user task status update returns `500 "Task not found"` instead of 403/404; (b) `POST /auth/register` returns the raw `User` entity including the **bcrypt password hash**, `id`, and internal timestamps — a DTO should be used here instead.
