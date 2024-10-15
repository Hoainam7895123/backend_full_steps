package dev.hoainamtd.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI(@Value("${open.api.title}") String title,
                           @Value("${open.api.version}") String version,
                           @Value("${open.api.description}") String description,
                           @Value("${open.api.serverUrl}") String serverUrl,
                           @Value("${open.api.serverName}") String serverName
                           ) {
        return new OpenAPI().info(new Info()
                        .title(title)
                        .version(version)
                        .description(description)
                        .license(new License().name("API License").url("http://domain.vn/license")))
                .servers(List.of(new Server().url(serverUrl).description(serverName)));

    }

    //Tich hop cai url vd /v3/api-docs de tich vao micro service
    @Bean
    public GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("api-service-1")
                .packagesToScan("dev.hoainamtd.controller")
                .build();
    }
}
