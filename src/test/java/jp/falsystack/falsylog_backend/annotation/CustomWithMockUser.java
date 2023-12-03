package jp.falsystack.falsylog_backend.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = MockUserFactory.class)
public @interface CustomWithMockUser {

  String email() default "mockuser@test.com";
  String name() default "test-user";
  String password() default "1q2w3e4r";

  // int level() default 5;

  // String mobileNumber() default "08012341234";
}
