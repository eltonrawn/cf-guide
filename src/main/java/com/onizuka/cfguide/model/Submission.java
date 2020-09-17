package com.onizuka.cfguide.model;

import lombok.Data;

@Data
public class Submission {
    Long id;
    Long contestId;
    Long creationTimeSeconds;
    Long relativeTimeSeconds;
    Problem problem;
    Party author;
    String programmingLanguage;
    String verdict;
    String testset;
    Long passedTestCount;
    Long timeConsumedMillis;
    Long memoryConsumedBytes;
    Double points;
    String date;
    String problemType;
}
