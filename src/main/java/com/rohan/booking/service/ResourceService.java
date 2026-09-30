package com.rohan.booking.service;

import com.rohan.booking.dto.resource.ResourceRequest;
import com.rohan.booking.dto.resource.ResourceResponse;
import com.rohan.booking.entity.Resource;
import com.rohan.booking.exception.ResourceNotFoundException;
import com.rohan.booking.repository.ResourceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceService {

    private final ResourceRepository resourceRepository;

    public ResourceService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public ResourceResponse createResource(ResourceRequest request) {

        Resource resource = new Resource();

        resource.setName(request.getName());
        resource.setDescription(request.getDescription());
        resource.setLocation(request.getLocation());
        resource.setPrice(request.getPrice());
        resource.setAvailable(request.getAvailable());

        Resource savedResource = resourceRepository.save(resource);

        return mapToResponse(savedResource);
    }

    public List<ResourceResponse> getAllResources() {

        return resourceRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ResourceResponse getResourceById(Long id) {

        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));

        return mapToResponse(resource);
    }

    public ResourceResponse updateResource(Long id, ResourceRequest request) {

        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));

        resource.setName(request.getName());
        resource.setDescription(request.getDescription());
        resource.setLocation(request.getLocation());
        resource.setPrice(request.getPrice());
        resource.setAvailable(request.getAvailable());

        Resource updatedResource = resourceRepository.save(resource);

        return mapToResponse(updatedResource);
    }

    public void deleteResource(Long id) {

        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));

        resourceRepository.delete(resource);
    }

    private ResourceResponse mapToResponse(Resource resource) {

        return new ResourceResponse(
                resource.getId(),
                resource.getName(),
                resource.getDescription(),
                resource.getLocation(),
                resource.getPrice(),
                resource.isAvailable()
        );
    }
}