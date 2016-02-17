var loc = window.location, new_uri;
new_uri = "ws:"
new_uri += "//" + loc.host;
new_uri += "/socket/config";

var socket = new WebSocket(new_uri);
socket.onmessage = function(event) {
    document.getElementById("config_area").value = event.data;
}

socket.onopen = function(event) {
    socket.send("initial");
}

function updateConfig() {
    var json = document.getElementById("config_area").value;
    socket.send(json);
}