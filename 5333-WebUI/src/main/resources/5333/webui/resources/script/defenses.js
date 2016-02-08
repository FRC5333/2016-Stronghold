var defenses_sorter = null;

var category_map = {
    "a": ["cheval", "portculus"],
    "b": ["moat", "ramparts"],
    "c": ["sally", "drawbridge"],
    "d": ["rock", "rough"]
}

window.onload = function() {
    defenses_sorter = document.getElementById("defense_sorter").children;
}

function swap_left(element) {
    var actualNode = element.parentNode.parentNode;
    var index = Array.prototype.indexOf.call(actualNode.parentNode.children, actualNode);
    actualNode.parentNode.insertBefore(actualNode, actualNode.parentNode.children[index-1]);
}

function swap_right(element) {
    var actualNode = element.parentNode.parentNode;
    var index = Array.prototype.indexOf.call(actualNode.parentNode.children, actualNode);
    actualNode.parentNode.insertBefore(actualNode, actualNode.parentNode.children[index+2]);
}

function switch_defense(element, category) {
    var imgNode = element.parentNode.parentNode.children[0].children[0];
    var active_defense = imgNode.getAttribute("data-active-img");
    var arrMap = category_map[category];
    var newActive = arrMap[0];
    for (var i = 0; i < arrMap.length; i++) {
        if (arrMap[i] != active_defense) newActive = arrMap[i]; 
    }
    imgNode.src="/png/" + newActive + "_" + category + ".png";
    imgNode.setAttribute("data-active-img", newActive);
    imgNode.parentNode.parentNode.setAttribute("data-active", newActive);
}

function get_defenses() {
    var arr = [];
    for (var i = 0; i < defenses_sorter.length; i++) {
        var child = defenses_sorter[i];
        arr[i] = child.getAttribute("data-active");
    }
    return arr;
}

function submit_defenses() {
    var def = get_defenses();
    httpGet("/defenses/set/" + def[0] + "/" + def[1] + "/" + def[2] + "/" + def[3]);
}