package edu.rice.comp504.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.eclipse.jetty.websocket.api.Session;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static spark.Spark.*;
import static j2html.TagCreator.*;

/**
 * The chat app controller communicates with all the clients on the web socket.
 */
public class ChatAppController {
    static Map<Session,String> userNameMap = new ConcurrentHashMap<>();
    static int nextUserId = 1;

    /**
     * Chat App entry point.
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        Gson gson = new Gson();
        port(getHerokuAssignedPort());
        staticFiles.location("/public");

        webSocket("/chatapp", WebSocketController.class);
        init();

        post("/create-room", (request, response) -> {
            System.out.println(urlDecodeAndParse(request.body()));
            return gson.toJson("");
        });
    }

    /**
     * parse the url string from application/x-www-form-urlencoded, i.e., key1=val1&key2=val2 with url encoding
     * @param url the post/get body which is encoded by application/x-www-form-urlencoded
     * @return key-value map
     */
    public static Map<String, List<String>> urlDecodeAndParse(String url) {
        // for example, body is
        // name=Try+ro+have&introduction=siii+hhhs++++!&school=rice&location=africa%2Ceurope%2Casia&lowestAge=1&highestAge=33
        if (url.isEmpty()) {
            return Collections.emptyMap();
        }
        return Arrays.stream(url.split("&"))
                .map(ChatAppController::splitQueryParameter)
                .collect(Collectors.groupingBy(AbstractMap.SimpleImmutableEntry::getKey, LinkedHashMap::new, Collectors.mapping(Map.Entry::getValue, toList())));
    }

    private static AbstractMap.SimpleImmutableEntry<String, String> splitQueryParameter(String it) {
        final int idx = it.indexOf("=");
        String key = idx > 0 ? it.substring(0, idx) : it;
        try {
            key = java.net.URLDecoder.decode(key, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String value = null;
        try {
            value = idx > 0 && it.length() > idx + 1 ? java.net.URLDecoder.decode(it.substring(idx + 1), StandardCharsets.UTF_8.name()) : null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new AbstractMap.SimpleImmutableEntry<>(key, value);
    }

    /**
     * Broadcast message to all users.
     * @param sender  The message sender.
     * @param message The message.
     */
    static void broadcastMessage(String sender, String message) {
        userNameMap.keySet().forEach(session -> {
            try {
                JsonObject jo = new JsonObject();
                // TODO add a JSON object property that has a key (userMessage) and a j2html paragraph value
                jo.addProperty("userMessage", p(sender + " says:" + message).render());
                session.getRemote().sendString(String.valueOf(jo));
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }

    /**
     * Get the heroku assigned port number.
     * @return The heroku assigned port number
     */
    private static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; // return default port if heroku-port isn't set.
    }
}
