package com.basic.microservices.api_gateway;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class ApiGatewayConfiguration {
    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p.path("/get")
               .filters(f-> f.addRequestHeader("MyHeader","MyUri")
                       .addRequestParameter("Param","MyValue"))
               .uri("http://httpbin.org:80"))
                .route(p -> p.path("/currency-exchange/**") // Đây là api
                        .uri("lb://currency-exchange")) // Đây là các tên đã đăng ký trên eureka
                .route(p -> p.path("/currency-conversion/**")
                        .uri("lb://currency-conversion"))
                .route(p -> p.path("/currency-conversion-feign/**")
                        .uri("lb://currency-conversion"))
                .route(p -> p.path("/currency-conversion-new/**")
                        .filters(f -> f.rewritePath(
                                "/currency-conversion-new/(?<segment>.*)", // Thay đổi đường dẫn từ -feign thành -new.
                                // (?<segment>.*) Biểu thức chính quy lấy ra phần sau / rồi gắn xuống dưới
                                "/currency-conversion-feign/${segment}"
                        ))
                        .uri("lb://currency-conversion"))
                .build();
    }
}
