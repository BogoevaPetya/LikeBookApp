package com.likebookapp.service;

import com.likebookapp.repository.MoodRepository;
import org.springframework.stereotype.Service;

@Service
public class MoodService {
    private final MoodRepository moodRepository;

    public MoodService(MoodRepository moodRepository) {
        this.moodRepository = moodRepository;
    }
}
