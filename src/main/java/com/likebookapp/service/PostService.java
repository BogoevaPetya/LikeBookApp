package com.likebookapp.service;

import com.likebookapp.config.UserSession;
import com.likebookapp.model.dto.MyPostDTO;
import com.likebookapp.model.dto.PostAddDTO;
import com.likebookapp.model.dto.PostInfoDTO;
import com.likebookapp.model.entity.Mood;
import com.likebookapp.model.entity.Post;
import com.likebookapp.model.entity.User;
import com.likebookapp.repository.MoodRepository;
import com.likebookapp.repository.PostRepository;
import com.likebookapp.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final UserSession userSession;
    private final MoodRepository moodRepository;

    public PostService(PostRepository postsRepository, ModelMapper modelMapper, UserRepository userRepository, UserSession userSession, MoodRepository moodRepository) {
        this.postRepository = postsRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.userSession = userSession;
        this.moodRepository = moodRepository;
    }


    public boolean add(PostAddDTO postAddDTO) {
        Optional<User> optionalUser = this.userRepository.findByUsername(userSession.getUsername());
        if (optionalUser.isEmpty()){
            return false;
        }
        User user = optionalUser.get();
        Mood mood = moodRepository.findByName(postAddDTO.getMoodNameEnum());

        Post post = modelMapper.map(postAddDTO, Post.class);
        post.setUser(user);
        post.setMood(mood);

        postRepository.save(post);

        return true;
    }

    public List<PostInfoDTO> findOtherPosts() {
        List<PostInfoDTO> postInfoDTOs = new ArrayList<>();

        Optional<User> optionalUser = this.userRepository.findByUsername(userSession.getUsername());
        if (optionalUser.isEmpty()){
            return postInfoDTOs;
        }
        User user = optionalUser.get();

        List<Post> allOtherPosts = postRepository
                .findAll()
                .stream()
                .filter(post -> !post.getUser().getUsername().equals(user.getUsername()))
                .collect(Collectors.toList());

        for (Post post : allOtherPosts) {
            String username = post.getUser().getUsername();
            PostInfoDTO postInfoDTO = modelMapper.map(post, PostInfoDTO.class);
            postInfoDTO.setUsername(username);
            postInfoDTOs.add(postInfoDTO);
        }

        return postInfoDTOs;
    }


    public List<MyPostDTO> findMyPosts() {
        List<MyPostDTO> myPostDTOS = new ArrayList<>();

        Optional<User> optionalUser = this.userRepository.findByUsername(userSession.getUsername());
        if (optionalUser.isEmpty()){
            return myPostDTOS;
        }
        User user = optionalUser.get();

        List<Post> posts = postRepository
                .findAll()
                .stream()
                .filter(post -> post.getUser().getUsername().equals(user.getUsername()))
                .collect(Collectors.toList());

        for (Post post : posts) {
            MyPostDTO myPostDTO = modelMapper.map(post, MyPostDTO.class);
            myPostDTOS.add(myPostDTO);
        }

        return myPostDTOS;
    }

    public void like(Long id) {
        Optional<User> byUsername = userRepository.findByUsername(userSession.getUsername());
        if (byUsername.isEmpty()){
            return;
        }

        User user = byUsername.get();

        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isEmpty()){
            return;
        }

        Post post = optionalPost.get();
        post.likePost(user);
        postRepository.save(post);
    }

    public void remove(Long id) {
        this.postRepository.deleteById(id);
    }
}
