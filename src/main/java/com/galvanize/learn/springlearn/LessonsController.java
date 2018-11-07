package com.galvanize.learn.springlearn;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lessons")
public class LessonsController {

    private final LessonRepository repo;


    public LessonsController(LessonRepository repo){
        this.repo = repo;
    }

    @GetMapping("")
    public Iterable<Lesson> all(){
        return this.repo.findAll();
    }

    @PostMapping("")
    public Lesson create(@RequestBody Lesson lesson){
        return this.repo.save(lesson);
    }

}
