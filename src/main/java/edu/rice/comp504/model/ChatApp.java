package edu.rice.comp504.model;

import java.util.*;
import java.util.function.Predicate;

public class ChatApp {
    Map<String, ChatRoom> chatRoomMap;
    Map<String, User> userMap;

    public ChatApp() {
        this.chatRoomMap = new HashMap<>();
        this.userMap = new HashMap<>();
    }

    public ChatRoom createRoom(Map<String, List<String>> request) {
        String name = request.get("name").get(0); // required field
        String intro = request.get("introduction").size() > 0 ? request.get("introduction").get(0) : "";
        ChatRoom room = new ChatRoom(name, intro);

        List<String> schools = request.get("school");
//        Predicate<String> inSchoolsList = (s) -> {
//
//        };
        List<String> locations = request.getOrDefault("location", new ArrayList<>());
        List<String> lowestAge = request.getOrDefault("lowestAge", new ArrayList<>());
        List<String> highestAge = request.getOrDefault("highestAge", new ArrayList<>());
        return null;
    }

}
