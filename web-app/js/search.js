function updateAddressBar(href) {
	var q = $("#q").val();
	window.history.pushState({}, "GitHub Repositories Viewer: search results for \"" + q + "\"", href + "?q=" + q)
}

function showError() {
	$("#results").html("<p>Error while fetching the results page.</p>");
}

function showSpinner() {
	$("#spinner").show();
	$("input").attr("readonly", "readonly");
}

function hideSpinner() {
	$("#spinner").hide();
	$("input").removeAttr("readonly");
}
