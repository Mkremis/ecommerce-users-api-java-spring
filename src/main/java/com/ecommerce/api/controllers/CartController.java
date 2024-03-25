package com.ecommerce.api.controllers;

import com.ecommerce.api.dtos.ProductDTO;
import com.ecommerce.api.entities.Cart;
import com.ecommerce.api.security.entities.User;
import com.ecommerce.api.security.util.CookieUtil;
import com.ecommerce.api.services.CartService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class CartController {
    @Autowired
    CartService cartService;
    @Autowired
    CookieUtil cookieUtil;

    @Value("${jwt.accessTokenCookieName}")
    private String cookieName;

    @GetMapping("/cart")
    public ResponseEntity<?> getCart(HttpServletRequest request) {
        try {
            String userId = cookieUtil.getUserIdFromCookie(request, cookieName);
            List<Cart> carts = cartService.getCartsByUserId(userId);
            if (!carts.isEmpty()) {
                return ResponseEntity.ok(carts);
            } else {
                return ResponseEntity.ok(Collections.emptyList());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la solicitud");
        }
    }

    @PutMapping("/cart")
    public ResponseEntity<Set<Cart>> updateCartItem(@RequestBody ProductDTO updatedCart, HttpServletRequest request) {
        try {
            User user = cookieUtil.getUserFromCookie(request, cookieName);
            if (user != null) {
                // Actualiza el cart y obtén el cart actualizado
               Set<Cart>updatedUserCart = cartService.updateCart(updatedCart, user);
                // Retorna el cart completo actualizado
                return ResponseEntity.ok(updatedUserCart);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Aquí se puede enviar un cuerpo vacío o un mensaje según lo que necesites
        }
    }

    @DeleteMapping("/cart/{cartId}")
    public ResponseEntity<Set<Cart>> deleteCart(@PathVariable Long cartId, HttpServletRequest request) {
        try {
            User user = cookieUtil.getUserFromCookie(request, cookieName);
            if (user != null) {
                // Elimina el cart y obtén los carts actualizados del usuario
                Set<Cart>updatedUserCart = cartService.deleteCartById(cartId, user);
                // Retorna los carts actualizados del usuario
                return ResponseEntity.ok(updatedUserCart);
            } else {
                return ResponseEntity.notFound().build();
            }
        }    catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Aquí se puede enviar un cuerpo vacío o un mensaje según lo que necesites
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Aquí se puede enviar un cuerpo vacío o un mensaje según lo que necesites
    }
    }
}
