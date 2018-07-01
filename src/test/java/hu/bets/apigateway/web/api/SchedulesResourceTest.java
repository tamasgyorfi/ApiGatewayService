package hu.bets.apigateway.web.api;

import hu.bets.apigateway.model.schedules.Schedules;
import hu.bets.apigateway.model.users.User;
import hu.bets.apigateway.service.schedules.SchedulesService;
import hu.bets.common.util.json.Json;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.core.Response;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SchedulesResourceTest {

    private SchedulesResource sut;
    @Mock
    private SchedulesService scheduleService;
    private Schedules schedules = new Schedules(Collections.emptyList(), Collections.emptyList(), null, Collections.emptyList());

    @Before
    public void before() {
        sut = new SchedulesResource(scheduleService);
    }

    @Test
    public void getSchedulesParsesPayloadAndDelegates() {

        System.out.println(new Json().toJson(new User("id", "http", "nev")));

        when(scheduleService.getAggregatedResult("aaakak")).thenReturn(schedules);
        Response response = sut.getSchedules("{\"userId\":\"aaakak\"}");
        String schedules = (String) response.getEntity();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals("{\"schedules\":[],\"bets\":[],\"errors\":[]}", schedules);
    }

    @Test
    public void exceptionResultsInErrorPayload() {
        when(scheduleService.getAggregatedResult("aaakak")).thenThrow(new IllegalArgumentException());

        Response response = sut.getSchedules("{\"userId\":\"aaakak\"}");
        String schedules = (String) response.getEntity();

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertEquals("{\"error\":\"Unable to retrieve schedules. null\",\"matches\":[],\"token\":\"\"}", schedules);
    }
}