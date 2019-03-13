package co.amazing;

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

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
    @Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                                  
          .apis(RequestHandlerSelectors.basePackage("co.amazing.resource"))              
          .paths(PathSelectors.any())
          .build()
          .apiInfo(metadata());
    }

	private ApiInfo metadata() {
		Contact contact = new Contact("Danny Bastos", "https://github.com/dannybastos", "danny.bastos.br@gmail.com");
		return new ApiInfoBuilder()
				.title("Amazing Co API")
				.description("Api for the tradeshift challenge")
				.version("1.0.0")
				.contact(contact)
				.license("Apache License Version 2.0")
		        .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0\"")
		        .build();
	}
}