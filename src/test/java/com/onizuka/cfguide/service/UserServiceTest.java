package com.onizuka.cfguide.service;

import com.google.gson.Gson;
import com.onizuka.cfguide.dto.RestApiResponse;
import com.onizuka.cfguide.dto.UserSubmissionByDateRequest;
import com.onizuka.cfguide.dto.UserSubmissionByDateResponse;
import com.onizuka.cfguide.model.SubmissionList;
import com.onizuka.cfguide.util.HTTPUtil;
import com.onizuka.cfguide.util.TimeUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TimeUtil.class})
public class UserServiceTest {
    @MockBean
    HTTPUtil httpUtil;
    @Captor
    ArgumentCaptor<UserSubmissionByDateRequest> requestArgumentCaptor;
    UserService userService;

    @Before
    public void init() throws IOException, URISyntaxException {
        userService = Mockito.spy(new UserService(httpUtil));
        Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("mockCfResponseForSubByDate.json")).toURI());
        String cfMockResponse = Files.readString(path);
        SubmissionList submissionList = new Gson().fromJson(cfMockResponse, SubmissionList.class);
        RestApiResponse restApiResponse = RestApiResponse.builder()
                .httpStatus(HttpStatus.valueOf(200))
                .responseBody(submissionList)
                .build();
        Objects.requireNonNull(submissionList);
        when(httpUtil.get(any(), any()))
                .thenReturn(restApiResponse);
    }

    @Test
    public void testNotNullGetSubmissionByDate() {
        UserSubmissionByDateRequest request = new UserSubmissionByDateRequest("RogueNinja", (long) 10);
        UserSubmissionByDateResponse response = userService.getSubmissionByDate(request);
        assertNotNull(response);

        verify(httpUtil, times(1)).get(any(), any());
        verify(userService, times(1)).getSubmissionByDate(requestArgumentCaptor.capture());
        assertEquals(request, requestArgumentCaptor.getValue());
    }
}
