package com.sda.controller;

import com.sda.dto.ProductDto;
import com.sda.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@Controller
public class HomeController {

    private ProductService productService;

    @Autowired
    public HomeController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/home")
    public String getHomepage(Model model) {
        List<ProductDto> productDtoList = productService.getProductDtoList();// this gives us the list of productsDto
        model.addAttribute("productDtoList",productDtoList);
        return "home";
    }

    @GetMapping("/viewProduct")
    public String getViewProduct(Model model){
        ProductDto productDto = new ProductDto();
        productDto.setName("Flamingo");
        productDto.setStartingPrice("100000");
        productDto.setDescription("deadafafgasga");
        productDto.setCategory("house");
        model.addAttribute("product",productDto);
        return "viewProduct";
    }




}
