'use strict';

const webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/chatapp");

/**
 * Entry point into chat room
 */
window.onload = function() {

    webSocket.onclose =   ()    => alert("WebSocket connection closed");
    webSocket.onmessage = (msg) => updateChatRoom(msg);
    $("#btn-msg").click(()      => sendMessage($("#message").val()));

    // init all the "join rooms", "your rooms" as hidden
    $(".chat-app-blocks").hide();

    // nav-item click event
    $("#join-rooms-nav-item").click(function () {
        $(".chat-app-blocks").hide();
        $("#join-rooms").show();
    });
    $("#your-rooms-nav-item").click(function () {
        $(".chat-app-blocks").hide();
        $("#your-rooms").show();
    });
    $("#create-rooms-nav-item").click(function () {
        $(".chat-app-blocks").hide();
        $("#create-rooms").show();
    });

    // submit creation room
    $("#room-creation-form").submit(function () {
        var room = new Object();
        room.name = $("#roomName").val();
        room.introduction = $("#introduction").val();
        room.school = $("#school").val();
        // console.log($(".location_checkbox:checked").val());
        room.location = $(".location_checkbox:checked").map(function() {return this.value;}).get(); // Array
        room.lowestAge = $("#lowest-age").val();
        room.highestAge = $("#highest-age").val();
        // parse the form
        // alert(room);
        console.log(room);
        createRoom(room);
    })
};

/**
 * create room
 * @param msg  The message to send to the server.
 */
function createRoom(room) {
    $.post("create-room", room, function (data) {

    }, "json")
}

/**
 * Send a message to the server.
 * @param msg  The message to send to the server.
 */
function sendMessage(msg) {
    if (msg !== "") {
        webSocket.send(msg);
        $("#message").val("");
    }
}

/**
 * Update the chat room with a message.
 * @param message  The message to update the chat room with.
 */
function updateChatRoom(message) {
    // TODO convert the data to JSON and append the message to the chat area
    let data = JSON.parse(message.data);

    console.log(data.userMessage);
    $("#chatArea").append(data.userMessage);
}
