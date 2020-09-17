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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class UserService {

    private final HTTPUtil httpUtil;
    private final ContestInfoService contestInfoService;

    @Autowired
    UserService(HTTPUtil httpUtil, ContestInfoService contestInfoService) {
        this.httpUtil = httpUtil;
        this.contestInfoService = contestInfoService;
    }

    public UserSubmissionByDateResponse getSubmissionByDate(UserSubmissionByDateRequest userSubmissionByDateRequest) {
        List<Submission> submissions = getUserSubmissions(userSubmissionByDateRequest);
        String validity = TimeUtil.getStringfromEpoch(
                TimeUtil.getEpochBeforeNDays(userSubmissionByDateRequest.getNoOfDays()) * 1000
        );

        submissions = submissions.stream()
                .filter(submission -> validity.compareTo(submission.getDate()) <= 0)
                .collect(Collectors.toList());

        Map<String, Integer> solveCountByType = submissions.stream()
                .filter(submission -> "OK".equals(submission.getVerdict()))
                .collect(Collectors.groupingBy(Submission::getProblemType))
                .entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().size()));

        List<SingleDaySubmission> singleDaySubmissions =
                getSingleDaySubmissions(submissions, Math.toIntExact(userSubmissionByDateRequest.getNoOfDays()));

        return UserSubmissionByDateResponse.builder()
                .totalSubmissionCount(submissions.size())
                .countAra(singleDaySubmissions)
                .solveCountByType(solveCountByType)
                .build();
    }

    private List<SingleDaySubmission> getSingleDaySubmissions(List<Submission> submissions, int days) {
        var cgSubByDate = submissions.stream().collect(Collectors.groupingBy(Submission::getDate));
        var acCountByDate = new HashMap<String, Long>();
        var totSubCountByDate = new HashMap<String, Long>();

        cgSubByDate.forEach((key, value) -> {
            var acCount = value.stream().filter(submission -> "OK".equals(submission.getVerdict())).count();
            var total = value.size();
            acCountByDate.put(key, acCount);
            totSubCountByDate.put(key, (long) total);
        });

        return IntStream.range(0, days)
                .mapToObj(day -> {
                    String date = TimeUtil.getStringfromEpoch(TimeUtil.getEpochBeforeNDays(day) * 1000);
                    return SingleDaySubmission.builder()
                            .uniqueAcCount(acCountByDate.getOrDefault(date, (long) 0))
                            .totalSubmission(totSubCountByDate.getOrDefault(date, (long) 0))
                            .date(date)
                            .build();
                })
                .collect(Collectors.toList());
    }

    private List<Submission> getUserSubmissions(UserSubmissionByDateRequest userSubmissionByDateRequest) {
        RestApiResponse response = httpUtil.get(getUserInfoFetchURI(userSubmissionByDateRequest), SubmissionList.class);
        var submissions = ((SubmissionList) response.getResponseBody()).getResult();
        submissions.forEach(submission -> {
            var date = TimeUtil.getStringfromEpoch(submission.getCreationTimeSeconds() * 1000);
            submission.setDate(date);
            submission.setProblemType(getProblemType(submission));
        });
        return submissions;
    }

    private String getProblemType(Submission submission) {
        return contestInfoService.getContestTypeById(submission.getContestId()) + "-" + submission.getProblem().getIndex();
    }

    private String getUserInfoFetchURI(UserSubmissionByDateRequest userSubmissionByDateRequest) {
        return String.format("https://codeforces.com/api/user.status?handle=%s&from=1&count=100",
                userSubmissionByDateRequest.getHandle());
    }
}
