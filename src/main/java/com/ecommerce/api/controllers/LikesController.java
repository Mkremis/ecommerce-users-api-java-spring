package com.ecommerce.api.controllers;


import com.ecommerce.api.dtos.LikesDTO;
import com.ecommerce.api.entities.Likes;
import com.ecommerce.api.security.entities.User;
import com.ecommerce.api.security.util.CookieUtil;
import com.ecommerce.api.services.LikesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class LikesController {
    @Autowired
    LikesService likesService;
    @Autowired
    CookieUtil cookieUtil;

    @Value("${jwt.accessTokenCookieName}")
    private String cookieName;


    @GetMapping("/likes")
    public ResponseEntity<?> getLikes(HttpServletRequest request) {
        try {
            String userId = cookieUtil.getUserIdFromCookie(request, cookieName);
            List<Likes> likes = likesService.getLikesByUserId(userId);
            if (!likes.isEmpty()) {
                return ResponseEntity.ok(likes);
            } else {
                return ResponseEntity.ok(Collections.emptyList());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la solicitud");
        }
    }

    @PostMapping("/likes")
    public ResponseEntity<?> postLikes(@RequestBody LikesDTO likesDTO, HttpServletRequest request) {
        try {
            User user = cookieUtil.getUserFromCookie(request, cookieName);
            if (user != null) {
                Likes likes = new Likes();
                likes.setProdId(likesDTO.getProdId());
                likes.setProdName(likesDTO.getProdName());
                likes.setProdGender(likesDTO.getProdGender());
                likes.setProdImage(likesDTO.getProdImage());
                likes.setProdPrice(likesDTO.getProdPrice());
                likes.setPriceCurrency(likesDTO.getPriceCurrency());

                // Añadir el like a la colección de likes del User
                user.getLikes().add(likes);

                // Establecer el user en likes
                likes.setUser(user);

                // Guardar el User actualizado, esto debería persistir el Likes con el user_id asignado
                likesService.createLike(likes);

                return ResponseEntity.ok(likes);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la solicitud");
        }
    }

    @DeleteMapping("/likes/{prodId}")
    public ResponseEntity<String> deleteLike(@PathVariable String prodId, HttpServletRequest request) {
        try {
            User user = cookieUtil.getUserFromCookie(request, cookieName);
            if (user != null) {
            likesService.deleteLikeByProdIdAndUser(prodId, user);
            }
            return ResponseEntity.ok("Like with ID " + prodId + " deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting like with ID " + prodId);
        }
    }
}
