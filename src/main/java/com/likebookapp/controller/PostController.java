package com.likebookapp.controller;

import com.likebookapp.config.UserSession;
import com.likebookapp.model.dto.PostAddDTO;
import com.likebookapp.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class PostController {
    private final PostService postService;
    private final UserSession userSession;

    public PostController(PostService postService, UserSession userSession) {
        this.postService = postService;
        this.userSession = userSession;
    }

    @ModelAttribute
    public PostAddDTO postAddDTO(){
        return new PostAddDTO();
    }

    @GetMapping("/post-add")
    public String addPostView(){
        if (!this.userSession.isLoggedIn()){
            return "redirect:/";
        }

        return "post-add";
    }

    @PostMapping("/post-add")
    public String addPost(@Valid PostAddDTO postAddDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if (!this.userSession.isLoggedIn()){
            return "redirect:/";
        }

        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("postAddDTO", postAddDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.postAddDTO", bindingResult);
            return "redirect:/post-add";
        }

        boolean success = postService.add(postAddDTO);

        if (!success){
            redirectAttributes.addFlashAttribute("postAddDTO", postAddDTO);
            return "redirect:/post-add";
        }

        return "redirect:/home";
    }
}
