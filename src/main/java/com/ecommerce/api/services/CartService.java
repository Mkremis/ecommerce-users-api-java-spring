package com.ecommerce.api.services;

import com.ecommerce.api.entities.Cart;
import com.ecommerce.api.repositories.CartRepository;
import com.ecommerce.api.dtos.ProductDTO;
import com.ecommerce.api.security.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CartService {
    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public List<Cart> getCartsByUserId(String userId) {
        return cartRepository.findAllByUserId(userId);
    }

    public Set<Cart> updateCart(ProductDTO updatedCart, User user) {
        // Verificar si el cart existe por su prodId
        Optional<Cart> existingCart = cartRepository.findByProdIdAndUserId(updatedCart.getProdId(), user.getId());
        if (existingCart.isPresent()) {
            // Si el cart existe, actualizar sus campos
            Cart cartToUpdate = existingCart.get();
            cartToUpdate.setProductQ(updatedCart.getProductQ());
            Cart updatedCartResult = cartRepository.save(cartToUpdate);
            return user.getCarts();
        } else {
            // Si el cart no existe, crear uno nuevo
            Cart cart = new Cart();
            cart.setProdId(updatedCart.getProdId());
            cart.setProdGender(updatedCart.getProdGender());
            cart.setProdName(updatedCart.getProdName());
            cart.setProdImage(updatedCart.getProdImage());
            cart.setProdPrice(updatedCart.getProdPrice());
            cart.setProductQ(updatedCart.getProductQ());
            cart.setPriceCurrency(updatedCart.getPriceCurrency());
            // Añadir el Cart a la colección de carts del User
            user.getCarts().add(cart);
            // Establecer el user en el Cart
            cart.setUser(user);
            cartRepository.save(cart);
            return user.getCarts();
        }
    }

    public Set<Cart> deleteCartById(Long cartId, User user) {
        Optional<Cart> cartOptional = cartRepository.findById(cartId);
        if (cartOptional.isPresent()) {
            Cart cartToDelete = cartOptional.get();
            user.getCarts().remove(cartToDelete); // Remove the cart from user's carts
            cartRepository.deleteById(cartId);
            return user.getCarts(); // Return updated carts after deletion
        } else {
            throw new RuntimeException("Cart not found with ID: " + cartId);
        }
    }
}
