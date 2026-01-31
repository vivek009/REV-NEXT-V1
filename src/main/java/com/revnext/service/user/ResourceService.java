package com.revnext.service.user;

import com.revnext.domain.user.Resource;
import com.revnext.repository.user.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;

    public Resource getResourceById(Long id) {
        return resourceRepository.findById(id).orElse(null);
    }

    public Resource createResource(Resource resource) {
        return resourceRepository.save(resource);
    }

    public Resource updateResource(Long id, Resource updatedResource) {
        Resource existingResource = getResourceById(id);
        if (existingResource != null) {
            return resourceRepository.save(existingResource);
        }
        return null;
    }

    public void deleteResource(Long id) {
        resourceRepository.deleteById(id);
    }
}
