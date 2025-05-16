package com.fatepet.petrest.user.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fatepet.global.response.ApiResponse;
import com.fatepet.global.response.ResponseCode;
import com.fatepet.petrest.user.security.dto.CustomUserDetails;
import com.fatepet.petrest.user.security.dto.LoginRequestDTO;
import com.fatepet.petrest.user.security.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000L * 60 * 30;            // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 24 * 7;  // 7일

    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;
    private final JWTUtil jwtUtil;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        LoginRequestDTO loginRequestDTO;
        try {
            loginRequestDTO = objectMapper.readValue(request.getInputStream(), LoginRequestDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequestDTO.username(), loginRequestDTO.password(), null);

        //token에 담은 검증을 위한 AuthenticationManager로 전달
        return authenticationManager.authenticate(authToken);
    }

    //로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        String accessToken = jwtUtil.createJwtToken("access", username, role, ACCESS_TOKEN_EXPIRE_TIME);
        String refreshToken = jwtUtil.createJwtToken("refresh", username, role, REFRESH_TOKEN_EXPIRE_TIME);


        response.addCookie(createCookie("refresh", refreshToken));
        response.setContentType("application/json;charset=UTF-8");
        response.addHeader("Authorization", "Bearer " + accessToken);

        ApiResponse<Void> apiResponse = ApiResponse.of(ResponseCode.SUCCESS);

        response.setStatus(apiResponse.getStatus());
        new ObjectMapper().writeValue(response.getWriter(), apiResponse);
    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setContentType("application/json;charset=UTF-8");

        ApiResponse<Void> apiResponse = ApiResponse.of(ResponseCode.WRONG_USERNAME_PASSWORD);

        response.setStatus(apiResponse.getStatus());
        new ObjectMapper().writeValue(response.getWriter(), apiResponse);
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        cookie.setSecure(true);
        cookie.setPath("/Prod/token");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
