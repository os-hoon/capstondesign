package cherrysumer.cherrysumer.util.jwt;

import cherrysumer.cherrysumer.exception.ErrorCode;
import cherrysumer.cherrysumer.exception.handler.CertificationHandler;
import cherrysumer.cherrysumer.exception.handler.UserErrorHandler;
import cherrysumer.cherrysumer.service.CustomUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final CustomUserDetailService userDetailService;
    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authoriztionHeader = request.getHeader("Authorization");

        if(authoriztionHeader != null && authoriztionHeader.startsWith("Bearer ")) {
            String token = authoriztionHeader.substring(7);

            if(tokenProvider.isValidToken(token)) {
                String userId = tokenProvider.getUserId(token);
                try {
                    UserDetails userDetails =  userDetailService.loadUserByUsername(userId);

                    if(userDetails != null) {
                        UsernamePasswordAuthenticationToken userToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                        SecurityContextHolder.getContext().setAuthentication(userToken);
                    }
                } catch (Exception e) {
                    throw new UserErrorHandler(ErrorCode._USER_NOT_FOUND);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
