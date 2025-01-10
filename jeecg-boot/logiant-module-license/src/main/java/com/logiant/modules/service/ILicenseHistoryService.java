package com.logiant.modules.service;

import com.logiant.modules.entity.LicenseHistory;

import java.util.List;

public interface ILicenseHistoryService {
    LicenseHistory save(LicenseHistory history);

    List<LicenseHistory> findAll();

    LicenseHistory findById(Long id);
}
