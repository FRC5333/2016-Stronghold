var loc = window.location, new_uri;
new_uri = "ws:"
new_uri += "//" + loc.host;
new_uri += "/socket/readout";

var socket = new WebSocket(new_uri);
socket.onmessage = function(event) {
    var msg = JSON.parse(event.data);
    var table = document.getElementById("readout_table").getElementsByTagName('tbody')[0];
    for (var key in msg) {
        if (!msg.hasOwnProperty(key)) continue;
        var value = msg[key];

        var eles = table.getElementsByClassName("__name__" + key);
        if (eles.length == 0) {
            var row = table.insertRow(table.rows.length);
            row.className = "__name__" + key;
            row.insertCell(0).innerHTML = key;
            row.insertCell(1).innerHTML = value;
        } else {
            eles[0].cells[1].innerHTML = value;
        }
    }
};