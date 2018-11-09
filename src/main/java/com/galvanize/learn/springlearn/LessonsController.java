package com.galvanize.learn.springlearn;

import org.springframework.web.bind.annotation.*;
import wiremock.org.apache.http.client.methods.CloseableHttpResponse;
import wiremock.org.apache.http.client.methods.HttpGet;
import wiremock.org.apache.http.impl.client.CloseableHttpClient;
import wiremock.org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
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

    @PostMapping("/create")
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
    public void getByDate(@RequestParam LocalDate date) throws IOException, ParseException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet("http://localhost:4000/test/learn/" + date.toString());
        CloseableHttpResponse httpResponse = httpClient.execute(request);
        String stringResponse = convertHttpResponseToString(httpResponse);
        Lesson lesson = new Lesson();
        String newJson = stringResponse.replace("{", "");
        String[] members = newJson.split(",");
        List<String> newElements = new ArrayList<>();
        for(String member: members){
            String[] elements = member.split(":");
            newElements.add(elements[1]);
        }

        lesson.setId(Long.parseLong(newElements.get(0)));
        lesson.setTitle(newElements.get(1));
        System.out.println("====================================================="+newElements.get(2));
        Instant instant = Instant.parse(newElements.get(2)+":"+newElements.get(3));

        //get date time only
        LocalDate result = LocalDate.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.getId()));
        lesson.setDeliveredOn(result);
        create(lesson);


        //sendDataByDate(stringResponse, date.toString());
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

    public void sendDataByDate(String input, String date) throws IOException {
        URL url = new URL("http://localhost:8080/lessons/create");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");

        OutputStream os = conn.getOutputStream();
        os.write(input.getBytes());
        os.flush();

        if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));

        String output;
        System.out.println("Output from Server .... \n");
        while ((output = br.readLine()) != null) {
            System.out.println(output);
        }

    }

}
