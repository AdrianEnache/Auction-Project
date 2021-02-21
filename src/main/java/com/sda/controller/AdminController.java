package com.sda.controller;

import com.sda.dto.ProductDto;
import com.sda.service.ProductService;
import com.sda.validator.ProductDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class AdminController {

    private ProductDtoValidator productDtoValidator;
    private ProductService productService;

    @Autowired
    public AdminController(ProductDtoValidator productDtoValidator,ProductService productService) {
        this.productDtoValidator = productDtoValidator;
        this.productService = productService;
    }

    @GetMapping("/addProduct")
    public String getAddProduct(Model model){
        model.addAttribute("productDto",new ProductDto());
        return "addProduct";
    }

    @PostMapping("/addProduct")
    public String postAddProduct(Model model, ProductDto productDto, BindingResult bindingResult,
                                 Authentication authentication,@RequestParam("image") MultipartFile multipartFile){
        String loggedUserEmail = authentication.getName();
        System.out.println(loggedUserEmail);
        productDtoValidator.validate(productDto,bindingResult);
        if (bindingResult.hasErrors()){
            model.addAttribute("productDto",productDto);
            return "addProduct";
        }
        productService.addProduct(productDto,loggedUserEmail,multipartFile);
        return "redirect:/addProduct";
    }

}
