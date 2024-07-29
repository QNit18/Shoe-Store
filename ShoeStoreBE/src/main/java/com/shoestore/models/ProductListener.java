package com.shoestore.models;

import com.shoestore.services.ProductRedisService;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;

import java.util.logging.Logger;

@AllArgsConstructor
public class ProductListener {
    private final ProductRedisService productRedisService;
    private static final Logger logger = Logger.getLogger(ProductListener.class.getName());


    @PrePersist
    public void prePersist(Product product) {
        logger.info("PrePersist: " + product);
    }

    @PostPersist // save = persis
    public void postPersist(Product product) {
        // Update Redis cache
        logger.info("PostPersist: " + product);
        productRedisService.clear();
    }


    @PreUpdate
    public void preUpdate(Product product) {
        logger.info("PreUpdate: " + product);
    }

    @PostUpdate
    public void postUpdate(Product product) {
        logger.info("PostUpdate: " + product);
        productRedisService.clear();
    }

    @PreRemove
    public void preRemove(Product product) {
        logger.info("PreRemove: " + product);
    }

    @PostRemove
    public void postRemove(Product product) {
        logger.info("PostRemove: " + product);
        productRedisService.clear();
    }

}
