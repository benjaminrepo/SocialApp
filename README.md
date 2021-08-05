# SocialApp
 
#### Authentication For all the URLs 
In the auth filter, user authentication is verified based on the http only cookie
   * If authenticated 
      *  It is redirected to /home page
   * If unauthenticated
     * It is redirected to /login page
   *  If any error occurs in authentication (like twitter user invalid)
      * It is redirected to /logout and then to /login page


#### login page
* If authenticated 
  * It is redirected to /home page
* If unauthenticated
  *  It is redirected to twitter login page, 
     *  once logged in to the twitter, it will redirect to twiter authorize page
     *  Once authorized, twitter will redirect to application callback url.
  *  callback url called from twitter.
  *  user infomation and twitter4j object stored as byte array to be used for scheduler.

#### scheduler
  *  Once the server is started, Spring scheduler will also get started. It will run periodically (every one min).
  *  Based on the automic boolean, pause and resume are implemented
     *  If scheduler is running (Periodically):
         *  Get user information
         *  user task created
         *  Added to the thread pool executer.
     *  Executer will run the user tasks based on the configured available threads.
     *  user task fetches the latest 20 tweets from twitter and updates to the local database.
  *  One time scheduler created to execute the user task for the logged in user.


#####  Example Search Query:
*  searchTweet=retweetCount>0,isFavorited:true,createdAt>2021-06-30,search:test
*  orderBy=createdAt&direction=desc
*  page=0&size=10

###### Operations
  * ":" operator can be used to search the text fields, boolean fileds
  * ">" or "<"  can be used in all date or number filds

###### keys :
*  search key to search on tweets and username
*  all the tweet properties can be used to perform search, filter, sort

##### sort
* All the tweet properies can be used
Example orderBy=createdAt&direction=desc

##### Pagination
page=0&size=10

 
#### Appliction APIs 

*  /twitter/getRecent: fetch the recent tweets for the logged in user
*  /twitter/search : to perform search

*  /scheduler/isRunning : get status of the scheduler
*  /scheduler/start : To start the scheduler
*  /scheduler/stop : To stop the schededuler
*  /scheduler/oneTimeSync : to sync last 20 tweets for logged in user


# Yet to do:
*  Indexing needs to be done in the database, based on the column that the user does most to query.
*  Application authentication needs to be migrated to spring security. 
   *  Or userid to be encrypted to store in the http only cookie
*  UI needs to be completed.
*  server push notification to be done, to pull the updated data.
*  tweet and user mapping to be changed to many to many
*  new mapping to be created to support home timeline tweets
*  user query object needs to be added.
*  spring scheduler intervals should be loaded from properties. And the same should be used to pull the tweets from the twitter server.

