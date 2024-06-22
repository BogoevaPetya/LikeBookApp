package com.likebookapp.model.dto;

import com.likebookapp.model.entity.Mood;
import com.likebookapp.model.entity.User;

import java.util.Set;

public class MyPostDTO {
    private Long id;
    private Mood mood;
    private Set<User> likes;
    private String content;

    public Mood getMood() {
        return mood;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMood(Mood mood) {
        this.mood = mood;
    }

    public Set<User> getLikes() {
        return likes;
    }

    public void setLikes(Set<User> likes) {
        this.likes = likes;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
