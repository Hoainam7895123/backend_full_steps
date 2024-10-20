package dev.hoainamtd.configuration;

import dev.hoainamtd.service.JwtService;
import dev.hoainamtd.service.UserService;
import dev.hoainamtd.util.TokenType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// PreFilter sẽ đứng trước tất cả câc api
@Component
@Slf4j
@RequiredArgsConstructor
public class PreFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final JwtService jwtService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("---------------- PreFilter ----------------");

        // lấy token của người dùng gửi kèm khi request
        final String authorization = request.getHeader("Authorization");
        log.info("Authorization: {}", authorization);

        // kiểm tra tính đúng đắn của token
        // nếu k có token, hoặc token k bắt đầu bằng bearer thì return luôn, không thực hiện gì cả
        if (StringUtils.isBlank(authorization) || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // cắt bearer trong token của người dùng
        final String token = authorization.substring("Bearer ".length());
        log.info("Token: {}", token);

        // doc token và xác định ra được user
        final String userName = jwtService.extractUsername(token, TokenType.ACCESS_TOKEN);

        // kiểm tra username khác null và người dùng đã được xác thực chưa
        if (StringUtils.isNotEmpty(userName) && SecurityContextHolder.getContext().getAuthentication() == null ) {
            UserDetails userDetails = userService.userDetailsService().loadUserByUsername(userName);
            if (jwtService.isValid(token, TokenType.ACCESS_TOKEN, userDetails)) {
                // nếu token hợp lệ thì tiếp tục kiểm tra
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authentication);
                SecurityContextHolder.setContext(context);
            }
        }

        filterChain.doFilter(request, response);

    }
}
