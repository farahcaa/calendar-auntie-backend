package com.calendar_auntie.model.mappers;

import com.calendar_auntie.model.dtos.ProductDTO;
import com.calendar_auntie.model.entities.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
  ProductDTO toDto(Product product);
  Product toEntity(ProductDTO dto);
}