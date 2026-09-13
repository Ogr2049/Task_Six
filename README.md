Требования для проекта:

-Java 17

-Maven 3.8+

-PostgreSQL 14+

-Spring Boot 3.1.5

-SpringDoc OpenAPI 2.2.0 


Настройка базы данных
1. Создать базу данных:

CREATE DATABASE user_db;


2. Настроить подключение в application.yml:

  spring:

      datasource:
  
        url: jdbc:postgresql://localhost:5432/user_db
    
        username: your_username
    
        password: your_password

