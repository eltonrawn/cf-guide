package com.onizuka.cfguide.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ContestList {
    String status;
    List<Contest> result;
}
