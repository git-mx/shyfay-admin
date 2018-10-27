package com.shyfay.admin.config;


import com.shyfay.admin.web.interceptor.SessionInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.MultipartConfigElement;
import java.math.BigDecimal;
import java.util.List;

/**
 * 启动配置类，用于配置静态资源访问权限和后台接口拦截器
 *
 * @author 牟雪
 * @since 2018/7/31
 */
@Configuration
//@EnableWebMvc //如果继承 WebMvcConfigurerAdapter 可不写
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/adminH5/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/adminH5/");
        registry.addResourceHandler("/ueditor/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/ueditor/");
//        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/").setCachePeriod(0);
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/").setCachePeriod(0);
        super.addResourceHandlers(registry);
    }

    @Bean
    public SessionInterceptor sessionInterceptor(){
        return  new SessionInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(sessionInterceptor())
//                .addPathPatterns("/student/**")
//                .addPathPatterns("/user/**")
//                .addPathPatterns("/department/**")
//                .addPathPatterns("/position/**")
//                .addPathPatterns("/login/userExit")
//                .addPathPatterns("/login/modifyPassword");
        super.addInterceptors(registry);

    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        //序列化将BigDecimal转String类型
        SimpleModule bigDecimalModule = new SimpleModule();
        bigDecimalModule.addSerializer(BigDecimal.class, ToStringSerializer.instance);
        objectMapper.registerModule(bigDecimalModule);
        jackson2HttpMessageConverter.setObjectMapper(objectMapper);
        converters.add(jackson2HttpMessageConverter);
        super.configureMessageConverters(converters);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true).maxAge(3600);
    }

  /*  @Bean
    public FilterRegistrationBean xssFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean(new WebAppAllFilter());
        registration.setName("xssFilter");
        registration.addUrlPatterns("*//*");
        registration.setOrder(1);
        return registration;
    }*/


    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //设置单次文件上传最大限制
        factory.setMaxFileSize("5MB");
        /// 设置总上传数据总大小
        //factory.setMaxRequestSize("102400KB");
        return factory.createMultipartConfig();
    }
}
