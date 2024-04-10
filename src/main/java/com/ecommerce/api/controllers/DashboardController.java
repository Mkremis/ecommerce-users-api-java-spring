package com.ecommerce.api.controllers;


import java.util.HashMap;
import java.util.List;
import java.util.Optional;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.ecommerce.api.dtos.DashboardDTO;
import com.ecommerce.api.entities.Dashboard;
import com.ecommerce.api.security.entities.User;
import com.ecommerce.api.security.util.CookieUtil;
import com.ecommerce.api.services.DashboardsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class DashboardController {
    @Autowired
    DashboardsService dashboardsService;

    @Autowired
    CookieUtil cookieUtil;

    @Value("${jwt.accessTokenCookieName}")
    private String cookieName;

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard(HttpServletRequest request) {
        try {
            String userId = cookieUtil.getUserIdFromCookie(request, cookieName);
            Optional<Dashboard> optionalDashboard = dashboardsService.getDashboardByUserId(userId);
            if (optionalDashboard.isPresent()) {
                return ResponseEntity.ok(optionalDashboard.get());
            } else {
                HashMap emptyDashboard = new HashMap<>();
                return ResponseEntity.ok(emptyDashboard);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la solicitud");
        }
    }

    @PatchMapping("/dashboard")
    public ResponseEntity<?> postDashboard(@RequestBody DashboardDTO dashboardDTO, HttpServletRequest request) {
        try {
            User user = cookieUtil.getUserFromCookie(request, cookieName);
            if (user != null) {
                dashboardsService.updateDashboard(dashboardDTO, user);

                return ResponseEntity.ok(dashboardDTO);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la solicitud" );
        }
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<List<Dashboard>> save(@Valid @RequestBody Dashboard dashboard, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<Dashboard> savedDashboards = this.dashboardsService.getAllDashboards();
        return new ResponseEntity<>(savedDashboards, HttpStatus.OK);
    }

}
