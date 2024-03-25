package com.ecommerce.api.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.ecommerce.api.repositories.DashboardsRepository;
import com.ecommerce.api.security.services.UserService;
import com.ecommerce.api.dtos.DashboardDTO;
import com.ecommerce.api.entities.Dashboard;

import com.ecommerce.api.security.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class DashboardsService {
    @Autowired
    DashboardsRepository dashboardsRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserService userService;


    public Optional<Dashboard> getDashboardByUserId(String userId) {
       Optional<Dashboard> dashboard = dashboardsRepository.findByUserId(userId);
       return dashboard;
    }

    public void updateDashboard(DashboardDTO dashboardDTO, User user){
        // Verificar si el dashboard existe para el user
        Optional<Dashboard> existingDashboard = dashboardsRepository.findByUserId(user.getId());
        if(existingDashboard.isPresent()){
            Dashboard dashboardToUpdate = existingDashboard.get();
            dashboardToUpdate.setTitle(dashboardDTO.getTitle());
            dashboardToUpdate.setFirst(dashboardDTO.getFirst());
            dashboardToUpdate.setLast(dashboardDTO.getLast());
            dashboardToUpdate.setEmail(dashboardDTO.getEmail());
            dashboardToUpdate.setPhone(dashboardDTO.getPhone());
            dashboardToUpdate.setThumbnail(dashboardDTO.getThumbnail());
            dashboardToUpdate.setCity(dashboardDTO.getCity());
            dashboardToUpdate.setState(dashboardDTO.getState());
            dashboardToUpdate.setStreetNumber(dashboardDTO.getStreetNumber());
            dashboardToUpdate.setStreet(dashboardDTO.getStreet());
            dashboardToUpdate.setCountry(dashboardDTO.getCountry());
            dashboardToUpdate.setPostcode(dashboardDTO.getPostcode());

            String newPassword = dashboardDTO.getPassword();
            if (newPassword != null) {
                String encodedPassword = passwordEncoder.encode(newPassword);
                // Actualizar la contraseña en el objeto User
                user.setPassword(encodedPassword);
                userService.save(user);

                // Actualizar la contraseña en el objeto Dashboard también
                dashboardToUpdate.getUser().setPassword(encodedPassword);
            }
            dashboardsRepository.save(dashboardToUpdate);
        }else {
            // Si el dashboard no existe, crear uno nuevo
            Dashboard newDashboard = new Dashboard();
            newDashboard.setTitle(dashboardDTO.getTitle());
            newDashboard.setFirst(dashboardDTO.getFirst());
            newDashboard.setLast(dashboardDTO.getLast());
            newDashboard.setEmail(dashboardDTO.getEmail());
            newDashboard.setPhone(dashboardDTO.getPhone());
            newDashboard.setThumbnail(dashboardDTO.getThumbnail());
            newDashboard.setCity(dashboardDTO.getCity());
            newDashboard.setState(dashboardDTO.getState());
            newDashboard.setStreetNumber(dashboardDTO.getStreetNumber());
            newDashboard.setStreet(dashboardDTO.getStreet());
            newDashboard.setCountry(dashboardDTO.getCountry());
            newDashboard.setPostcode(dashboardDTO.getPostcode());
            newDashboard.setUser(user);
            dashboardsRepository.save(newDashboard);
        }
    }
    public void createDashboard(Dashboard userDashboard){
        dashboardsRepository.save(userDashboard);
    }
    public List<Dashboard> getAllDashboards(){return  this.dashboardsRepository.findAll();}
}
