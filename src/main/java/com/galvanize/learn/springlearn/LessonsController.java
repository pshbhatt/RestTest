package com.galvanize.learn.springlearn;

import org.springframework.web.bind.annotation.*;
import wiremock.org.apache.http.client.methods.CloseableHttpResponse;
import wiremock.org.apache.http.client.methods.HttpGet;
import wiremock.org.apache.http.impl.client.CloseableHttpClient;
import wiremock.org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;


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

    @PutMapping("")
    public Lesson update(@RequestBody Lesson lesson){
        return this.repo.save(lesson);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam long id){
        this.repo.deleteById(id);
    }

    @PostMapping("/byDate")
    public List<Lesson> getByDate(@RequestParam Date date) throws IOException, ParseException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet("http://localhost:4000/test/learn");
        CloseableHttpResponse httpResponse = httpClient.execute(request);
        String stringResponse = convertHttpResponseToString(httpResponse);
        System.out.println("Response from request in controller: " + stringResponse);
        Date date1=new SimpleDateFormat("yyyy-dd-MM").parse(stringResponse);
        List<Lesson> result = repo.findByDate(date1);
        System.out.println("Records: " + result.get(0).getTitle());
        return result;
    }

    private String convertHttpResponseToString(CloseableHttpResponse httpResponse) throws IOException {
        InputStream inputStream = httpResponse.getEntity().getContent();
        return convertInputStreamToString(inputStream);
    }

    private String convertInputStreamToString(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream, "UTF-8");
        String string = scanner.useDelimiter("\\Z").next();
        scanner.close();
        return string;
    }

}
