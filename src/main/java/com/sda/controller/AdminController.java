package com.sda.controller;

import com.sda.dto.ProductDto;
import com.sda.dto.UserHeaderDto;
import com.sda.service.ProductService;
import com.sda.service.UserService;
import com.sda.validator.ProductDtoValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
public class AdminController {

    private final ProductDtoValidator productDtoValidator;
    private final ProductService productService;
    private final UserService userService;

    @Autowired
    public AdminController(ProductDtoValidator productDtoValidator,ProductService productService,UserService userService) {
        this.productDtoValidator = productDtoValidator;
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping("/addProduct")
    public String getAddProduct(Model model,Authentication authentication){
        model.addAttribute("productDto",new ProductDto());

        UserHeaderDto userHeaderDto =userService.getUserHeaderDto(authentication.getName());
        model.addAttribute("userHeaderDto",userHeaderDto);
        return "addProduct";
    }

    @PostMapping("/addProduct")
    public String postAddProduct(Model model, ProductDto productDto, BindingResult bindingResult,
                                 Authentication authentication,@RequestParam("image") MultipartFile multipartFile){
        String loggedUserEmail = authentication.getName();
        UserHeaderDto userHeaderDto = userService.getUserHeaderDto(loggedUserEmail);
        productDtoValidator.validate(productDto,bindingResult);
        if (bindingResult.hasErrors()){
            model.addAttribute("productDto",productDto);
            model.addAttribute("userHeaderDto",userHeaderDto);
            return "addProduct";
        }
        log.info("product Added");
        productService.addProduct(productDto,loggedUserEmail,multipartFile);
        return "redirect:/addProduct";
    }

}
