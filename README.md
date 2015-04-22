A simple Twitter client that supports viewing a Twitter timeline and composing a new tweet.

User stories:
=============

Required:
--------
* [x] User can sign in to Twitter using OAuth login
* [x] User can view the tweets from their home timeline
 * [x] User should be displayed the username, name, and body for each tweet
 * [x] User should be displayed the relative timestamp for each tweet "8m", "7h"
 * [x] User can view more tweets as they scroll with infinite pagination
 * [x] **Optional:** Links in tweets are clickable and will launch the web browser
  * [x] Twitter links are expanded and displayed
* [x] User can compose a new tweet
 * [x] User can click a “Compose” icon in the Action Bar on the top right
 * [x] User can then enter a new tweet and post this to twitter
 * [x] User is taken back to home timeline with new tweet visible in timeline
 * [x] **Optional:** User can see a counter with total number of characters left for tweet
* [x] User can switch between Timeline and Mention views using tabs.
 * [x] User can view their home timeline tweets.
 * [x] User can view the recent mentions of their username.
 * [x] User can scroll to bottom of either of these lists and new tweets will load ("infinite scroll")
* [x] User can navigate to view their own profile
 * [x] User can see picture, # of followers, # of following, and tweets on their profile.
* [x] User can click on the profile image in any tweet to see another user's profile.
 * [x] User can see picture, # of followers, # of following, and tweets of clicked user.
 * [x] Profile view should include that user's timeline

Advanced:
---------
* [x] **Advanced:** User can refresh tweets timeline by pulling down to refresh (i.e pull-to-refresh)
* [x] **Advanced:** User can open the twitter app offline and see last loaded tweets
 * [x] Tweets are persisted into sqlite and can be displayed from the local DB
* [x] **Advanced:** User can tap a tweet to display a "detailed" view of that tweet
* [x] **Advanced:** User can select "reply" from detail view to respond to a tweet
* [x] **Advanced:** Improve the user interface and theme the app to feel "twitter branded"
* [x] **Bonus:** User can see embedded image media within the tweet detail view
* [x] **Advanced:** Robust error handling, check if internet is available, handle error cases, network failures
* [x] **Advanced:** User can search for tweets matching a particular query and see results
