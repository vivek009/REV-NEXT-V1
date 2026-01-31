package com.revnext.service.user;

import com.revnext.domain.user.Resource;
import com.revnext.domain.user.User;
import com.revnext.repository.user.ResourceRepository;
import com.revnext.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    public boolean hasPermission(String username, String resource, String action) {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return user.getRoles().stream()
                .flatMap(role -> role.getResources().stream())
                .anyMatch(permission -> permission.getName().toLowerCase().equals(resource)
                        && permission.getMethod().toLowerCase().equals(action));
    }

    public Resource getResourceByPathAndMethod(String path, String method) {
        try {
            Long resourceId = Long.parseLong(path);
            return resourceRepository.findById(resourceId).orElse(null);
        } catch (NumberFormatException e) {
            return resourceRepository.findByNameAndMethod(path, method);
        }
    }
}
