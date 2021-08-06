/**
 * 
 */

var get = function(url, callback, data) {
	$("#showLoading").show();
	$.ajax({ "url": url, "data": data })
		.then(callback)
		.fail(function(data) {
			alert("Error "+data);
		})
}

$(document).ready(function() {
	SearchHandler().initSearch();
	SearchHandler().onClickSearch();
	updateSchedulerStatus();
});


function tweetResponseHandler(response) {
	function getUserInfo(userObj) {
		if (userObj == null) return;
		return userObj.name + " (@" + userObj.screenName + ")";
	}

	function updateUserMentions(template, userMen) {
		var mentions = userMen.map(function(obj) { return obj.name + " @" + obj.screenName })
		template.find("#tweet_mentions").text(mentions)
	}

	function updateTweetInfo(template, tweetObj) {

		if (tweetObj.quotedStatus) {
			template.find("#quoteUser")
				.text("[Quoted]" + getUserInfo(tweetObj.quotedStatus.user));
			updateTweetInfo(template, tweetObj.quotedStatus);
			return;
		}
		if (tweetObj.retweetedStatus) {
			template.find("#reTweetUser")
				.text("[Retweeted]" + getUserInfo(tweetObj.retweetedStatus.user));
			updateTweetInfo(template, tweetObj.retweetedStatus);
			return;
		}

		if (tweetObj.userMentionEntities.length) {
			updateUserMentions(template, tweetObj.userMentionEntities);
		}

		template.find("#reTweetCount").text(tweetObj.retweetCount);		
		template.find("#likesCount").text(tweetObj.favoriteCount);

		template.find("#likesCount").parent().attr('class', 
			tweetObj.favorited === true ? 'btn bg-primary': 'btn bg-secondary');
		template.find("#reTweetCount").parent().attr('class', 
			tweetObj.retweeted === true ? 'btn bg-primary': 'btn bg-secondary');



		template.find("#tweet").text(tweetObj.text);
		var jsdate = new Date(Date.parse(tweetObj.createdAt)).toISOString().slice(0, 19).replace('T', ' ');
		template.find("#displayDate").text(jsdate);

	}

	$("#showLoading").hide();
	$(".container-fluid .modal-content").remove();
	response.forEach(function(status) {

		var template = $("#tweetTemplate").clone(true);

		template[0].id = "";
		template[0].style.display = "flex";

		if (status.retweeted) {
			template[0].style.backgroundColor = "aliceblue";
		}
		updateTweetInfo(template, status);
		template.find("#userName")
			.text("[Tweeted] " + getUserInfo(status.user));


		$(".modal-dialog").append(template);


	})
}


function showRecentTweets() {
	hideAll();
	$(".recentView").show();
	get("/twitter/getRecent", tweetResponseHandler);
}


function SearchHandler() {
	var self = this;
	var searchStr = "";

	function _onTypeSearchText(event) {
		if (event.keyCode === 13) {
			_onClickSearch();
			return;
		}
		_updateFilter();
	}

	function _onClickSearch() {
		hideAll();
		$("#searchView").show();
		get("/twitter/search", tweetResponseHandler, getSearchQuery());
	}

	function _updateFilter() {
		var text = $("#search_text").val().trim();
		filterVal = $("#search_filter").val().split(",").map(val => val.trim().startsWith("search:") ? "" :val).toString()
		$("#search_filter").val(filterVal+" search:" + text);
	}
	function getSearchQuery() {

		var searchStr = "searchTweet=" + encodeURIComponent($("#search_filter").val());

		searchStr += "&orderBy=" + encodeURIComponent($("#search_orderBy").val().trim());
		searchStr += "&direction=" + encodeURIComponent($("#search_direction").val().trim());

		searchStr += "&page=" + encodeURIComponent($("#search_page").val().trim());
		searchStr += "&size=" + encodeURIComponent($("#search_size").val().trim());
		return searchStr;

	}

	function _resetSearch() {
		$("#search_orderBy").val("createdAt");
		$("#search_direction").val("asc");

		$("#search_page").val("0");
		$("#search_size").val("10");
	}


	function _initSearch() {
		if ($("#search_orderBy").val().trim() === "") {
			$("#search_orderBy").val("createdAt");
		}
		if ($("#search_direction").val().trim() === "") {
			$("#search_direction").val("desc");
		}
		if ($("#search_page").val().trim() === "") {
			$("#search_page").val("0");
		}
		if ($("#search_size").val().trim() === "") {
			$("#search_size").val("10");
		}
		if ($("#search_text").val().trim() === "") {
			$("#search_text").val("");
		}
		if ($("#search_filter").val().trim() === "") {
			$("#search_filter").val("retweetCount>0, retweetCount<10000, isRetweeted:, search:");
		}
	}
	return {
		onTypeSearchText: _onTypeSearchText,
		onClickSearch: _onClickSearch,
		initSearch: _initSearch
	}

}
function hideAll() {
	$(".recentView").hide();
	$("#schView").hide();
	$(".searchView").hide();
}

function updateSchedulerStatus() {
	get("/scheduler/isRunning", function(res) {
		if (res) {
			$("#schStatus").text("Scheduler Running");
			$("#schStatus")[0].className = "btn btn-success";
		} else {
			$("#schStatus")[0].className = "btn btn-danger";
			$("#schStatus").text("Scheduler Stoped");
		}
	});
}
