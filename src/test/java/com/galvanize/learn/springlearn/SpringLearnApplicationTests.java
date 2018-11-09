package com.galvanize.learn.springlearn;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SpringLearnApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    LessonRepository repo;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(4000);

    @Test
    public void create() throws ParseException {
        LessonsController controller = new LessonsController(repo);
        //Lesson expected = new Lesson();
        //expected.setId(1L);
        //expected.setTitle("first");
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-dd-MM", Locale.ENGLISH);
        //LocalDate date = LocalDate.parse("2018-11-07", formatter);
        //Date date1 = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
        //Date date1=new DateTimeFormatter("yyyy-MM-dd").parse("2018-11-07");
        //expected.setDeliveredOn(date1);
        Lesson lesson = new Lesson();
        controller.create(lesson);

        then(repo).should(times(1)).save(lesson);

    }

    @Test
	public void updateData() {
    LessonsController controller = new LessonsController(repo);
        Lesson lesson = new Lesson();
        controller.update(lesson);

        then(repo).should(times(1)).save(lesson);
    }

    @Test
    public void delete(){
        LessonsController controller = new LessonsController(repo);
        controller.delete(1L);
        then(repo).should(times(1)).deleteById(1L);
    }

    @Test
    public void serviceTest() throws IOException, ParseException {
        LessonsController controller = new LessonsController(repo);
        wireMockRule.stubFor(get(urlEqualTo("/test/learn/2018-11-07"))
                //.withHeader("Accept", equalTo("application/json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{id:6,title:sixth,deliveredOn:2018-11-07T00:00:00Z}")));
        String dateInString = "2018-11-07T00:00:00Z";

        Instant instant = Instant.parse(dateInString);

        //get date time only
        LocalDate result = LocalDate.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.getId()));
        controller.getByDate(result);

    }





}
