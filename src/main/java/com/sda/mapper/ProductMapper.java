package com.sda.mapper;

import com.sda.dto.ProductDto;
import com.sda.model.Product;
import com.sda.model.ProductCategory;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProductMapper {

    public Product map(ProductDto productDto, MultipartFile multipartFile) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setCategory(ProductCategory.valueOf(productDto.getCategory()));
        product.setStartingPrice(Integer.valueOf(productDto.getStartingPrice()));
        product.setMinimumBidStep(Integer.valueOf(productDto.getMinimumBidStep()));
        product.setStartBiddingTime(LocalDateTime.parse(productDto.getStartBiddingTime()));
        product.setEndBiddingTime(LocalDateTime.parse(productDto.getEndBiddingTime()));
        try {
            product.setImage(multipartFile.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return product;
    }

    public List<ProductDto> map(List<Product> productList) {
        List<ProductDto> productDtoList = new ArrayList<>();
        for (Product product : productList) {
            ProductDto productDto = map(product);
//            productDto.setImage(product.getImage().toString());
            productDtoList.add(productDto);
        }

        return productDtoList;
    }

    private ProductDto map(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setName(product.getName());
        productDto.setCategory(product.getCategory().name());
        productDto.setDescription(product.getDescription());
        productDto.setStartingPrice(product.getStartingPrice().toString());
        productDto.setMinimumBidStep(product.getMinimumBidStep().toString());
        productDto.setStartBiddingTime(product.getStartBiddingTime().toString());
        productDto.setEndBiddingTime(product.getEndBiddingTime().toString());
        //transforma imaginea intr-un string
        String imageAsString = Base64.encodeBase64String(product.getImage());
        productDto.setBase64Image(imageAsString);
        return productDto;
    }
}
