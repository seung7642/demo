package com.demo.core.scan.filter;

import java.lang.annotation.*;

@Target(ElementType.TYPE) // 해당 애너테이션이 어디에 붙을건지를 명시합니다. (클래스, 필드, 메서드 등)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyExcludeComponent {
}
