package com.hmg.licensingservice.repository;

import com.hmg.licensingservice.model.License;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LicenseRepository extends CrudRepository<License,String> {
    public License findByOrganizationIdAndLicenseId(String organizationId,String licenseId);
}
