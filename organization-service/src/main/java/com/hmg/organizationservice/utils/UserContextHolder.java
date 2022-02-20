package com.hmg.organizationservice.utils;

import org.springframework.util.Assert;

// ThreadLocal에 UserContext 값을 저장하기 위한 클래스
public class UserContextHolder {
    // 정적 ThreadLocal 변수에 저장되는 UserContext
    private static ThreadLocal<UserContext> userContext = new ThreadLocal<>();

    // UserContext 객체를 사용하기 위해 가져오는 getContext 메서드
    public static final UserContext getContext() {
        UserContext context = userContext.get();

        if(context == null) {
            context = createEmptyContext();
            userContext.set(context);
        }
        return userContext.get();
    }

    public static final void setContext(UserContext context) {
        Assert.notNull(context, "Only non-null UserContext instances are permitted");
        userContext.set(context);
    }

    private static final UserContext createEmptyContext() {
        return new UserContext();
    }
}
