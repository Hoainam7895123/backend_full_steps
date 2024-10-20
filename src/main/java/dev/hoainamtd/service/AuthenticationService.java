package dev.hoainamtd.service;

import dev.hoainamtd.dto.response.TokenResponse;
import dev.hoainamtd.dto.reuqest.SignInRequest;
import dev.hoainamtd.exception.InvalidDataException;
import dev.hoainamtd.model.Token;
import dev.hoainamtd.model.User;
import dev.hoainamtd.repository.UserRepository;
import dev.hoainamtd.util.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenService tokenService;

    public TokenResponse authenticate(SignInRequest signInRequest)  {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getUsername(),signInRequest.getPassword()));

        var user = userRepository.findByUsername(signInRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username or Password is incorrect"));

        String accessToken = jwtService.generateToken(user);

        String refreshToken = jwtService.generateRefreshToken(user);

        // save token to db
        tokenService.save(Token.builder().username(user.getUsername()).accessToken(accessToken).refreshToken(refreshToken).build());

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .build();
    }

    public TokenResponse refresh(HttpServletRequest request) {
        // B1: validate refresh token
        String refreshToken = request.getHeader("x-token");
        if (StringUtils.isBlank(refreshToken)) {
            throw new InvalidDataException("Token must be not blank");
        }

        // B2: extract user from token
        final String userName = jwtService.extractUsername(refreshToken, TokenType.REFRESH_TOKEN); // find username from token
        System.out.println("username: " + userName);

        // B3: check it into database
        Optional<User> user = userRepository.findByUsername(userName);
        // validate 1 lượt nữa, xem token còn hạn không
        System.out.println("userId=" + user.get().getId());

        // nếu token không hợp lệ thì sẽ ném ra exception
        if (!jwtService.isValid(refreshToken, TokenType.REFRESH_TOKEN, user.get())) {
            throw new InvalidDataException("Token is invalid");
        }

        // nếu refresh token hợp lệ thì sẽ generate ra một access token mới
        String accessToken = jwtService.generateToken(user.get());

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.get().getId())
                .build();
    }

    public String logout(HttpServletRequest request) {
        // B1: validate refresh token
        String refreshToken = request.getHeader(HttpHeaders.REFERER);
        if (StringUtils.isBlank(refreshToken)) {
            throw new InvalidDataException("Token must be not blank");
        }
        final String userName = jwtService.extractUsername(refreshToken, TokenType.ACCESS_TOKEN); // find username from token

        // check token in database
        Token currentToken = tokenService.getByUsername(userName);

        tokenService.delete(currentToken.getUsername());
        return "Deleted";
    }
}
