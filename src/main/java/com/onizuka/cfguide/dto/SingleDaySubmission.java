package com.onizuka.cfguide.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SingleDaySubmission {
    String date;
    long totalSubmission;
    long acCount;
    long waCount;
    long tleCount;
    long mleCount;
}
