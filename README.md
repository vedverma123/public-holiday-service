# Public Holiday Service

The Public Holiday Management Service handles fetching, storing, and notifying employees about upcoming public holidays based on their location. It integrates with external APIs to retrieve holidays and uses Feign clients to communicate with other services, such as the Employee Service.

## Technologies
1. Java 17
2. JPA (Hibernate)
3. Lombok
4. Spring Boot
5. Spring Retry
6. Spring Cache
7. Feign Client
8. RestTemplate
9. Scheduled Tasks

### Features
1. **Email Alerts**: Asynchronous email notifications about upcoming holidays.
2. **Public Holiday** Fetching: Fetches holidays from an external API and stores them.
3. **Scheduled Tasks**: A scheduler fetches holidays monthly.
4. **Cache Management**: Caches upcoming holidays for optimization.
5. **Event Publishing**: Publishes holiday events for employee notifications.

## In Memory Database
Currently, application is configured to run with H2 database accessible at url [http://localhost:8551/h2-console/login.jsp]()
with following properties.

```
   JDBC URL : jdbc:h2:mem:public_holiday_service
   User name : sa
   Password : password
```   


