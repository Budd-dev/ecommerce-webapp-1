package com.example.ECommerce.service;

import com.example.ECommerce.dto.request.ProductRequestDTO;
import com.example.ECommerce.dto.response.ProductResponseDTO;
import com.example.ECommerce.exception.BadRequestException;
import com.example.ECommerce.exception.ResourceNotFoundException;
import com.example.ECommerce.mapper.ProductMapper;
import com.example.ECommerce.model.Product;
import com.example.ECommerce.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.ECommerce.util.ECommerceUtils.ZERO_BIG_DECIMAL;
import static com.example.ECommerce.util.ECommerceUtils.validateId;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    public void validateProduct(ProductRequestDTO productRequestDTO) {
        logger.info("Validating product: {}", productRequestDTO);

        if (productRequestDTO.getName() == null || productRequestDTO.getName().trim().isEmpty()) {
            logger.error("Product name is missing");
            throw new BadRequestException("Product name is required.");
        }

        if (productRequestDTO.getName().length() < 3 || productRequestDTO.getName().length() > 100) {
            logger.error("Product name length is invalid: {}", productRequestDTO.getName().length());
            throw new BadRequestException("Product name must be between 3 and 100 characters.");
        }

        if (productRequestDTO.getPrice() == null || productRequestDTO.getPrice().compareTo(ZERO_BIG_DECIMAL) <= 0) {
            logger.error("Product price is invalid: {}", productRequestDTO.getPrice());
            throw new BadRequestException("Product price is required and can't be negative.");
        }

        if (productRequestDTO.getQuantity() == null || productRequestDTO.getQuantity() < 0) {
            logger.error("Product quantity is invalid: {}", productRequestDTO.getQuantity());
            throw new BadRequestException("Product quantity is required and cannot be negative");
        }

        if (productRequestDTO.getDescription() == null || productRequestDTO.getDescription().trim().isEmpty()) {
            logger.error("Product description is missing");
            throw new BadRequestException("Product description is required.");
        }

        if (productRequestDTO.getDescription().length() > 500) {
            logger.error("Product description length is too long: {}", productRequestDTO.getDescription().length());
            throw new BadRequestException("Product description must be 500 characters or less.");
        }

        logger.debug("Product validation successful: {}", productRequestDTO);
    }

    public List<ProductResponseDTO> fetchAllProducts() {
        logger.info("Fetching all products");
        List<ProductResponseDTO> products = productRepository.findAll().stream()
                .map(productMapper::productToProductResponseDTO)
                .collect(Collectors.toList());
        logger.info("Fetched {} products", products.size());
        return products;
    }

    public Integer getProductQuantity(Integer productId) {
        logger.info("Fetching quantity for product ID: {}", productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    logger.error("Product not found for ID: {}", productId);
                    return new ResourceNotFoundException("Product not found for id: " + productId);
                });
        validateId(productId, "Product");
        logger.info("Product quantity for ID {}: {}", productId, product.getQuantity());
        return product.getQuantity();
    }

    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) throws BadRequestException {
        logger.info("Creating a new product");
        validateProduct(productRequestDTO);

        Product product = productMapper.productRequestDtoToProduct(productRequestDTO);
        Product savedProduct = productRepository.save(product);
        logger.info("Product created successfully: {}", savedProduct);
        return productMapper.productToProductResponseDTO(savedProduct);
    }

    public ProductResponseDTO fetchProductsById(Integer id) {
        logger.info("Fetching product by ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Product not found for ID: {}", id);
                    return new ResourceNotFoundException("Product dne for id: " + id);
                });
        validateId(id, "Product");
        logger.info("Fetched product: {}", product);
        return productMapper.productToProductResponseDTO(product);
    }

    @Transactional
    public ProductResponseDTO updateProduct(Integer id, ProductRequestDTO productRequestDTO) {
        logger.info("Updating product with ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Product not found for ID: {}", id);
                    return new ResourceNotFoundException("Product dne for id: " + id);
                });
        validateId(id, "Product");
        validateProduct(productRequestDTO);

        productMapper.updateProductFromProductRequestDto(productRequestDTO, product);
        Product updatedProduct = productRepository.save(product);
        logger.info("Product updated successfully: {}", updatedProduct);
        return productMapper.productToProductResponseDTO(updatedProduct);
    }

    @Transactional
    public ResponseEntity<HttpStatus> deleteProduct(Integer id) {
        logger.info("Deleting product with ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Product not found for ID: {}", id);
                    return new ResourceNotFoundException("Product dne for id: " + id);
                });

        productRepository.delete(product);
        logger.info("Product deleted successfully for ID: {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
