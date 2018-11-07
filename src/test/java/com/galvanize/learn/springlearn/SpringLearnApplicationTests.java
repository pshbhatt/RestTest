package com.galvanize.learn.springlearn;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringLearnApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
	public void getData() throws Exception {
        LessonRepository repo = Mockito.mock(LessonRepository.class);
	    LessonsController controller = new LessonsController(repo);
	    //LessonsController mockController = Mockito.mock(LessonsController.class);
        String resultJson = "{\n" +
                "        \"id\": 1,\n" +
                "        \"title\": \"first\",\n" +
                "        \"deliveredOn\": \"2018-11-07\"\n" +
                "    }";
        Lesson lesson = new Lesson();
        lesson.setId(1L);
        lesson.setTitle("first");
        Date date1=new SimpleDateFormat("yyyy-MM-dd").parse("2018-11-07");
        lesson.setDeliveredOn(date1);
        //when(controller.all()).thenReturn(resultJson);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/lessons").accept(
                MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        System.out.println(result.getResponse());
        String expected = "{id:1,title:first,deliveredOn:2018-11-07}";

        // {"id":"Course1","name":"Spring","description":"10 Steps, 25 Examples and 10K Students","steps":["Learn Maven","Import Project","First Example","Second Example"]}

        JSONAssert.assertEquals(expected, result.getResponse()
                .getContentAsString(), false);
    }

}
