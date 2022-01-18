package com.hmg.licensingservice.services;


import com.hmg.licensingservice.clients.OrganizationFeignClient;
import com.hmg.licensingservice.config.ServiceConfig;
import com.hmg.licensingservice.model.License;
import com.hmg.licensingservice.model.Organization;
import com.hmg.licensingservice.repository.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LicenseService {

    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    ServiceConfig config;

    @Autowired
    OrganizationFeignClient organizationFeignClient;

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
}
