package com.galvanize.learn.springlearn;

import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface LessonRepository extends CrudRepository<Lesson, Long> {

    public List<Lesson> findByDeliveredOn(Date deliveredOn);

}
