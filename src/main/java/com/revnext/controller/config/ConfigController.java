package com.revnext.controller.config;

import com.revnext.controller.BaseController;
import com.revnext.controller.config.request.ConfigRequest;
import com.revnext.controller.config.response.ConfigResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/config")
public class ConfigController extends BaseController {

    @GetMapping
    public ResponseEntity<List<ConfigResponse>> listAllConfigs() {
        return null;
    }

    @GetMapping("/{configName}")
    public ResponseEntity<ConfigResponse> getConfigByName(@PathVariable String configName) {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConfigResponse> getConfig(@PathVariable UUID id) {
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateConfig(@PathVariable UUID id, @RequestBody ConfigRequest request) {
        return null;
    }

    @PostMapping
    public ResponseEntity<String> createConfig(@RequestBody ConfigRequest request) {
        return null;
    }

    @DeleteMapping
    public ResponseEntity<String> removeConfig(@RequestParam String id) {
        return null;
    }

}
