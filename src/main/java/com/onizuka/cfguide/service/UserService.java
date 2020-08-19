package com.onizuka.cfguide.service;

import com.onizuka.cfguide.model.SubmissionList;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserService {

    public String getSubmission() {
        final String uri = "https://codeforces.com/api/user.status?handle=Fefer_Ivan&from=1&count=10";

        RestTemplate restTemplate = new RestTemplate();
        SubmissionList result = restTemplate.getForObject(uri, SubmissionList.class);

        return result.getStatus();
    }
}
