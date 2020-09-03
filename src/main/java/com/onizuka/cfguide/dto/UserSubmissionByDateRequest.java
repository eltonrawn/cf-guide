package com.onizuka.cfguide.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSubmissionByDateRequest {
    String handle;
    Long noOfDays;
}
