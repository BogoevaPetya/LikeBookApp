package com.likebookapp.model.dto;

import com.likebookapp.model.entity.Mood;
import com.likebookapp.model.entity.MoodNameEnum;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PostAddDTO {
    @NotNull(message = "Content length must be between 2 and 50 characters!")
    @Size(min = 2, max = 150, message = "Content length must be between 2 and 50 characters!")
    private String content;
    @NotNull(message = "You must select a mood!")
    private MoodNameEnum moodNameEnum;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MoodNameEnum getMoodNameEnum() {
        return moodNameEnum;
    }

    public void setMoodNameEnum(MoodNameEnum moodNameEnum) {
        this.moodNameEnum = moodNameEnum;
    }
}
