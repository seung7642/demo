package com.example.springbootrest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@EnableSwagger2
@SpringBootApplication
public class SpringBootRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootRestApplication.class, args);
    }

    @Bean
    public Docket api() {
        final ApiInfo apiInfo = new ApiInfoBuilder()
                .title("Vote Rest API")
                .description("<h3>Vote Rest api의 문서 제공</h3>")
                .build();

        ParameterBuilder parameterBuilder = new ParameterBuilder();
        parameterBuilder.name("x-user-id")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(true)
                .build();
        List<Parameter> params = new ArrayList<>();
        params.add(parameterBuilder.build());

        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .groupName("voteWS")
                .apiInfo(apiInfo)
                .globalOperationParameters(params)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.springbootrest.controller"))
                .build();
        return docket;
    }
}
