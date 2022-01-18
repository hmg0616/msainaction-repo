package com.hmg.organizationservice.services;

import com.hmg.organizationservice.model.Organization;
import com.hmg.organizationservice.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class OrganizationService {
    @Autowired
    private OrganizationRepository orgRepository;

    public Organization getOrg(String organizationId) {
 
    	Optional<Organization> organization = orgRepository.findById(organizationId);
    	if (!organization.isPresent())
    		throw new NullPointerException("organizationId-" + organizationId);
    	return organization.get();
    }
}
