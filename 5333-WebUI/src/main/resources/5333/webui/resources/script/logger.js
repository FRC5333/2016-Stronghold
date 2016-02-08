function scroll_logger_field() {
    var area = document.getElementById("loggerField");
    area.scrollTop = area.scrollHeight;
}

var loc = window.location, new_uri;
new_uri = "ws:"
new_uri += "//" + loc.host;
new_uri += "/socket/logger";

var socket = new WebSocket(new_uri);
socket.onmessage = function(event) {
    document.getElementById("loggerField").innerHTML += event.data + "\n";
    scroll_logger_field();
}

function submit_command(ev) {
    if (ev.keyCode == 13) {
        var field = document.getElementById("commandField");
        socket.send(field.value);
        field.value = "";
    }
}