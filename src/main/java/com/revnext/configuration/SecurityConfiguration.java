package com.revnext.configuration;

import com.revnext.configuration.security.JWTAuthenticationEntryPoint;
import com.revnext.domain.user.Resource;
import com.revnext.service.jwt.JwtService;
import com.revnext.service.user.AuthorizationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private JWTAuthenticationEntryPoint entryPoint;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private RequestUriMatcher requestUriMatcher;

    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(jwtService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource())).csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(entryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/authenticate", "/refresh_token", "/actuator/mapping", "/error",
                                "/custom-mappings", "/**")
                        .permitAll()
                        .anyRequest()
                        .access(this::dynamicAuthorization));

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Add multiple allowed origins
        List<String> origins = Arrays.asList(allowedOrigins.split(","));
        configuration.setAllowedOrigins(origins);

        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true); // Allow cookies/auth headers

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter(corsConfigurationSource());
    }

    public AuthorizationDecision dynamicAuthorization(Supplier<Authentication> authenticationSupplier,
            RequestAuthorizationContext requestAuthorizationContext) {
        // Extract authentication details
        Authentication authentication = authenticationSupplier.get();
        if (authentication == null || !authentication.isAuthenticated()) {
            return new AuthorizationDecision(false);
        }

        String username = authentication.getName();

        // Extract resource information from the request
        HttpServletRequest request = requestAuthorizationContext.getRequest();
        String httpMethod = request.getMethod();
        Optional<String> resourcePath = requestUriMatcher.getPattern(requestAuthorizationContext);
        if (resourcePath.isEmpty()) {
            return new AuthorizationDecision(false); // Resource not found
        }

        // Fetch the resource using the extracted path
        Resource resource = authorizationService.getResourceByPathAndMethod(resourcePath.get().toLowerCase(),
                httpMethod.toLowerCase());
        if (resource == null) {
            return new AuthorizationDecision(false); // Resource not found
        }

        // Check if the user is authorized
        boolean hasPermission = authorizationService.hasPermission(username, resource.getName().toLowerCase(),
                httpMethod.toLowerCase());

        // Return the final decision
        return new AuthorizationDecision(hasPermission);
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/api-docs/**", "/swagger-ui/**", "/actuator/**", "/error",
                "/custom-mappings", "/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Encrypt passwords
    }

}