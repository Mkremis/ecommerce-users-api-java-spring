package com.ecommerce.api.security.controllers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.ecommerce.api.security.services.UserService;
import com.ecommerce.api.entities.Dashboard;
import com.ecommerce.api.entities.Message;
import com.ecommerce.api.security.dtos.LoginUser;
import com.ecommerce.api.security.dtos.NewUser;
import com.ecommerce.api.security.dtos.LoginResponse;
import com.ecommerce.api.security.entities.Role;
import com.ecommerce.api.security.entities.User;
import com.ecommerce.api.security.enums.RoleList;
import com.ecommerce.api.security.jwt.JwtProvider;
import com.ecommerce.api.security.services.RoleService;
import com.ecommerce.api.security.util.CookieUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final RoleService roleService;
    private final JwtProvider jwtProvider;
    private final LoginResponse loginResponse;
    private final CookieUtil cookieUtil;

    @Value("${jwt.accessTokenCookieName}")
    private String cookieName;

    // Constructor para la inyección de dependencias
    public AuthController(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder,
                          UserService userService, RoleService roleService, JwtProvider jwtProvider,
                          LoginResponse loginResponse, CookieUtil cookieUtil) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.roleService = roleService;
        this.jwtProvider = jwtProvider;
        this.loginResponse = loginResponse;
        this.cookieUtil = cookieUtil;
    }

    // Endpoint para iniciar sesión
    @PostMapping("/login")
    public ResponseEntity<Object> login(HttpServletResponse httpServletResponse,
                                        @Valid @RequestBody LoginUser loginUser, BindingResult bidBindingResult) {
        // Verifica si hay errores de validación en el objeto LoginUser
        if (bidBindingResult.hasErrors()) {
            return new ResponseEntity<>(new Message("Revise sus credenciales"), HttpStatus.BAD_REQUEST);
        }
        try {
            // Autenticar al usuario con el AuthenticationManager
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginUser.getUserName(), loginUser.getPassword())
            );
            // Establecer la autenticación en el contexto de seguridad
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // Generar un token JWT
            String jwt = jwtProvider.generateToken(authentication);
            // Crear una cookie con el token JWT
            CookieUtil.create(httpServletResponse, cookieName, jwt, false, -1, "localhost");

            // Obtener el usuario y el carrito asociado al usuario que ha iniciado sesión
            Optional<User> optionalUser = userService.getByUserName(loginUser.getUserName());
            User user = optionalUser.orElse(null); // Obtener el usuario o null si no está presente

            // Crear el objeto LoginResponse con el mensaje y los datos del usuario y su carrito
            HashMap<String, String> userData = new HashMap<>();
            userData.put("userName", user.getUserName());
            userData.put("email", user.getEmail());
            Dashboard dashboard = user.getDashboard();
            if (dashboard != null) {
                userData.put("thumbnail", user.getDashboard().getThumbnail());
            }

            LoginResponse loginResponse = new LoginResponse(new Message("Sesión iniciada"), userData, user.getCarts(), user.getLikes());

            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        } catch (Exception e) {
            // Manejo de excepciones y retorno de mensaje de error
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Endpoint para registrar un nuevo usuario
    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody NewUser newUser, BindingResult bindingResult) {
        // Verifica si hay errores de validación en el objeto NewUser
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(new Message("Revise los campos e intente nuevamente"), HttpStatus.BAD_REQUEST);
        }

        // Crear un nuevo usuario con los datos proporcionados
        User user = new User(newUser.getUserName(), newUser.getEmail(),
                passwordEncoder.encode(newUser.getPassword()));
        Set<Role> roles = new HashSet<>();
        roles.add(roleService.getByRoleName(RoleList.ROLE_USER).get());
        if (newUser.getRoles().contains("admin")) {
            roles.add(roleService.getByRoleName(RoleList.ROLE_ADMIN).get());
        }
        user.setRoles(roles);

        // Guardar el usuario en la base de datos
        userService.save(user);

        // Retornar un mensaje de éxito
        return new ResponseEntity<>(new Message("Registro exitoso! Inicie sesión"), HttpStatus.CREATED);
    }

    // Endpoint para obtener detalles del usuario actual
    @GetMapping("/details")
    public ResponseEntity<Object> getUserDetails() {
        // Obtener los detalles del usuario actualmente autenticado
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = userDetails.getUsername();
        Optional<User> user = this.userService.getByUserName(userName);
        if (!user.isPresent()) {
            return new ResponseEntity<>(new Message("No encontrado"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user.get(), HttpStatus.OK);
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

    // Endpoint para cerrar sesión
    @GetMapping("/logout")
    public ResponseEntity<Message> logOut(HttpServletResponse httpServletResponse) {
        // Limpiar la cookie de autenticación
        CookieUtil.clear(httpServletResponse, cookieName);
        return new ResponseEntity<>(new Message("Sesión cerrada"), HttpStatus.OK);
    }

}
