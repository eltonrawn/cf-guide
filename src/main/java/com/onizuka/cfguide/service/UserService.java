package com.onizuka.cfguide.service;

import com.onizuka.cfguide.dto.RestApiResponse;
import com.onizuka.cfguide.dto.UserSubmissionByDateRequest;
import com.onizuka.cfguide.model.Submission;
import com.onizuka.cfguide.model.SubmissionList;
import com.onizuka.cfguide.util.HTTPUtil;
import com.onizuka.cfguide.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final HTTPUtil httpUtil;

    @Autowired
    UserService(HTTPUtil httpUtil) {
        this.httpUtil = httpUtil;
    }

    private SubmissionList getSubmissionByDate(UserSubmissionByDateRequest userSubmissionByDateRequest) {
        String uri = getUserInfoFetchURI(userSubmissionByDateRequest);
        RestApiResponse httpResponse = httpUtil.get(uri, SubmissionList.class);
        SubmissionList result = (SubmissionList) httpResponse.getResponseBody();
        Long epochSecond = TimeUtil.getEpochBeforeNDays(userSubmissionByDateRequest.getNoOfDays());
        SubmissionList filteredResult = new SubmissionList();

        for (Submission submission : result.getResult()) {
            if (submission.getCreationTimeSeconds() > epochSecond) {
                filteredResult.getResult().add(submission);
            } else {
                break;
            }
        }
        return filteredResult;
    }

    private String getUserInfoFetchURI(UserSubmissionByDateRequest userSubmissionByDateRequest) {
        return String.format("https://codeforces.com/api/user.status?handle=%s&from=1&count=100",
                userSubmissionByDateRequest.getHandle());
    }

    public Long getSubmissionByDateCount(UserSubmissionByDateRequest userSubmissionByDateRequest) {
        return (long) getSubmissionByDate(userSubmissionByDateRequest).getResult().size();
    }
}
