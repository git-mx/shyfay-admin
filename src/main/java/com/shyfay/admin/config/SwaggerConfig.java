package com.shyfay.admin.config;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

/**
 * Swagger配置
 *
 * @author huangjie
 * @since 2017-02-23
 */

@Configuration
@EnableSwagger2
@ComponentScan(basePackages = {"com.shyfay.admin.web.controller"})
public class SwaggerConfig {

    private final static String SERVICE_URL = "http://ip:port/swagger-ui.html";
    private final static String AUTHOR_NAME = "爱丽丝项目组";
    private final static String AUTHOR_URL = "http://www.syswin.com";
    private final static String AUTHOR_EMAIL = "muxue@syswin.com";

    private final static String OPEN_API_PATH = "(/domain/.*)|(/login/getCode.*)|(/login/userLogin.*)|(/wx/.*)";
    private final static String INNER_API_PATH = "(/student/.*)|(/department/.*)|(/position/.*)|(/user/.*)|(/login/userExit)|(/login/modifyPassword)|(/upload/.*)";

    @Bean
    public Docket innerApi(){
        return createDocket("需要验证的接口", "需要验证的接口", INNER_API_PATH, "内部接口")
                .globalOperationParameters(getTokenParameter());
    }

    @Bean
    public Docket openApi(){
        return createDocket("不需要验证的接口", "不需要验证的接口", OPEN_API_PATH, "外部接口");
    }

    private Docket createDocket(String title, String description, String path, String groupName){

        ApiInfo apiInfo = new ApiInfoBuilder()
                .title(title)
                .description(description)
                .termsOfServiceUrl(SERVICE_URL)
                .contact(new Contact(AUTHOR_NAME, AUTHOR_URL, AUTHOR_EMAIL))
                .version("v1.0.0")
                .build();
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.shyfay.admin.web.controller"))
                .paths(PathSelectors.regex(path))
                .build()
                .groupName(groupName)
                .enable(true)
                .apiInfo(apiInfo);
    }

    private List<Parameter> getTokenParameter(){
        ParameterBuilder builder = new ParameterBuilder();
        builder.name("sessionId")
                .description("sessionId")
                .modelRef(new ModelRef("String"))
                .parameterType("header")
                .required(true)
                .build();
        return Lists.newArrayList(builder.build());
    }
}