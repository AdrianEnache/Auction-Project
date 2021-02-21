package com.sda.mapper;

import com.sda.dto.ProductDto;
import com.sda.model.Bid;
import com.sda.model.Product;
import com.sda.model.ProductCategory;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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

    public ProductDto map(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setName(product.getName());
        productDto.setCategory(product.getCategory().name());
        productDto.setDescription(product.getDescription());
        productDto.setStartingPrice(product.getStartingPrice().toString());
        productDto.setMinimumBidStep(product.getMinimumBidStep().toString());
        productDto.setStartBiddingTime(product.getStartBiddingTime().toString());
        productDto.setEndBiddingTime(product.getEndBiddingTime().toString());
        String imageAsString = Base64.encodeBase64String(product.getImage());
        productDto.setBase64Image(imageAsString);
        productDto.setId(product.getId().toString());
        productDto.setCurrentPrice(getCurrentPrice(product));
        return productDto;
    }

    private String getCurrentPrice(Product product) {
        Optional<Bid> optionalMaxBid = product.getBidList()
                .stream()
                .max(Comparator.comparing(Bid::getValue));
        if (optionalMaxBid.isPresent()){
            Bid maxBid = optionalMaxBid.get();
            return maxBid.getValue().toString();
        }
        return product.getStartingPrice().toString();
    }
}
