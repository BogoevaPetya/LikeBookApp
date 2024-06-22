package com.likebookapp.init;

import com.likebookapp.model.entity.Mood;
import com.likebookapp.model.entity.MoodNameEnum;
import com.likebookapp.repository.MoodRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class MoodInit implements CommandLineRunner {
    private final MoodRepository moodRepository;

    public MoodInit(MoodRepository moodRepository) {
        this.moodRepository = moodRepository;
    }


    @Override
    public void run(String... args) throws Exception {
        if (this.moodRepository.count() == 0){
            List<Mood> moods = new ArrayList<>();

            Arrays.stream(MoodNameEnum.values())
                    .forEach(m -> {
                        Mood mood = new Mood();
                        mood.setName(m);
                        moods.add(mood);
                    });

            moodRepository.saveAll(moods);
        }
    }
}
