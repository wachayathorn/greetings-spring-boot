# In-memory H2 with Spring Data JPA

Rooms, users, bookings, and sessions are stored in an in-memory H2 database through Spring Data JPA. Restarting the process drops every row.

The current app uses Spring Data JDBC, Liquibase, and Postgres. Those stay out of this system because the requested stack is H2 in memory and JPA, and a restart that clears bookings is an accepted consequence of that choice.
