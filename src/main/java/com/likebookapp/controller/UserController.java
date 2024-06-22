package com.likebookapp.controller;

import com.likebookapp.config.UserSession;
import com.likebookapp.model.dto.UserLoginDTO;
import com.likebookapp.model.dto.UserRegisterDTO;
import com.likebookapp.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class UserController {
    private final UserService userService;
    private final UserSession userSession;

    public UserController(UserService userService, UserSession userSession) {
        this.userService = userService;
        this.userSession = userSession;
    }

    @ModelAttribute
    public UserLoginDTO userLoginDTO(){
        return new UserLoginDTO();
    }

    @ModelAttribute
    public UserRegisterDTO userRegisterDTO(){
        return new UserRegisterDTO();
    }

    @GetMapping("/login")
    public String loginView(){
        if (this.userSession.isLoggedIn()){
            return "redirect:/home";
        }
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@Valid UserLoginDTO userLoginDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (this.userSession.isLoggedIn()){
            return "redirect:/home";
        }

        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("userLoginDTO", userLoginDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userLoginDTO", bindingResult);
            return "redirect:/login";
        }

        boolean isSuccessful = userService.login(userLoginDTO);

        if (!isSuccessful){
            redirectAttributes.addFlashAttribute("passwordMismatch", true);
            redirectAttributes.addFlashAttribute("userLoginDTO", userLoginDTO);
            return "redirect:/login";
        }

        return "redirect:/home";
    }

    @GetMapping("/register")
    public String registerView(){
        if (this.userSession.isLoggedIn()){
            return "redirect:/home";
        }

        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@Valid UserRegisterDTO userRegisterDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if (this.userSession.isLoggedIn()){
            return "redirect:/home";
        }

        if (bindingResult.hasErrors() || !userService.register(userRegisterDTO)){
            redirectAttributes.addFlashAttribute("userRegisterDTO", userRegisterDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegisterDTO", bindingResult);
            return "redirect:/register";
        }

        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String doLogout(){
        if (!this.userSession.isLoggedIn()){
            return "redirect:/";
        }

        userService.logout();
        return "redirect:/";
    }


}
