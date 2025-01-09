package org.jeecg.modules.service;

import org.jeecg.modules.entity.LicenseHistory;
import org.jeecg.modules.repository.LicenseHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LicenseHistoryService implements ILicenseHistoryService {

    @Autowired
    private LicenseHistoryRepository repository;

    @Override
    public LicenseHistory save(LicenseHistory history) {
        return repository.save(history);
    }

    @Override
    public List<LicenseHistory> findAll() {
        return repository.findAll();
    }

    @Override
    public LicenseHistory findById(Long id) {
        return repository.findById(id).orElse(null);
    }
}
