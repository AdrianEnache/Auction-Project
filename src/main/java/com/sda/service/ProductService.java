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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class ProductService {

    //== fields ==
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

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


    public List<ProductDto> getProductDtoList(String authenticatedUserEmail) {
        List<Product> productList = productRepository.findAll();
        return productMapper.map(productList,authenticatedUserEmail);
    }

    public List<ProductDto> getActiveProductDtoList(String authenticatedUserEmail) {
        List<Product> productList = productRepository.findAllActive(LocalDateTime.now());
        return productMapper.map(productList,authenticatedUserEmail);
    }

    public List<ProductDto> getProductDtoListFor(String authenticatedUserEmail) {
        List<Product> productList = productRepository.findAllByBidder(authenticatedUserEmail);
        return productMapper.map(productList,authenticatedUserEmail);
    }

    public Optional<ProductDto> getProductDtoBy(String productId,String authenticatedUserEmail) {
        Optional<Product> optionalProduct = productRepository.findById(Integer.parseInt(productId));
        if (!optionalProduct.isPresent()){
            return Optional.empty();
        }
        ProductDto productDto = productMapper.map(optionalProduct.get(),authenticatedUserEmail);
        return Optional.of(productDto);
    }

    public List<ProductDto> search(String keyword,String authenticatedUserEmail){
        List<Product> searchProducts = productRepository.search(keyword);
        return productMapper.map(searchProducts,authenticatedUserEmail);
    }


    public List<ProductDto> getActiveBiddingList(String authenticatedUserEmail) {
        List<Product> productList = productRepository.findAllActiveByBidder(authenticatedUserEmail,LocalDateTime.now());
        return productMapper.map(productList,authenticatedUserEmail);
    }


    public List<ProductDto> getExpiredAndAssignedList(String authenticatedUserEmail) {
        List<Product> productList = productRepository.findAllExpiredAndAssigned(authenticatedUserEmail,LocalDateTime.now());
        return productMapper.map(productList,authenticatedUserEmail);
    }

    public Date getParse(String endBiddingTime) throws ParseException {
        return new SimpleDateFormat("dd-MM-yyyy hh:mm").parse(endBiddingTime);
    }
}
