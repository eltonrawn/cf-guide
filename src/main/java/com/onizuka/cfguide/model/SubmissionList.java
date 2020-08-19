package com.onizuka.cfguide.model;

import lombok.Data;

import java.util.List;

@Data
public class SubmissionList {
    String status;
    List<Submission> result;
}
