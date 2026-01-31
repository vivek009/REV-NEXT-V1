package com.revnext.controller.login;

import com.revnext.constants.JWTConstants;
import com.revnext.controller.BaseController;
import com.revnext.controller.RequestAndResponseConstant;
import com.revnext.controller.URLConstants;
import com.revnext.controller.login.request.LoginRequest;
import com.revnext.controller.login.response.LoginResponse;
import com.revnext.domain.user.User;
import com.revnext.service.jwt.JwtService;
import com.revnext.service.user.UserService;
import com.revnext.service.user.exception.UserNotAuthorizedException;
import com.revnext.util.DateAndTimeUtil;
import com.revnext.util.HttpUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CommonsLog
@Validated
public class LoginController extends BaseController {

        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        private JwtService jwtService;

        @Autowired
        private UserService userService;

        @Value("${token.duration.days}")
        private long durationDays;

        @Value("${token.duration.hours}")
        private long durationHours;

        @Value("${token.duration.minutes}")
        private long durationMinutes;

        @PostMapping(value = URLConstants.AUTHENTICATE, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<LoginResponse> authenticate(HttpServletRequest request,
                        @RequestBody LoginRequest loginRequest) throws UserNotAuthorizedException {
                User user = userService.getUserForLogin(loginRequest.getUserName(), loginRequest.getPassword());
                Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(user.getUserId(), user.getPassword()));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String token = jwtService
                                .createCustomToken(
                                                user,
                                                Map.of(
                                                                JWTConstants.USER_ID.getValue(),
                                                                user.getUserId().toString(),
                                                                JWTConstants.ROLES.getValue(),
                                                                user.getRolesAsValue(),
                                                                JWTConstants.CLIENT_IP.getValue(),
                                                                HttpUtils.getClientIPs(request),
                                                                JWTConstants.IAT.getValue(),
                                                                DateAndTimeUtil.addSecondsToInstant(0).getEpochSecond(),
                                                                JWTConstants.EXP.getValue(),
                                                                DateAndTimeUtil.addSecondsToInstant(durationDays,
                                                                                durationHours, durationMinutes)
                                                                                .getEpochSecond()));

                return getResponse(() -> LoginResponse
                                .builder()
                                .userName(user.getUserName())
                                .name(user.getName())
                                .token(token)
                                .refreshToken(user.getRefreshToken())
                                .email(user.getEmail())
                                .mobileNumber(user.getMobileNumber())
                                .profilePictureUrl(user.getProfilePictureUrl())
                                .roles(user.getRoles().stream()
                                                .map(role -> role.getName().getValue()) // or .map(role ->
                                                                                        // role.getName()) if Role has a
                                                                                        // getName() method
                                                .collect(Collectors.toSet()))
                                .divisionName(user.getDivisionName())
                                .shopName(user.getShopName())
                                .build());
        }

        @PostMapping(value = URLConstants.REFRESH_TOKEN, produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<LoginResponse> refreshToken(HttpServletRequest request,
                        @RequestParam(RequestAndResponseConstant.REFRESH_TOKEN) String refreshToken)
                        throws UserNotAuthorizedException {
                User user = userService.verifyExpirationAndGetUser(refreshToken);
                String token = jwtService
                                .createCustomToken(
                                                user,
                                                Map.of(
                                                                JWTConstants.USER_ID.getValue(),
                                                                user.getUserId().toString(),
                                                                JWTConstants.CLIENT_IP.getValue(),
                                                                JWTConstants.ROLES.getValue(),
                                                                user.getRolesAsString(),
                                                                HttpUtils.getClientIPs(request),
                                                                JWTConstants.IAT.getValue(),
                                                                DateAndTimeUtil.addSecondsToInstant(0).getEpochSecond(),
                                                                JWTConstants.EXP.getValue(),
                                                                DateAndTimeUtil.addSecondsToInstant(durationDays,
                                                                                durationHours, durationMinutes)
                                                                                .getEpochSecond()));

                return getResponse(() -> LoginResponse
                                .builder()
                                .name(user.getUserName())
                                .token(token)
                                .refreshToken(user.getRefreshToken())
                                .email(user.getEmail())
                                .mobileNumber(user.getMobileNumber())
                                .profilePictureUrl(user.getProfilePictureUrl())
                                .divisionName(user.getDivisionName())
                                .build());
        }
}
