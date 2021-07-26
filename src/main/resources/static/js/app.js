/**
 * 
 */

var get = function(url, callback) {
	$.ajax({ "url": url }).then(callback)
}

$(document).ready(function() {

	showRecentTweets();
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
		template.find("#tweet").text(tweetObj.text);
		template.find("#displayDate").text(tweetObj.createdAt);

	}

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
	$("#recentView").show();
	get("/twitter/dataAccess/getRecent", tweetResponseHandler);
}

function hideAll() {
	$("#recentView").hide();
	$("#schView").hide();
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
