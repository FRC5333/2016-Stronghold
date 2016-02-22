var loc = window.location, new_uri;
new_uri = "ws:"
new_uri += "//" + loc.host;
new_uri += "/socket/training";

var socket = new WebSocket(new_uri);
socket.onmessage = function(event) {
    shooter_distance.value = event.data
};

function training_test() {
    var o = { action: "test" };
    o.top = Number(top_spd.value);
    o.btm = Number(btm_spd.value);
    socket.send(JSON.stringify(o));
}

function training_register() {
    var o = { action: "register" };
    o.top = Number(top_spd.value);
    o.btm = Number(btm_spd.value);
    socket.send(JSON.stringify(o));
}

function training_clear() {
    socket.send(JSON.stringify({ action: "clear" }));
}