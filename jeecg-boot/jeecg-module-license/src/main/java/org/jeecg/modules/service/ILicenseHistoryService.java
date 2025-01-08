package org.jeecg.modules.service;

import org.jeecg.modules.entity.LicenseHistory;

import java.util.List;

public interface ILicenseHistoryService {
    LicenseHistory save(LicenseHistory history);

    List<LicenseHistory> findAll();

    LicenseHistory findById(Long id);
}
