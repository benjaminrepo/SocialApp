# SocialApp
 
#### Authentiation For all the URL 
In the auth filter, verify user authenticated based on the http only cookie
   * If authenticated 
      *  redirect to /home
   * If unauthenticated
     * redirect to /login
   *  If any error to verify the authentication(like twitter user id not valid)
      * redirect to /logout and /login page


#### login page
* if authenticated 
  * redirect to /home page
* If anauthenticated
  *  redirect to twitter login page, 
     *  once logged in twitter will redirect to permission page
     *  Once grated permission redirct to applicaiton callback url
  *  callback url called from twitter.
  *  user infomation and twitter4j object stored as byte array to be used for scheduler.

#### scheduler
  *  On server start spring scheduler will be started
  *  Based on the automic boolean pause and resume are implemented
     *  If scheduler running:
         *  pull user infomation and user task added to the thread pool executer
     *  Executer will run the user tasks based on the configured available threads.
     *  user taks fetch the latest 20 tweets from twitter and update to the local database.
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
*  Indexing needs to be done in the database based on the column that the user does most to query.
*  Application authentication needs to be migrated to spring security. 
   *  Or userid to be encrypted to store in the http only cookie
*  UI needs to be completed.
*  server push notification to be done. To pull after the sceduler executed
*  tweet and user mapping to be changed to many to many
*  new mapping to be created to support home timeline tweets
*  user query object needs to be added.

