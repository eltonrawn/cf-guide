package com.onizuka.cfguide.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class UserSubmissionByDateResponse {
    int totalSubmissionCount;
    List<SingleDaySubmission> countAra;
    Map<String, Integer> solveCountByType;
}
