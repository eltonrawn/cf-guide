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
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
        List<Submission> result = ((SubmissionList) httpResponse.getResponseBody()).getResult();

        Map<String, Long> submissionCount = filteredSubmissionVerdictCount(result, p -> true);
        Map<String, Long> acCount = filteredSubmissionVerdictCount(result, p -> p.getVerdict().equals("OK"));
        Map<String, Long> waCount = filteredSubmissionVerdictCount(result, p -> p.getVerdict().equals("WRONG_ANSWER"));
        Map<String, Long> mleCount = filteredSubmissionVerdictCount(result, p -> p.getVerdict().equals("MEMORY_LIMIT_EXCEEDED"));
        Map<String, Long> tleCount = filteredSubmissionVerdictCount(result, p -> p.getVerdict().equals("TIME_LIMIT_EXCEEDED"));
        Map<String, Long> uniqueAcCount = filteredUniqueSubmissionVerdictCount(result, p -> p.getVerdict().equals("OK"));

        ArrayList<SingleDaySubmission> singleDaySubmissions = new ArrayList<>();
        long totalSubmissionCount = 0;

        for (int days = 0; days < userSubmissionByDateRequest.getNoOfDays(); days++) {
            String date = TimeUtil.getStringfromEpoch(TimeUtil.getEpochBeforeNDays(days) * 1000);
            SingleDaySubmission singleDaySubmission = new SingleDaySubmission();
            singleDaySubmission.setDate(date);

            singleDaySubmission.setTotalSubmission(submissionCount.getOrDefault(date, 0L));
            singleDaySubmission.setAcCount(acCount.getOrDefault(date, 0L));
            singleDaySubmission.setWaCount(waCount.getOrDefault(date, 0L));
            singleDaySubmission.setMleCount(mleCount.getOrDefault(date, 0L));
            singleDaySubmission.setTleCount(tleCount.getOrDefault(date, 0L));
            singleDaySubmission.setUniqueAcCount(uniqueAcCount.getOrDefault(date, 0L));

            singleDaySubmissions.add(singleDaySubmission);
            totalSubmissionCount += submissionCount.getOrDefault(date, 0L);
        }

        return new UserSubmissionByDateResponse(totalSubmissionCount, singleDaySubmissions);
    }

    Map<String, Long> filteredSubmissionVerdictCount(List<Submission> result, Predicate<Submission> predicate) {
        return result
                .stream()
                .filter(predicate)
                .collect((Collectors
                        .groupingBy(p -> TimeUtil.getStringfromEpoch(p.getCreationTimeSeconds() * 1000),
                                Collectors.counting())
                ));
    }

    Map<String, Long> filteredUniqueSubmissionVerdictCount(List<Submission> result, Predicate<Submission> predicate) {
        return result
                .stream()
                .filter(predicate)
                .collect((Collectors
                        .groupingBy(p -> TimeUtil.getStringfromEpoch(p.getCreationTimeSeconds() * 1000),
                                Collectors.collectingAndThen(Collectors.mapping(p -> String.format(
                                        "%s-%s", p.getProblem().getContestId(), p.getProblem().getIndex()),
                                        Collectors.toSet()), p -> (long) (p.size()))
                        )));


    }

    String getUserInfoFetchURI(UserSubmissionByDateRequest userSubmissionByDateRequest) {
        return String.format("https://codeforces.com/api/user.status?handle=%s&from=1&count=100",
                userSubmissionByDateRequest.getHandle());
    }
}
