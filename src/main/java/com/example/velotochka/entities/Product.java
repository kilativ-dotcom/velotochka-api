package com.example.velotochka.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.*;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double price;
    private String description;
    @CreatedDate
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date created;
    @LastModifiedDate
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date updated;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @ElementCollection
    private Set<Image> images;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = CascadeType.ALL)
    private List<Feature> features;


    public Product() {}

    public void addImage(Image image) {
        images.add(image);
    }
    public void removeImage(Image image) {
        images.remove(image);
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Set<Image> getImages() {
        return images;
    }

    public void setImages(Set<Image> images) {
        this.images = images;
    }

    public List<Feature> getFeatures() {
        return features;
    }
    public Map<String, List<Object>> getFeaturesMap() {
        Map<String, List<Object>> result = new HashMap<>();
        for (Feature f : features) {
            result.computeIfAbsent(f.getName(), k -> new ArrayList<>()).add(f.getConvertedValue());
        }
        return result;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
