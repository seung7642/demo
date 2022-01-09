package com.demo.core.beanfind;

import com.demo.core.AppConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApplicationContextInfoTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    // JUnit5 부터는 테스트 클래스명이나 메서드명에 접근 제한자(public)를 생략할 수 있습니다.
    @Test
    @DisplayName("모든 빈 출력하기")
    void findAllBean() throws Exception {
        // given (전제조건_이런게 주어졌을 때)
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = ac.getBean(beanDefinitionName);
            System.out.println("name = " + beanDefinitionName + ", object = " + bean);
        }

        // when (시나리오_이렇게 하면)

        // then (기대결과_이렇게 된다.)
    }

    @Test
    @DisplayName("애플리케이션 빈 출력하기")
    void findApplicationBean() throws Exception {
        // given (전제조건_이런게 주어졌을 때)
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName);

            // Role ROLE_APPLICATION: 직접 등록한 애플리케이션 빈
            // Role ROLE_INFRASTRUCTURE: 스프링이 내부에서 사용하는 빈
            if (beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) {
                Object bean = ac.getBean(beanDefinitionName);
                System.out.println("name = " + beanDefinitionName + ", object = " + bean);
            }
        }

        // when (시나리오_이렇게 하면)

        // then (기대결과_이렇게 된다.)
    }
}
