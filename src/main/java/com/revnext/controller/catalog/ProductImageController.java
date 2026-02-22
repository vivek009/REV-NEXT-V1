package com.revnext.controller.catalog;

import com.revnext.domain.catalog.Image;
import com.revnext.domain.catalog.Product;
import com.revnext.repository.catalog.ProductRepository;
import com.revnext.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@Slf4j
@RequiredArgsConstructor
public class ProductImageController {

    private final ProductRepository productRepository;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @PostMapping("/{id}/image")
    public ResponseEntity<?> uploadImage(@PathVariable UUID id,
            @RequestParam("file") MultipartFile file) throws IOException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));

        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir, "products");
        Files.createDirectories(uploadPath);

        // Generate unique filename
        String originalName = file.getOriginalFilename();
        String extension = originalName != null && originalName.contains(".")
                ? originalName.substring(originalName.lastIndexOf("."))
                : ".jpg";
        String fileName = id + extension;
        Path filePath = uploadPath.resolve(fileName);

        // Save file
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Remove existing images and add new one
        product.getImages().clear();
        Image image = Image.builder()
                .name(originalName)
                .uri("/api/products/" + id + "/image")
                .provider("LOCAL")
                .mimeType(file.getContentType())
                .sizeBytes(file.getSize())
                .version(1)
                .product(product)
                .build();
        product.getImages().add(image);
        productRepository.save(product);

        log.info("Image uploaded for product {}: {}", id, fileName);
        return ResponseEntity.ok().body(java.util.Map.of(
                "imageUrl", "/api/products/" + id + "/image",
                "fileName", fileName));
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<Resource> getImage(@PathVariable UUID id) throws IOException {
        Path uploadPath = Paths.get(uploadDir, "products");

        // Find the image file (try common extensions)
        Path filePath = null;
        for (String ext : new String[] { ".jpg", ".jpeg", ".png", ".gif", ".webp" }) {
            Path candidate = uploadPath.resolve(id + ext);
            if (Files.exists(candidate)) {
                filePath = candidate;
                break;
            }
        }

        if (filePath == null || !Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new UrlResource(filePath.toUri());
        String contentType = Files.probeContentType(filePath);
        if (contentType == null)
            contentType = "image/jpeg";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CACHE_CONTROL, "max-age=86400")
                .body(resource);
    }

    @DeleteMapping("/{id}/image")
    public ResponseEntity<?> deleteImage(@PathVariable UUID id) throws IOException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));

        // Remove from database
        product.getImages().clear();
        productRepository.save(product);

        // Remove file
        Path uploadPath = Paths.get(uploadDir, "products");
        for (String ext : new String[] { ".jpg", ".jpeg", ".png", ".gif", ".webp" }) {
            Path candidate = uploadPath.resolve(id + ext);
            Files.deleteIfExists(candidate);
        }

        log.info("Image deleted for product {}", id);
        return ResponseEntity.ok().body(java.util.Map.of("message", "Image deleted"));
    }
}
