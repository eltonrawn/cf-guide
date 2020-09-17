package com.onizuka.cfguide.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Contest {
    Long id;
    String cgType;
    String name;
    String type;
    String phase;
    Boolean frozen;
    Long durationSeconds;
    Long startTimeSeconds;
    Long relativeTimeSeconds;
}
