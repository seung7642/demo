package com.demo.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
        // 예제 코드에 사용된 AppConfig.class 를 살리기 위해 아래 코드를 넣었습니다.
        // 실무에서는 아래와 같이 @Configuration 을 빼지 않고 다같이 컴포넌트 스캔합니다.
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class),

//        basePackages = "com.demo.core.member",
        basePackageClasses = AutoAppConfig.class
)
public class AutoAppConfig {


}
