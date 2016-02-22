function httpGet(theUrl) {
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.open( "GET", theUrl, false );
    xmlHttp.send( null );
    return xmlHttp.responseText;
}

function textAreaKeyDown(e) {
    if(e.keyCode === 9) {
        var start = this.selectionStart;
        var end = this.selectionEnd;

        var target = e.target;
        var value = target.value;

        target.value = value.substring(0, start)
                    + "\t"
                    + value.substring(end);

        this.selectionStart = this.selectionEnd = start + 1;
        e.preventDefault();
    }
}

function round2(num) {
    return Math.round(num * 100) / 100;
}