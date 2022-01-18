package com.hmg.organizationservice.controllers;

import com.hmg.organizationservice.model.Organization;
import com.hmg.organizationservice.services.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "v1/organizations")
public class OrganizationServiceController {

    @Autowired
    private OrganizationService orgService;

    @GetMapping(value = "/{organizationId}")
    public Organization getOrganization(@PathVariable(value = "organizationId") String organizationId) {
        return orgService.getOrg(organizationId);
    }
}
