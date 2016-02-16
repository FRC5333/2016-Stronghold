var loc = window.location, new_uri;
new_uri = "ws:"
new_uri += "//" + loc.host;
new_uri += "/socket/readout";

var socket = new WebSocket(new_uri);
socket.onmessage = function(event) {
    var msg = JSON.parse(event.data);
    document.getElementById("shooter_top_throttle").innerHTML = msg["shooter_top_throttle"];
    document.getElementById("shooter_btm_throttle").innerHTML = msg["shooter_btm_throttle"];
}