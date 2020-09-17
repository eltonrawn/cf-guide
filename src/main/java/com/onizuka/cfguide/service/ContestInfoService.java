package com.onizuka.cfguide.service;

import com.onizuka.cfguide.model.ContestList;
import com.onizuka.cfguide.util.CGResourceUtil;
import com.onizuka.cfguide.util.HTTPUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ContestInfoService {
    private final String defaultContestType;
    private final HTTPUtil httpUtil;
    private final CGResourceUtil cgResourceUtil;
    Map<Long, String> contestTypeById;
    Map<String, String> cgContestTypeByName;

    public ContestInfoService(HTTPUtil httpUtil, CGResourceUtil cgResourceUtil,
                              @Value("${cg.config.defaultContestType}") String defaultContestType) {
        this.httpUtil = httpUtil;
        this.cgResourceUtil = cgResourceUtil;
        this.defaultContestType = defaultContestType;
        this.cgContestTypeByName = new HashMap<>();
        this.contestTypeById = new HashMap<>();
    }

    private void updateContestList() {
        if (CollectionUtils.isEmpty(cgContestTypeByName)) {
            init();
        }
        contestTypeById = getContestList().getResult().stream()
                .map(contest -> Pair.of(contest.getId(), getCGContestType(contest.getName())))
                .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
    }

    private ContestList getContestList() {
        var restApiResponse = httpUtil.get("https://codeforces.com/api/contest.list", ContestList.class);
        return (ContestList) restApiResponse.getResponseBody();
    }

    private String getCGContestType(String contestName) {
        return cgContestTypeByName.entrySet().stream()
                .filter(entry -> contestName.contains(entry.getKey()))
                .findFirst()
                .map(Map.Entry::getValue).orElse(this.defaultContestType);
    }

    public String getContestTypeById(long contestId) {
        if (contestTypeById.containsKey(contestId)) {
            return contestTypeById.get(contestId);
        }
        updateContestList();
        return contestTypeById.getOrDefault(contestId, this.defaultContestType);
    }

    // Casting is always safe
    @SuppressWarnings("unchecked")
    @PostConstruct
    void init() {
        ((Map<String, List<String>>) cgResourceUtil.getResourceAsObject("config/contestNameKeys.json")).
                forEach((key, values) -> values.forEach(val -> cgContestTypeByName.put(val, key)));
    }
}
