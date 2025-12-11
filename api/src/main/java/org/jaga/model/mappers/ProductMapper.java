package org.jaga.model.mappers;

import org.jaga.model.dtos.ProductDTO;
import org.jaga.model.entities.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
  ProductDTO toDto(Product product);
  Product toEntity(ProductDTO dto);
}