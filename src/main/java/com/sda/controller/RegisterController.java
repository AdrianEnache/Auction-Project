package com.sda.controller;

import com.sda.dto.UserDto;
import com.sda.service.UserService;
import com.sda.validator.UserDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    private UserService userService;
    private UserDtoValidator userDtoValidator;


    @Autowired
    public RegisterController(UserService userService, UserDtoValidator userDtoValidator) {
        this.userService = userService;
        this.userDtoValidator = userDtoValidator;
    }


    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        System.out.println("se apeleaza getRegister");
        model.addAttribute("userDto", new UserDto());
        return "register";
    }

    @PostMapping("/register")
    public String postRegisterPage(Model model, UserDto userDto, BindingResult bindingResult) {
        System.out.println("se apeleaza postRegister cu " + userDto);
        userDtoValidator.validate(userDto,bindingResult);
        if (bindingResult.hasErrors()){
            //todo - cum pot sa pastrez datele dupa ce primesc eroare
            model.addAttribute("userDto", userDto);
            return "register";
        }
        userService.register(userDto);
        return "redirect:/home";
    }
}
