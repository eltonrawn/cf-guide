package com.onizuka.cfguide.dto;

import lombok.Builder;
import lombok.Data;

// TODO uniqueAcCount is false naming. kept it to not break front end code
@Builder
@Data
public class SingleDaySubmission {
    String date;
    Long totalSubmission;
    Long uniqueAcCount;
}
