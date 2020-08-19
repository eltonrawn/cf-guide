package com.onizuka.cfguide.model;

import lombok.Data;

import java.util.List;

@Data
public class Party {
    Long contestId;
    List<Member> members;
    String participantType;
    Long teamId;
    String teamName;
    Boolean ghost;
    Long room;
    Long startTimeSeconds;
}
