package edu.rice.comp504.model;

import java.util.function.Predicate;

public class ChatRoom {
    private String name;
    private String introduction;

    private Predicate<String> inSchoolRange;
    private Predicate<String> inLocationRange;
    private Predicate<Integer> inAgeRange;

    public ChatRoom(String name, String introduction) {
        this.name = name;
        this.introduction = introduction;
    }

    public void setInSchoolRange(Predicate<String> inSchoolRange) {
        this.inSchoolRange = inSchoolRange;
    }

    public void setInLocationRange(Predicate<String> inLocationRange) {
        this.inLocationRange = inLocationRange;
    }

    public void setInAgeRange(Predicate<Integer> inAgeRange) {
        this.inAgeRange = inAgeRange;
    }

}
