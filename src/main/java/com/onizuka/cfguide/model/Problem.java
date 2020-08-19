package com.onizuka.cfguide.model;

import lombok.Data;

import java.util.List;

@Data
public class Problem {
    Long contestId;
    String problemsetName;
    String index;
    String name;
    String type;
    Double points;
    Long rating;
    List<String> tags;
}
