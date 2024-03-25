package com.ecommerce.api.security.controllers;


import com.ecommerce.api.entities.Dashboard;
import com.ecommerce.api.entities.Message;
import com.ecommerce.api.security.dtos.LoginResponse;
import com.ecommerce.api.security.entities.User;
import com.ecommerce.api.security.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class SessionController {

    @Value("${jwt.accessTokenCookieName}")
    private String cookieName;

    @Autowired
    CookieUtil cookieUtil;



    // Endpoint para cerrar sesión
    @GetMapping("/logout")
    public ResponseEntity<Message> logOut(HttpServletResponse httpServletResponse) {
        // Limpiar la cookie de autenticación
        CookieUtil.clear(httpServletResponse, cookieName);
        return new ResponseEntity<>(new Message("Sesión cerrada"), HttpStatus.OK);
    }

    // Endpoint para recargar la sesión del usuario
    @GetMapping("/reload")
    public ResponseEntity<Object> reloadSession(HttpServletRequest request, HttpServletResponse response) {
        try {
            // Obtener al usuario desde la cookie en la solicitud
            User user = cookieUtil.getUserFromCookie(request, cookieName);
            if (user == null) {
                return new ResponseEntity<>(new Message("Sesión no válida"), HttpStatus.UNAUTHORIZED);
            } else {
                // Crear un objeto LoginResponse con los datos del usuario y sus detalles
                HashMap<String, String> userData = new HashMap<>();
                userData.put("username", user.getUserName());
                userData.put("email", user.getEmail());
                Dashboard dashboard = user.getDashboard();
                if (dashboard != null) {
                    userData.put("thumbnail", user.getDashboard().getThumbnail());
                }
                LoginResponse loginResponse = new LoginResponse(new Message("Sesión persistente"), userData, user.getCarts(), user.getLikes());

                // Retornar una respuesta con el objeto LoginResponse y el estado OK
                return new ResponseEntity<>(loginResponse, HttpStatus.OK);
            }
        } catch (Exception e) {
            // En caso de error, retornar un mensaje de error y el estado BAD REQUEST
            return new ResponseEntity<>(new Message("Error al verificar la sesión:" + e.getMessage() ), HttpStatus.BAD_REQUEST);
        }
    }

}
