package com.hmg.organizationservice.hystrix;

import com.hmg.organizationservice.utils.UserContextHolder;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariable;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableLifecycle;
import com.netflix.hystrix.strategy.properties.HystrixProperty;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

// Hystrix 병행성 젼략을 위한 클래스
public class ThreadLocalAwareStrategy extends HystrixConcurrencyStrategy{ // HystrixConcurrencyStrategy 클래스를 상속한다.
    private HystrixConcurrencyStrategy existingConcurrencyStrategy;

    // 스프링 클라우드가 미리 정의한 병행성 클래스를 이 클래스의 생성자에 전달한다.
    public ThreadLocalAwareStrategy(
            HystrixConcurrencyStrategy existingConcurrencyStrategy) {
        this.existingConcurrencyStrategy = existingConcurrencyStrategy;
    }

    // 일부 메서드의 재정의 필요.
    // existingCouncurrencyStrategy 메서드 구현을 호출하거나 부모 HystrixConcurrencyStrategy 메서드를 호출한다.
    @Override
    public BlockingQueue<Runnable> getBlockingQueue(int maxQueueSize) {
        return existingConcurrencyStrategy != null
                ? existingConcurrencyStrategy.getBlockingQueue(maxQueueSize)
                : super.getBlockingQueue(maxQueueSize);
    }

    @Override
    public <T> HystrixRequestVariable<T> getRequestVariable(
            HystrixRequestVariableLifecycle<T> rv) {
        return existingConcurrencyStrategy != null
                ? existingConcurrencyStrategy.getRequestVariable(rv)
                : super.getRequestVariable(rv);
    }

    @Override
    public ThreadPoolExecutor getThreadPool(HystrixThreadPoolKey threadPoolKey,
                                            HystrixProperty<Integer> corePoolSize,
                                            HystrixProperty<Integer> maximumPoolSize,
                                            HystrixProperty<Integer> keepAliveTime, TimeUnit unit,
                                            BlockingQueue<Runnable> workQueue) {
        return existingConcurrencyStrategy != null
                ? existingConcurrencyStrategy.getThreadPool(threadPoolKey, corePoolSize,
                maximumPoolSize, keepAliveTime, unit, workQueue)
                : super.getThreadPool(threadPoolKey, corePoolSize, maximumPoolSize,
                keepAliveTime, unit, workQueue);
    }

    // 히스트릭스로 보호된 메서드(callable) 호출 전 추가 작업 처리를 위해 새로운 Callable(DelegatingUserContextCallable)로 감싼다
    // 부모 스레드의 ThreadLocal 값을 히스트릭스로 보호된 메서드의 스레드로 전달한다.
    @Override
    public <T> Callable<T> wrapCallable(Callable<T> callable) {
        return existingConcurrencyStrategy != null
                ? existingConcurrencyStrategy
                .wrapCallable(new DelegatingUserContextCallable<T>(callable, UserContextHolder.getContext()))
                : super.wrapCallable(new DelegatingUserContextCallable<T>(callable, UserContextHolder.getContext()));
    }
}
