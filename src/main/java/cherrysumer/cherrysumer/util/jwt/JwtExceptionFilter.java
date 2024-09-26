package cherrysumer.cherrysumer.util.jwt;

import cherrysumer.cherrysumer.exception.ErrorCode;
import cherrysumer.cherrysumer.util.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtExceptionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            chain.doFilter(request, response);
        } catch (JwtException ex) {
            String message = ex.getMessage();
            // 토큰 만료
            if(ErrorCode._TOKEN_EXPIRATION.getMessage().equals(message)) {
                setResponse(response, ErrorCode._TOKEN_EXPIRATION);
            } else {
                setResponse(response, ErrorCode._TOKEN_UNAUTHORIZED);
            }
        }
    }

    private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws RuntimeException, IOException {
        ApiResponse<Object> error = ApiResponse.onFailure(errorCode, null);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonErrorResponse = objectMapper.writeValueAsString(error);

        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE); // application/json
        response.setStatus(errorCode.getStatus().value());
        response.getWriter().write(jsonErrorResponse);
    }
}
