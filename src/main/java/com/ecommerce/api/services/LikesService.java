package com.ecommerce.api.services;

import com.ecommerce.api.repositories.LikesRepository;
import com.ecommerce.api.entities.Likes;
import com.ecommerce.api.security.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LikesService {
    private final LikesRepository likesRepository;

    public LikesService(LikesRepository likesRepository){
        this.likesRepository = likesRepository;
    }

    public List<Likes> getLikesByUserId(String userId) {
        return likesRepository.findAllByUserId(userId);
    }

    public void createLike(Likes likes) {
        likesRepository.save(likes);
    }
    public void deleteLikeByProdIdAndUser(String prodId, User user){
        Optional<Likes> existingLike = likesRepository.findByProdIdAndUserId(prodId, user.getId());
        if(existingLike.isPresent()){
            likesRepository.delete(existingLike.get());
        }
    }
}
