package com.fatepet.petrest.user.security;

import com.fatepet.petrest.global.response.ApiResponse;
import com.fatepet.petrest.global.response.ResponseCode;
import com.fatepet.petrest.user.security.util.JWTUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReissueController {

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000L * 60 * 30;

    private final JWTUtil jwtUtil;

    @PostMapping("/token")
    public ResponseEntity<ApiResponse<Void>> reissueToken(HttpServletRequest request, HttpServletResponse response) {

        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }

        if (refresh == null || !jwtUtil.getCategory(refresh).equals("refresh")) {
            ResponseCode code = ResponseCode.WRONG_TOKEN;
            return ResponseEntity.status(code.getStatusCode()).body(ApiResponse.of(code));
        }

        if (jwtUtil.isExpired(refresh)) {
            ResponseCode code = ResponseCode.TOKEN_EXPIRED;
            return ResponseEntity.status(code.getStatusCode()).body(ApiResponse.of(code));
        }

        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        String newToken = jwtUtil.createJwtToken("access", username, role, ACCESS_TOKEN_EXPIRE_TIME);

        response.setHeader("Authorization", "Bearer " + newToken);

        return ResponseEntity.ok(ApiResponse.of(ResponseCode.SUCCESS));
    }
}
