package com.onizuka.cfguide.service;

import com.google.gson.Gson;
import com.onizuka.cfguide.dto.RestApiResponse;
import com.onizuka.cfguide.model.ContestList;
import com.onizuka.cfguide.util.CGResourceUtil;
import com.onizuka.cfguide.util.HTTPUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        CGResourceUtil.class
})
public class ContestInfoServiceTest {
    @Value("${cg.config.defaultContestType}")
    String defaultContestType;

    @MockBean
    HTTPUtil httpUtil;
    @Autowired
    CGResourceUtil cgResourceUtil;

    ContestInfoService contestInfoService;

    @Before
    public void init() throws IOException, URISyntaxException {
        contestInfoService = Mockito.spy(new ContestInfoService(httpUtil, cgResourceUtil, defaultContestType));
        Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("mockCFContestList.json")).toURI());
        String cfMockContests = Files.readString(path);
        ContestList contests = new Gson().fromJson(cfMockContests, ContestList.class);
        RestApiResponse restApiResponse = RestApiResponse.builder()
                .httpStatus(HttpStatus.valueOf(200))
                .responseBody(contests)
                .build();
        Objects.requireNonNull(contests);
        when(httpUtil.get(eq("https://codeforces.com/api/contest.list"), any()))
                .thenReturn(restApiResponse);
    }

    @Test
    public void testUpdateContestList() {
        assertEquals(defaultContestType, contestInfoService.getContestTypeById(1415));
        assertEquals("Div3", contestInfoService.getContestTypeById(1409));
        assertEquals("Div2", contestInfoService.getContestTypeById(1417));
        assertEquals("Div2", contestInfoService.getContestTypeById(1420));
        assertEquals("Div2", contestInfoService.getContestTypeById(1418));
        assertEquals("Div2", contestInfoService.getContestTypeById(1407));
        assertEquals("Div1", contestInfoService.getContestTypeById(1404));
        assertEquals("Div1", contestInfoService.getContestTypeById(1416));
        assertEquals("Combine", contestInfoService.getContestTypeById(1284));
        assertEquals("Combine", contestInfoService.getContestTypeById(1270));
        assertEquals("Combine", contestInfoService.getContestTypeById(1266));
        assertEquals("Combine", contestInfoService.getContestTypeById(1408));
    }
}
