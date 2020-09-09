package com.onizuka.cfguide.service;

import com.onizuka.cfguide.dto.RestApiResponse;
import com.onizuka.cfguide.dto.UserSubmissionByDateRequest;
import com.onizuka.cfguide.dto.UserSubmissionByDateResponse;
import com.onizuka.cfguide.model.Submission;
import com.onizuka.cfguide.model.SubmissionList;
import com.onizuka.cfguide.util.HTTPUtil;
import com.onizuka.cfguide.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService {

    private final HTTPUtil httpUtil;

    @Autowired
    UserService(HTTPUtil httpUtil) {
        this.httpUtil = httpUtil;
    }

    public UserSubmissionByDateResponse getSubmissionByDate(UserSubmissionByDateRequest userSubmissionByDateRequest) {
        String uri = getUserInfoFetchURI(userSubmissionByDateRequest);
        RestApiResponse httpResponse = httpUtil.get(uri, SubmissionList.class);
        SubmissionList result = (SubmissionList) httpResponse.getResponseBody();

        long days = 0L;
        long epochSecond = TimeUtil.getEpochBeforeNDays(days);
        ArrayList<Long> countArray = new ArrayList<>();
        long cnt = 0;
        int totalSolveCount = 0;

        int i = 0;
        while(i < result.getResult().size()) {
            Submission submission = result.getResult().get(i);
            if (Boolean.TRUE
                    .equals(TimeUtil.isSameDay(submission.getCreationTimeSeconds() * 1000, epochSecond * 1000))) {
                cnt++;
                totalSolveCount++;
                i++;
            }
            else {
                countArray.add(cnt);
                days++;
                if(days > userSubmissionByDateRequest.getNoOfDays()) {
                    break;
                }
                cnt = 0;
                epochSecond = TimeUtil.getEpochBeforeNDays(days);
            }
        }
        return new UserSubmissionByDateResponse((long) totalSolveCount, countArray);
    }

    private String getUserInfoFetchURI(UserSubmissionByDateRequest userSubmissionByDateRequest) {
        return String.format("https://codeforces.com/api/user.status?handle=%s&from=1&count=100",
                userSubmissionByDateRequest.getHandle());
    }
}
