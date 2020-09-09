package com.onizuka.cfguide.service;

import com.onizuka.cfguide.dto.RestApiResponse;
import com.onizuka.cfguide.dto.SingleDaySubmission;
import com.onizuka.cfguide.dto.UserSubmissionByDateRequest;
import com.onizuka.cfguide.dto.UserSubmissionByDateResponse;
import com.onizuka.cfguide.model.Submission;
import com.onizuka.cfguide.model.SubmissionList;
import com.onizuka.cfguide.util.HTTPUtil;
import com.onizuka.cfguide.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
        ArrayList<SingleDaySubmission> countArray = new ArrayList<>();
        SingleDaySubmission singleDaySubmission = new SingleDaySubmission();
        singleDaySubmission.setDate(TimeUtil.getStringfromEpoch(epochSecond * 1000));
        int totalSubmissionCount = 0;
        Set<String> acIds = new HashSet<>();

        int i = 0;
        while(i < result.getResult().size()) {
            Submission submission = result.getResult().get(i);
            if (Boolean.TRUE
                    .equals(TimeUtil.isSameDay(submission.getCreationTimeSeconds() * 1000, epochSecond * 1000))) {

                if(submission.getVerdict().equals("OK")) {
                    singleDaySubmission.setAcCount(singleDaySubmission.getAcCount() + 1);
                    acIds.add(submission.getProblem().getContestId() + "-" + submission.getProblem().getIndex());
                } else if(submission.getVerdict().equals("WRONG_ANSWER")) {
                    singleDaySubmission.setWaCount(singleDaySubmission.getWaCount() + 1);
                } else if(submission.getVerdict().equals("TIME_LIMIT_EXCEEDED")) {
                    singleDaySubmission.setTleCount(singleDaySubmission.getTleCount() + 1);
                } else if(submission.getVerdict().equals("MEMORY_LIMIT_EXCEEDED")) {
                    singleDaySubmission.setMleCount(singleDaySubmission.getMleCount() + 1);
                }
                singleDaySubmission.setTotalSubmission(singleDaySubmission.getTotalSubmission() + 1);
                totalSubmissionCount++;
                i++;
            }
            else {
                singleDaySubmission.setUniqueAcCount(acIds.size());
                countArray.add(singleDaySubmission);
                days++;
                if(days > userSubmissionByDateRequest.getNoOfDays()) {
                    break;
                }
                acIds.clear();
                epochSecond = TimeUtil.getEpochBeforeNDays(days);
                singleDaySubmission = new SingleDaySubmission();
                singleDaySubmission.setDate(TimeUtil.getStringfromEpoch(epochSecond * 1000));
            }
        }
        return new UserSubmissionByDateResponse((long) totalSubmissionCount, countArray);
    }

    private String getUserInfoFetchURI(UserSubmissionByDateRequest userSubmissionByDateRequest) {
        return String.format("https://codeforces.com/api/user.status?handle=%s&from=1&count=100",
                userSubmissionByDateRequest.getHandle());
    }
}
