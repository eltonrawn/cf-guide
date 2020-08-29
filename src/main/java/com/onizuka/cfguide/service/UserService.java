package com.onizuka.cfguide.service;

import com.onizuka.cfguide.dto.UserSubmissionByDateRequest;
import com.onizuka.cfguide.model.Submission;
import com.onizuka.cfguide.model.SubmissionList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class UserService {

    @Autowired
    RestTemplate restTemplate;

    public SubmissionList getSubmissionByDate(UserSubmissionByDateRequest userSubmissionByDateRequest) {
        String uri = String.format("https://codeforces.com/api/user.status?handle=%s&from=1&count=100",
                userSubmissionByDateRequest.getHandle());

        SubmissionList result = restTemplate.getForObject(uri, SubmissionList.class);

        Instant now = Instant.now();
        Instant yesterday = now.minus(userSubmissionByDateRequest.getNoOfDays(), ChronoUnit.DAYS);
        Long epochSecond = yesterday.getEpochSecond();

        SubmissionList filteredResult = new SubmissionList();

        for(Submission submission: result.getResult()) {
//            System.out.println(submission.getVerdict());
//            System.out.println(submission.getCreationTimeSeconds());
            if(submission.getCreationTimeSeconds() > epochSecond) {
                filteredResult.getResult().add(submission);
            }
            else {
                break;
            }
        }
//        System.out.println(yesterday.getEpochSecond());
//        System.out.println(backCount);

        return filteredResult;
    }

    public Long getSubmissionByDateCount(UserSubmissionByDateRequest userSubmissionByDateRequest) {
        return (long) getSubmissionByDate(userSubmissionByDateRequest).getResult().size();
    }
}
