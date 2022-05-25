package com.outletcn.app.configuration;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.toolkit.Sequence;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import com.outletcn.app.interceptor.TokenInterceptor;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.function.Predicate;


/**
 * 配置
 *
 * @author linx
 * @since 2022-05-06 10:41
 */
@Configuration
@EnableKnife4j
@EnableSwagger2WebMvc
public class ApplicationConfig implements WebMvcConfigurer {

    @Value("${system.check-token:true}")
    private boolean checkToken;

    private final OpenApiExtensionResolver openApiExtensionResolver;

    @Autowired
    public ApplicationConfig(OpenApiExtensionResolver openApiExtensionResolver) {
        this.openApiExtensionResolver = openApiExtensionResolver;
    }

    /**
     * 注册自定义拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenInterceptor(checkToken))
                .addPathPatterns("/v1/api/**")
                .excludePathPatterns("/doc.html",
                        "/favicon.ico",
                        "/webjars/**",
                        "/swagger-resources/**",
                        "/v1/api/applet/operator/login",
                        "/v1/api/applet/operator/normal-login",
                        "/v1/api/applet/clock-in/login",
                        "/v1/api/applet/write-off/normal-login"
                );
    }

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("新奥莱打卡后台")
                        .description("新奥莱打卡小程序的Api接口文档")
                        .termsOfServiceUrl("https://www.phadata.net")
                        .contact(new Contact("linx", "", "xxx@phadata.com"))
                        .version("1.0.0")
                        .build())
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build().extensions(openApiExtensionResolver.buildExtensions("1.0"));
    }

    @Bean
    public Docket createAppletApi() {
        Predicate<RequestHandler> appletPackage = RequestHandlerSelectors.basePackage("com.outletcn.app.controller.applet");
        return new Docket(DocumentationType.SWAGGER_2).groupName("applet")
                .apiInfo(new ApiInfoBuilder()
                        .title("新奥莱打卡后台")
                        .description("新奥莱打卡小程序的Api接口文档")
                        .termsOfServiceUrl("https://www.phadata.net")
                        .contact(new Contact("linx", "", "xxx@phadata.com"))
                        .version("1.0.0")
                        .build())
                .useDefaultResponseMessages(false)
                .select()
                .apis(appletPackage)
                .paths(PathSelectors.any())
                .build().extensions(openApiExtensionResolver.buildExtensions("1.0"));
    }

    /**
     * mybatis-plus的分页配置
     *
     * @return
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    /**
     * 生成Sequence 雪花算法
     *
     * @return
     */
    @Bean
    public Sequence sequence() {
        return new Sequence();
    }

}
