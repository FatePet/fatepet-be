package com.fatepet.petrest.user.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fatepet.global.response.ApiResponse;
import com.fatepet.global.response.ResponseCode;
import com.fatepet.petrest.user.Role;
import com.fatepet.petrest.user.security.dto.CustomUserDetails;
import com.fatepet.petrest.user.security.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import com.fatepet.petrest.user.User;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization= request.getHeader("Authorization");

        if (authorization == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!authorization.startsWith("Bearer ")) {
            responseProc(response, ResponseCode.UNAUTHORIZED);
            return;
        }

        String token = authorization.split(" ")[1];

        if (!jwtUtil.getCategory(token).equals("access")) {
            responseProc(response, ResponseCode.WRONG_TOKEN);
            return;
        }


        if (jwtUtil.isExpired(token)) {
            responseProc(response, ResponseCode.TOKEN_EXPIRED);
            return;
        }

        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        User user = User.builder()
                .username(username)
                .password("tempPassword")
                .role(Role.valueOf(role))
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }

    private static void responseProc(HttpServletResponse response, ResponseCode code) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        ApiResponse<Void> apiResponse = ApiResponse.of(code);

        response.setStatus(apiResponse.getStatus());
        new ObjectMapper().writeValue(response.getWriter(), apiResponse);
    }
}
