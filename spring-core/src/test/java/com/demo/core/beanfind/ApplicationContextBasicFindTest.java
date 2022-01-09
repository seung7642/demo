package com.demo.core.beanfind;

import com.demo.core.AppConfig;
import com.demo.core.member.MemberService;
import com.demo.core.member.MemberServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.*;

class ApplicationContextBasicFindTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @DisplayName("빈 이름으로 조회")
    public void findBeanByName() throws Exception {
        MemberService memberService = ac.getBean("memberService", MemberService.class);
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }

    @Test
    @DisplayName("이름없이 타입으로만 조회")
    public void findBeanByType() throws Exception {
        MemberService memberService = ac.getBean(MemberService.class);
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }

    @Test
    @DisplayName("구체 타입으로 조회")
    public void findBeanByName2() throws Exception {
        MemberService memberService = ac.getBean("memberService", MemberServiceImpl.class);
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }

    @Test
    @DisplayName("빈 이름으로 조회X")
    public void findBeanByNameX() throws Exception {
        // JUnit5에서 예외가 나는지 검증하기 위해선 assertj가 아닌 JUnit의 Assertions을 사용해야 합니다.
        // 두 번째 파라미터인 람다식의 로직이 실행되면, 첫 번째 파라미터에 명시된 예외가 발생한다는 의미입니다.
        // 정상적으로 첫 번째 파라미터에 명시된 예외가 나면 테스트가 성공합니다.
        org.junit.jupiter.api.Assertions.assertThrows(NoSuchBeanDefinitionException.class,
                () -> ac.getBean("xxxxx", MemberService.class));
    }
}
