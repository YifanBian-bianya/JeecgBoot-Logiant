package org.jeecg.modules.controller;

import org.jeecg.modules.entity.LicenseHistory;
import org.jeecg.modules.service.LicenseHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/license/history")
public class LicenseHistoryController {

    @Autowired
    private LicenseHistoryService service;

    @GetMapping("/")
    public String homePage() {
        return "history";
    }


    @GetMapping("/list")
    public List<LicenseHistory> getHistoryList() {
        return service.findAll();
    }

    @GetMapping("/detail/{id}")
    public LicenseHistory getHistoryDetail(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping("/add")
    public LicenseHistory addHistory(@RequestBody LicenseHistory history) {
        return service.save(history);
    }
}
