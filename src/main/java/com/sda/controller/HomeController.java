package com.sda.controller;

import com.sda.dto.UserDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String getHomepage(){
        return "home";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model){
        System.out.println("se apeleaza getRegister");
        model.addAttribute("userDto",new UserDto());
        return "register";
    }

    @PostMapping("/register")
    public String postRegisterPage(UserDto userDto){
        System.out.println("se apeleaza postRegister cu " + userDto);
        return "register";
    }
}
