package com.sda.controller;

import com.sda.dto.BidDto;
import com.sda.dto.ProductDto;
import com.sda.dto.UserHeaderDto;
import com.sda.service.BidService;
import com.sda.service.ProductService;
import com.sda.service.UserService;
import com.sda.validator.BidValidator;
import com.sda.validator.GenericValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
public class HomeController {

    private final ProductService productService;
    private final GenericValidator genericValidator;
    private final BidValidator bidValidator;
    private final BidService bidService;
    private final UserService userService;

    @Autowired
    public HomeController(ProductService productService, GenericValidator genericValidator,
                          BidValidator bidValidator, BidService bidService,UserService userService) {
        this.productService = productService;
        this.genericValidator = genericValidator;
        this.bidValidator = bidValidator;
        this.bidService = bidService;
        this.userService = userService;
    }

    @GetMapping("/home")
    public String getHomepage(Model model,Authentication authentication) {
        List<ProductDto> productDtoList = productService.getActiveProductDtoList(authentication.getName());// this gives us the list of productsDto
        model.addAttribute("productDtoList", productDtoList);
        UserHeaderDto userHeaderDto =userService.getUserHeaderDto(authentication.getName());
        model.addAttribute("userHeaderDto",userHeaderDto);
        return "home";
    }

    @GetMapping("/search")
    public String searchProduct(Model model, @Param("keyword") String keyword,Authentication authentication){
        model.addAttribute("keyword",keyword);
        List<ProductDto> searchedProductDtoList = productService.search(keyword, authentication.getName());
        UserHeaderDto userHeaderDto =userService.getUserHeaderDto(authentication.getName());
        model.addAttribute("searchResult",searchedProductDtoList);
        model.addAttribute("userHeaderDto",userHeaderDto);
        return "search_result";
    }

    @GetMapping("/viewProduct/{productId}")
    public String getViewProduct(Model model, @PathVariable(value = "productId") String productId,Authentication authentication) {
        if (genericValidator.isNotPositiveInteger(productId)) {
            return "redirect:/home";
        }
        Optional<ProductDto> optionalProductDto = productService.getProductDtoBy(productId,authentication.getName());
        if (!optionalProductDto.isPresent()) {
            return "redirect:/home";
        }
        ProductDto productDto = optionalProductDto.get();
        model.addAttribute("product", productDto);
        model.addAttribute("bidDto", new BidDto());

        UserHeaderDto userHeaderDto =userService.getUserHeaderDto(authentication.getName());
        model.addAttribute("userHeaderDto",userHeaderDto);

        return "viewProduct";
    }

    @PostMapping("/viewProduct/{productId}")
    public String postBid(Model model, @PathVariable(value = "productId") String productId,
                          BidDto bidDto, BindingResult bindingResult, Authentication authentication) {
        String loggedUserEmail = authentication.getName();
        bidValidator.validate(productId, bidDto, bindingResult);
        Optional<ProductDto> optionalProductDto = productService.getProductDtoBy(productId,authentication.getName());
        if (bindingResult.hasErrors()) {
            model.addAttribute("bidDto",bidDto);
            model.addAttribute("product", optionalProductDto.get());
            return "viewProduct";
        }
        bidService.placeBid(bidDto,productId,loggedUserEmail);
        return "redirect:/home";
    }


}
