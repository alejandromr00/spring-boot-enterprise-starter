package com.nexora.ai.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class SecurityTestController {

    // Endpoint público
    @GetMapping("/public")
    public String publicEndpoint() {
        return "Public endpoint";
    }

    // Cualquier usuario autenticado
    @GetMapping("/authenticated")
    public Map<String, Object> authenticated(Authentication authentication) {
        return Map.of(
                "username", authentication.getName(),
                "authorities", authentication.getAuthorities()
        );
    }

    // Solo ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String admin() {
        return "Welcome ADMIN";
    }

    // ADMIN o SUPERVISOR
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    @GetMapping("/supervisor")
    public String supervisor() {
        return "Welcome SUPERVISOR";
    }

    // Permiso INVENTORY_READ
    @PreAuthorize("hasAuthority('INVENTORY_READ')")
    @GetMapping("/inventory")
    public String inventory() {
        return "Inventory access granted";
    }

    // Permiso INVENTORY_CREATE
    @PreAuthorize("hasAuthority('INVENTORY_CREATE')")
    @PostMapping("/inventory")
    public String createInventory() {
        return "Inventory created";
    }

    // Permiso REQUISITION_APPROVE
    @PreAuthorize("hasAuthority('REQUISITION_APPROVE')")
    @PostMapping("/requisition/approve")
    public String approveRequisition() {
        return "Requisition approved";
    }
}