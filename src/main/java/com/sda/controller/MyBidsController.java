package com.sda.controller;

import com.sda.dto.ProductDto;
import com.sda.dto.UserHeaderDto;
import com.sda.service.ProductService;
import com.sda.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MyBidsController {

    private final ProductService productService;
    private final UserService userService;

    @Autowired
    public MyBidsController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping("/myBids")
    public String getMyBids(Model model, Authentication authentication) {
//        List<ProductDto> productDtoList = productService.getProductDtoListFor(authentication.getName());// this gives us the list of productsDto
        List<ProductDto> productActiveBiddingList = productService.getActiveBiddingList(authentication.getName());
        model.addAttribute("productActiveBiddingList", productActiveBiddingList);

        List<ProductDto> productDtoExpiredAndAssignedList = productService.getExpiredAndAssignedList(authentication.getName());
        model.addAttribute("productDtoExpiredAndAssignedList",productDtoExpiredAndAssignedList);
        UserHeaderDto userHeaderDto = userService.getUserHeaderDto(authentication.getName());
        model.addAttribute("userHeaderDto", userHeaderDto);

        return "myBids";
    }
}
