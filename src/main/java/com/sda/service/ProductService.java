package com.sda.service;

import com.sda.dto.ProductDto;
import com.sda.mapper.ProductMapper;
import com.sda.model.Product;
import com.sda.model.User;
import com.sda.repository.ProductRepository;
import com.sda.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;


@Service
public class ProductService {

    //== fields ==
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private ProductMapper productMapper;

    @Autowired
    public ProductService(UserRepository userRepository, ProductRepository productRepository, ProductMapper productMapper) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public void addProduct(ProductDto productDto, String loggedUserEmail, MultipartFile multipartFile) {
        Product product = productMapper.map(productDto,multipartFile);
        assignSeller(loggedUserEmail, product);
        productRepository.save(product);
    }



    private void assignSeller(String loggedUserEmail, Product product) {
        Optional<User> optionalUser = userRepository.findByEmail(loggedUserEmail);
        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            product.setSeller(user);
        }
    }


    public List<ProductDto> getProductDtoList() {
        List<Product> productList = productRepository.findAll();
        return productMapper.map(productList);
    }
}
