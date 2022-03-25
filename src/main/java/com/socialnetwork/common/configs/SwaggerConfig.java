package com.socialnetwork.common.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <b>Tác dụng: </b>config swagger (hỗ trợ docs API)
 * @author Mạnh Hùng
 *
 */

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.regex("/api/.*"))
                .build()
                .apiInfo(apiEndPointsInfo());
    }
    
    private ApiInfo apiEndPointsInfo(){
        return new ApiInfoBuilder().title("Social Network API")
                .description("Dự án mạng xã hội SNS")
                .version("1.0")
//                .contact(new Contact("Mạnh Hùng","",""))
                .build();
    }
}
