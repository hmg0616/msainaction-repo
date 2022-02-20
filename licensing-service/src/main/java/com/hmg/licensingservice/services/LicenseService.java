package com.hmg.licensingservice.services;


import com.hmg.licensingservice.clients.OrganizationFeignClient;
import com.hmg.licensingservice.config.ServiceConfig;
import com.hmg.licensingservice.model.License;
import com.hmg.licensingservice.model.Organization;
import com.hmg.licensingservice.repository.LicenseRepository;
import com.hmg.licensingservice.utils.UserContextHolder;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// 클래스 레벨 히스트릭스 타임아웃 설정
//@DefaultProperties(
//        commandProperties = {
//                @HystrixProperty(name="execution.isolation.thread.timeoutInMiliseconds", value="10000")})
@Service
public class LicenseService {

    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    ServiceConfig config;

    @Autowired
    OrganizationFeignClient organizationFeignClient;

    private static final Logger logger = LoggerFactory.getLogger(LicenseService.class);

    private Organization retrieveOrgInfo(String organizationId){
        Organization organization = null;

        System.out.println("I am using the feign client");
        organization = organizationFeignClient.getOrganization(organizationId);

        return organization;
    }

    public License getLicense(String organizationId, String licenseId) {
        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);

        Organization org = retrieveOrgInfo(organizationId);

        return license
                .withOrganizationName( org.getName())
                .withContactName( org.getContactName())
                .withContactEmail( org.getContactEmail() )
                .withContactPhone( org.getContactPhone() )
                .withComment(config.getExampleProperty());
    }

    @HystrixCommand(
        fallbackMethod = "buildFallbackLicenseList",
        threadPoolKey = "licenseByOrgThreadPool",               // 스레드 풀의 고유 이름 정의
        threadPoolProperties = { // 스레드 풀 동작을 정의하고 설정
            @HystrixProperty(name="coreSize",value="30"),       // 스레드 풀의 스레드 개수 정의 (기본값 10)
            @HystrixProperty(name="maxQueueSize", value="10")}, // 스레드 풀 앞에 배치할 큐와 큐에 넣을 요청수 정의 (기본값 -1)
        commandProperties={ // 회로 차단기 동작 사용자 정의
            @HystrixProperty(name="circuitBreaker.requestVolumeThreshold", value="10"),     // 최소 연속 호출 횟수 (기본값 20)
            @HystrixProperty(name="circuitBreaker.errorThresholdPercentage", value="75"),   // 실패해야 하는 호출 비율 (기본값 50%)
            @HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds", value="7000"),// 차단된 후 서비스 회복 상태 확인할 때 까지 대기할 시간 간격 (기본값 5,000ms)
            @HystrixProperty(name="metrics.rollingStats.timeInMilliseconds", value="15000"),// 서비스 호출 문제를 모니터할 시간 간격 (기본값 10,000 ms)
            @HystrixProperty(name="metrics.rollingStats.numBuckets", value="5")}            // 설정한 시간 간격 동안 통계를 수집할 횟수 (기본값 10)
    )
    public List<License> getLicensesByOrg(String organizationId){
        logger.info("LicenseService.getLicensesByOrg Correlation id: {}", UserContextHolder.getContext().getCorrelationId());
        randomlyRunLong();

        return licenseRepository.findByOrganizationId(organizationId);
    }

    private List<License> buildFallbackLicenseList(String organizationId){
        List<License> fallbackList = new ArrayList<>();
        License license = new License()
                .withId("0000000-00-00000")
                .withOrganizationId( organizationId )
                .withProductName("Sorry no licensing information currently available");

        fallbackList.add(license);
        return fallbackList;
    }

    private void randomlyRunLong(){
        Random rand = new Random();
        int randomNum = rand.nextInt((3 - 1) + 1) + 1;
        if (randomNum==3) sleep();
    }

    private void sleep(){
        try {
            Thread.sleep(11000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
