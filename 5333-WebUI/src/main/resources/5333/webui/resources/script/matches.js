function submit_match() {
    var form_elements = document.getElementById("match_form").elements;
    
    var selected_match_type = form_elements["matchType"].value;
    var selected_match_number = form_elements["matchNumber"].value;
    
    httpGet("/matches/set/" + selected_match_type + "/" + selected_match_number);
    
    return false;
}

function submit_placement() {
    var placement_id = document.getElementById("placement");
    httpGet("/placement/set/" + placement_id.value);

    return false;
}