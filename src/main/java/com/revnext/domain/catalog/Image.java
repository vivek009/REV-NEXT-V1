package com.revnext.domain.catalog;

import com.revnext.domain.BaseData;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import jakarta.persistence.GenerationType;

@Entity(name = "IMAGE")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Image extends BaseData {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Where the image is stored (local, S3, CDN, etc.)
    @Column(name = "uri", nullable = false)
    private String uri;

    // e.g. LOCAL, S3, AZURE, GCS
    @Column(name = "provider")
    private String provider;

    // MIME type like image/png, image/jpeg
    @Column(name = "mime_type")
    private String mimeType;

    // Optional size in bytes
    @Column(name = "size_bytes")
    private Long sizeBytes;

    // Versioning (e.g. v1, v2, etc.)
    @Column(name = "version")
    private Integer version;

    // Which product this image belongs to
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Optional metadata as JSON (e.g., resolution, tags)
    @Column(name = "metadata", columnDefinition = "TEXT")
    private String metadata;
}
