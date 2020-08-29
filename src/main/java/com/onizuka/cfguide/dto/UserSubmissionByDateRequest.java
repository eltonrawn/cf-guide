package com.onizuka.cfguide.dto;

import lombok.Data;

@Data
public class UserSubmissionByDateRequest {
    String handle;
    Long noOfDays;
}
