package com.marvel.gps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.nio.ByteBuffer;
import java.util.Arrays;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String DEFAULT_INCLUDE_PATTERN = "/gps/.*";
    static final String STARTING_MESSAGE = "Starting Swagger with JWT";
    static final String STARTED_MESSAGE = "Started Swagger with JWT in {} ms";
    static final String MANAGEMENT_TITLE_SUFFIX = "Management API";
    static final String MANAGEMENT_GROUP_NAME = "management";
    static final String MANAGEMENT_DESCRIPTION = "Management endpoints documentation";
    private final Logger log = LoggerFactory.getLogger(SwaggerConfig.class);

    @Bean
    public Docket swaggerSpringfoxApiDocket() {
        log.debug(STARTING_MESSAGE);
        StopWatch watch = new StopWatch();
        watch.start();

        Docket docket = createDocket();

        Contact contact = new Contact(
                "Global Provisioning Service",
                "marvel.com",
                "admin.gps@marvel.com"
        );

        ApiInfo apiInfo = new ApiInfo(
                "Marvel Global Provisioning Service",
                "Handles User registration and VM Provisioning",
                "1.0",
                "Terms of service",
                "GPS",
                "",
                "");

        docket.securitySchemes(Arrays.asList((apiKey())))
                .securityContexts(Arrays.asList(
                        SecurityContext.builder()
                                .securityReferences(
                                        Arrays.asList(SecurityReference.builder()
                                                .reference("JWT")
                                                .scopes(new AuthorizationScope[0])
                                                .build()
                                        )
                                )
                                .forPaths(PathSelectors.regex(DEFAULT_INCLUDE_PATTERN))
                                .build())
                )
                .apiInfo(apiInfo)
                .forCodeGeneration(true)
                .directModelSubstitute(ByteBuffer.class, String.class)
                .genericModelSubstitutes(ResponseEntity.class)
                .ignoredParameterTypes(Pageable.class)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.marvel.gps.web.controller"))
                .paths(regex("/.*"))
                .build();

        watch.stop();
        log.debug(STARTED_MESSAGE, watch.getTotalTimeMillis());
        return docket;
    }

    protected Docket createDocket() {
        return new Docket(DocumentationType.SWAGGER_2);

    }

    private ApiKey apiKey() {
        return new ApiKey("JWT", AUTHORIZATION_HEADER, ApiKeyVehicle.HEADER.getValue());
    }

}
