package com.likebookapp.controller;

import com.likebookapp.config.UserSession;
import com.likebookapp.model.dto.MyPostDTO;
import com.likebookapp.model.dto.PostInfoDTO;
import com.likebookapp.service.PostService;
import com.likebookapp.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class HomeController {
    private final PostService postService;
    private final UserSession userSession;

    public HomeController(PostService postService, UserSession userSession) {
        this.postService = postService;
        this.userSession = userSession;
    }

    @GetMapping("/")
    public String index(){
        if (this.userSession.isLoggedIn()){
            return "redirect:/home";
        }
        return "index";
    }

    @GetMapping("/home")
    public String home(Model model){
        if (!this.userSession.isLoggedIn()){
            return "redirect:/";
        }

        List<PostInfoDTO> otherPosts = postService.findOtherPosts();
        List<MyPostDTO> myPosts = postService.findMyPosts();

        model.addAttribute("otherPosts", otherPosts);
        model.addAttribute("myPosts", myPosts);

        return "home";
    }

    @GetMapping("/posts/like/{id}")
    private String likePost(@PathVariable Long id){
        if (!this.userSession.isLoggedIn()){
            return "redirect:/";
        }

        postService.like(id);
        return "redirect:/home";
    }

    @GetMapping("/posts/remove/{id}")
    private String removePost(@PathVariable Long id){
        if (!this.userSession.isLoggedIn()){
            return "redirect:/";
        }

        postService.remove(id);
        return "redirect:/home";
    }


}
