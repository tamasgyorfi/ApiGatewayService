package hu.bets.apigateway.web.api;

import hu.bets.apigateway.model.schedules.Schedules;
import hu.bets.apigateway.service.schedules.SchedulesService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.junit.Assert.*;
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

        when(scheduleService.getAggregatedResult("aaakak")).thenReturn(schedules);
        String schedules = sut.getSchedules("{\"userId\":\"aaakak\"}");

        assertEquals("{\"schedules\":[],\"bets\":[],\"errors\":[]}", schedules);
    }

    @Test
    public void exceptionResultsInErrorPayload() {
        when(scheduleService.getAggregatedResult("aaakak")).thenThrow(new IllegalArgumentException());

        String schedules = sut.getSchedules("{\"userId\":\"aaakak\"}");
        assertEquals("{\"error\":\"Unable to retrieve schedules. null\",\"matches\":[],\"token\":\"\"}", schedules);
    }
}