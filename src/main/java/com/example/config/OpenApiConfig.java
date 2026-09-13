package com.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    
    @Value("${server.port:8080}")
    private String myServerPort;
    
    @Bean
    public OpenAPI myCustomOpenApiConfiguration() {
        Server myLocalServer = new Server();
        myLocalServer.setUrl("http://localhost:" + myServerPort);
        myLocalServer.setDescription("Локальный сервер для разработки");
        
        Server myProductionServer = new Server();
        myProductionServer.setUrl("https://api.example.com");
        myProductionServer.setDescription("Продакшен сервер");
        
        Contact myContact = new Contact();
        myContact.setName("Поддержка User Service");
        myContact.setEmail("support@example.com");
        myContact.setUrl("https://example.com/contact");
        
        License myLicense = new License();
        myLicense.setName("Apache 2.0 License");
        myLicense.setUrl("https://www.apache.org/licenses/LICENSE-2.0");
        
        Info myApiInfo = new Info()
                .title("User Service API")
                .version("3.0.0")
                .description("""
                    REST API для управления пользователями с поддержкой HATEOAS.
                    
                    ### Основные возможности:
                    - Создание, чтение, обновление и удаление пользователей
                    - Поиск пользователей по email
                    - Валидация данных
                    - Полная документация через Swagger UI
                    - HATEOAS ссылки для навигации по API
                    """)
                .termsOfService("https://example.com/terms")
                .contact(myContact)
                .license(myLicense);
        
        return new OpenAPI()
                .info(myApiInfo)
                .servers(List.of(myLocalServer, myProductionServer));
    }
}