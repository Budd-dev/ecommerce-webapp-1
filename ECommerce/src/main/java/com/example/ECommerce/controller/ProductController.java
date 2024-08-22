package com.example.ECommerce.controller;

import com.example.ECommerce.dto.request.ProductRequestDTO;
import com.example.ECommerce.dto.response.ProductResponseDTO;
import com.example.ECommerce.exception.BadRequestException;
// import com.example.ECommerce.repository.ProductRepository;
import com.example.ECommerce.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@RestController
@RequestMapping("/api/ecommerce/products/")
public class ProductController {
    // @Autowired
    // private ProductRepository productRepository ;
    @Autowired
    private ProductService productService;

    // api to fetch all products
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> fetchAllProducts() {
        log.info("Fetching all products...");
        return ResponseEntity.ok(productService.fetchAllProducts());
    }

    // to get the stock (quantity) of current product by its id
    @GetMapping("{id}/quantity")
    public ResponseEntity<Integer> getProductQuantity(@PathVariable Integer id) {
        Integer quantity = productService.getProductQuantity(id);
        return ResponseEntity.ok(quantity);
    }


    // api to create a product object
    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductRequestDTO productRequestDTO) throws BadRequestException {
        return ResponseEntity.ok(productService.createProduct(productRequestDTO));
    }

    // api to fetch products by id
    @GetMapping("{id}")
    public ResponseEntity<ProductResponseDTO> fetchProductsById(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.fetchProductsById(id));
    }

    // api to update product data given its id
    @PutMapping("{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct
    (@PathVariable Integer id, @RequestBody ProductRequestDTO productRequestDTO) {
        return ResponseEntity.ok(productService.updateProduct(id, productRequestDTO));
    }

    // delete product REST api
    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> deleteProductById(@PathVariable Integer id) {
        return productService.deleteProduct(id);
    }
}
