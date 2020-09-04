package com.onizuka.cfguide.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSubmissionByDateResponse {
    Long totalSolveCount;
    ArrayList<Long> countAra = new ArrayList<>();
}
