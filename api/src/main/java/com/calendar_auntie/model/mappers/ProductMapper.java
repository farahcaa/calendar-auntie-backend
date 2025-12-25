package com.calendar_auntie.model.mappers;

import com.calendar_auntie.model.dtos.ProductReviewCheckoutDTO;
import com.calendar_auntie.model.dtos.ProductDTO;
import com.calendar_auntie.model.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ProductMapper {

  @Mapping(
    target = "thumbnail",
    source = "product",
    qualifiedByName = "firstMediaUrl"
  )
  ProductDTO toDto(Product product);

  Product toEntity(ProductDTO dto);
  @Mapping(target = "thumbnail",source = "product",qualifiedByName = "firstMediaUrl")
  ProductReviewCheckoutDTO toProductReviewCheckoutDTO(Product product);

  @Named("firstMediaUrl")
  default String mapFirstMediaUrl(Product product) {
    if (product == null ||
      product.getMedia() == null ||
      product.getMedia().isEmpty()) {
      return null;
    }

    return product.getMedia().getFirst().getUrl();
  }
}