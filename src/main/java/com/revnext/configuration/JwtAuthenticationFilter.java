package com.revnext.configuration;

import com.revnext.constants.JWTConstants;
import com.revnext.constants.Roles;
import com.revnext.service.jwt.JwtService;
import com.revnext.util.DateAndTimeUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.security.cert.CertificateException;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String TOKEN_HEADER = "authorization";

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) {
        logger.info("doFilter:: authenticating...");
        logger.info("Execute method asynchronously. " + Thread.currentThread().getName());
        String authToken = request.getHeader(TOKEN_HEADER);
        String userId;
        String role = null;
        String divisionName = null;
        String circleName = null;
        String shopName = null;

        try {
            if (StringUtils.isEmpty(authToken)) {
                userId = request.getHeader("USER");
            } else {
                Map<String, Object> claims = jwtService.verifyAndGetClaims(authToken);
                userId = claims.get(JWTConstants.USER_ID.getValue()).toString().replace("\"", "");
                Object divisionNameObject = claims.get(JWTConstants.DIVISION_NAME.getValue());
                divisionName = divisionNameObject == null ? "" : divisionNameObject.toString();
                Object circleNameObject = claims.get(JWTConstants.CIRCLE_NAME.getValue());
                circleName = circleNameObject == null ? "" : circleNameObject.toString();
                Object shopNameObject = claims.get(JWTConstants.SHOP_NAME.getValue());
                shopName = shopNameObject == null ? "" : shopNameObject.toString();
                role = claims.get(JWTConstants.ROLES.getValue()).toString();
                String expiredAt = claims.get(JWTConstants.EXP.getValue()).toString();
                if (DateAndTimeUtil.checkTokenExpiration(Long.parseLong(expiredAt))) {
                    // Token has expired
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    return;
                }
                UserDetails userDetails = jwtService.loadUserByUsername(userId);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.debug("doFilter():: successfully authenticated.");
            }

            if (userId != null) {
                request.setAttribute(JWTConstants.USER_ID.getValue(), UUID.fromString(userId));
            }

            if (StringUtils.isNotEmpty(role)) {
                try {
                    request.setAttribute(JWTConstants.ROLES.getValue(), Roles.fromValue(role));
                } catch (IllegalArgumentException e) {
                    logger.warn("Unknown role: " + role);
                }
            }
            if (divisionName != null) {
                request.setAttribute(JWTConstants.DIVISION_NAME.getValue(), divisionName);
            }
            if (circleName != null) {
                request.setAttribute(JWTConstants.CIRCLE_NAME.getValue(), circleName);
            }
            if (shopName != null) {
                request.setAttribute(JWTConstants.SHOP_NAME.getValue(), shopName);
            }
            filterChain.doFilter(request, response);
        } catch (CertificateException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            logger.error("Fail to authenticate.", ex);
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error(ex.getMessage(), ex);
        }
    }
}