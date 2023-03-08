package com.incorparation.controller;

import com.incorparation.annotations.Authenticate;
import com.incorparation.costants.Constants;
import com.incorparation.dto.CategoryObject;
import com.incorparation.service.impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/products")
public class ProductController {
    private final ProductServiceImpl productService;

    @Autowired
    public ProductController(ProductServiceImpl productService) {
        this.productService = productService;
    }

    @Authenticate(type = Constants.Authorization.SECURITY_VERIFICATION)
    @GetMapping(value = "/categories")
    public ResponseEntity<CategoryObject> getCategories() {
        return ResponseEntity.ok(productService.getCategories());
    }
}
