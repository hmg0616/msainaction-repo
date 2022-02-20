package com.hmg.organizationservice.hystrix;

import com.hmg.organizationservice.utils.UserContext;
import com.hmg.organizationservice.utils.UserContextHolder;

import java.util.concurrent.Callable;

public final class DelegatingUserContextCallable<V> implements Callable<V> {
    private final Callable<V> delegate;
    private UserContext originalUserContext;

    // 히스트릭스로 보호된 코드를 호출하는 원본 Callable과 부모 스레드에서 받은 UserContext 전달
    public DelegatingUserContextCallable(Callable<V> delegate,
                                             UserContext userContext) {
        this.delegate = delegate;
        this.originalUserContext = userContext;
    }

    // @HystrixCommand 애너테이션이 메서드를 보호하기 전에 호출되는 Call() 함수
    public V call() throws Exception {
        // UserContext 설정. UserContext를 저장하는 ThreadLocal 변수는 히스트릭스가 보호하는 메서드를 실행하는 스레드에 연결된다.
        UserContextHolder.setContext( originalUserContext );

        try {
            // UserContext 설정 후 히스트릭스가 보호하는 메서드 실행
            return delegate.call();
        }
        finally {
            this.originalUserContext = null;
        }
    }

    public static <V> Callable<V> create(Callable<V> delegate,
                                         UserContext userContext) {
        return new DelegatingUserContextCallable<V>(delegate, userContext);
    }
}