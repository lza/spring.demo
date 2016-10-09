package org.leon.spring.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by leon on 2016/9/16.
 */
@Configuration
@EnableWebMvc
@EnableSwagger2
public class SwaggerConfig {
    private final Logger logger = LoggerFactory.getLogger(SwaggerConfig.class);

    @Value("#{config['swagger.host']}")
    private String host;
    @Value("#{config['swagger.apiInfo.title']}")
    private String title;
    @Value("#{config['swagger.apiInfo.description']}")
    private String description;
    @Value("#{config['swagger.apiInfo.serviceTerms']}")
    private String serviceTerms;
    @Value("#{config['swagger.apiInfo.license']}")
    private String license;
    @Value("#{config['swagger.apiInfo.licenseUrl']}")
    private String licenseUrl;
    @Value("#{config['build.time']}")
    private String version;
    @Value("#{config['swagger.apiInfo.contact.name']}")
    private String contactName;
    @Value("#{config['swagger.apiInfo.contact.url']}")
    private String contactUrl;
    @Value("#{config['swagger.apiInfo.contact.email']}")
    private String contactEmail;

    @Bean
    public Docket swaggerSpringfoxDocket() {
        logger.debug("Starting Swagger");
        String packagePath = this.getClass().getPackage().getName();
        packagePath = packagePath.substring(0, packagePath.lastIndexOf("."));
        logger.debug("base package:" + packagePath);
        StopWatch watch = new StopWatch();
        watch.start();
        Docket swaggerSpringMvcPlugin = new Docket(DocumentationType.SWAGGER_2)
                .host(host)
                .apiInfo(apiInfo())
                .genericModelSubstitutes(ResponseEntity.class)
                .select()
                .apis(RequestHandlerSelectors.basePackage(packagePath))
                .paths(PathSelectors.any())
                .build();
        watch.stop();
        logger.debug("Started Swagger in {} ms", watch.getTotalTimeMillis());
        return swaggerSpringMvcPlugin;
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .termsOfServiceUrl(serviceTerms)
                .contact(new Contact(contactName, contactUrl, contactEmail))
                .license(license)
                .licenseUrl(licenseUrl)
                .version(version)
                .build();
    }
}
