package com.jobis.tax.core.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@OpenAPIDefinition(info = @Info(
        title = "샘플 OpenAPI Doc",
        description = "샘플 대해 설명하는 문서입니다.",
        version = "1.0"),
        servers = @Server(url = "/", description = "Default server url"))
@Configuration
public class SpringDocConfig {
    @Bean
    public OpenApiCustomiser openApiCustomiser() {
        return openApi -> openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {
            ApiResponses apiResponses = operation.getResponses();
            if (!apiResponses.containsKey("404")) {
                apiResponses.addApiResponse("404", new ApiResponse().description("Not Found"));
            }
            if (!apiResponses.containsKey("500")) {
                apiResponses.addApiResponse("500", new ApiResponse().description("Server Error"));
            }
        }));
    }

//    private ApiInfo apiInfo() {
//        Contact contact = new Contact("백승호","", "if_you_like@naver.com");
//
//        return new ApiInfo(
//                "백패커/아이디어스 과제",
//                "과제용으로 제작한 프로젝트입니다.",
//                "0.0.1",
//                "",
//                contact,
//                "The MIT License (MIT)",
//                "",
//                Collections.emptyList());
//    }
//
//    private ApiKey apiKey() {
//        return new ApiKey("JWT", "Authorization", "header");
//    }
//
//    private SecurityContext securityContext() {
//        return springfox
//                .documentation
//                .spi.service
//                .contexts
//                .SecurityContext
//                .builder()
//                .securityReferences(defaultAuth()).forPaths(PathSelectors.any()).build();
//    }
//
//    List<SecurityReference> defaultAuth() {
//        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
//        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
//        authorizationScopes[0] = authorizationScope;
//        return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
//    }
}
