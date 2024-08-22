package com.example.ECommerce.mapper;

import com.example.ECommerce.dto.request.ProductRequestDTO;
import com.example.ECommerce.dto.response.ProductResponseDTO;
import com.example.ECommerce.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);
    Product productRequestDtoToProduct(ProductRequestDTO productRequestDTO);
    ProductResponseDTO productToProductResponseDTO(Product product);
    void updateProductFromProductRequestDto(ProductRequestDTO productRequestDTO, @MappingTarget Product product);
}
