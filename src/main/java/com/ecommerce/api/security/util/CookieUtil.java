package com.ecommerce.api.security.util;

import com.ecommerce.api.security.services.UserService;
import com.ecommerce.api.security.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ecommerce.api.security.jwt.JwtProvider;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Component
public class CookieUtil {
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    UserService userService;

    public static void create(HttpServletResponse httpServletResponse, String name, String value, Boolean secure, Integer maxAge, String domain){
        Cookie cookie = new Cookie(name, value);
        cookie.setSecure(secure);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        cookie.setDomain(domain);
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);
    }
    public static void clear(HttpServletResponse httpServletResponse, String name){
        Cookie cookie = new Cookie(name, null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(1);
        cookie.setDomain("localhost");
        httpServletResponse.addCookie(cookie);
    }
    public String getUserIdFromCookie(HttpServletRequest request, String cookieName){
        String token = getValueFromCookie(request, cookieName);
        if (token == null) {
            return null;
        }
        if (jwtProvider.validateToken(token)) {
            String userNameFromToken = jwtProvider.getUserNameFromToken(token);
            Optional<User> optionalUser = userService.getByUserName(userNameFromToken);
            return optionalUser.get().getId();
        }else {return null;}
    }
    public User getUserFromCookie(HttpServletRequest request, String cookieName){
        String token = getValueFromCookie(request, cookieName);
        if (token == null) {
            return null;
        }
        if (jwtProvider.validateToken(token)) {
        String userNameFromToken = jwtProvider.getUserNameFromToken(token);
        Optional<User> optionalUser = userService.getByUserName(userNameFromToken);
        return optionalUser.get();
        }else {
            return null;
        }
    }
    private String getValueFromCookie(HttpServletRequest request, String cookieName){
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        String token = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                token = cookie.getValue();
                break;
            }
        }
        return token;
    }
}
