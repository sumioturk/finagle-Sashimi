# SASHIMI?
SASHIMI is a web service deleting your tweets periodically. Available on `http://SashimiQuality.com:9000`.
Current build status can be seen at `http://sashimiquality.com:8080/cc.xml` using `CCMenu.app`.
Deleting tweets is going to be much simpler than clicking 'delete' button on twitter.com 
or on whatever 3rd party twitter client you are currently using. You can do followings:

- Specify the lifetime of your tweets with MINUTES accuracy
- Delete tweets in the past
- On/Off automatic deletion
- Specify the tweets to be excluded from deletion
- Revert deletion(from v3.0)

# Why Sashimi
Sashimi is a sacred and traditional Japanese cuisine serving a fresh raw sliced fish and is thought as the most correct way to enjoy eating fish. Fishes must be extremely fresh in order to be served as Sashimi, and it has to be sliced in beautiful way; even one sloppy cut totally ruins entire Sashimi. It is not just a food on the plate. It is the art of the fragility and flow comes from its primitive magnificent appearance. 
Tweeting is like slicing Sashimi. You slice the bit of your thoughts, feelings and or sometimes even anger and transform it into ultimate versatile form of existence: that is, the words. Although the words may remain on piece of paper or surface of the rocks or even in electromagnetic field, after some time, it will die. Those words are absolutely NOT Sashimi Quality. The words has its life. In certain time, it will die as the words even if it can be seen on the paper. If it is dead, it needs to vanish: tweets need to be deleted. Under that philosophy. We provides you a handy way. Hope you like it.

# Sashimi v2.0 Deba APIs
Following APIs are supported.

- [Register new user](#register-new-user)
- [Login](#login)
- [Logout](#logout)
- [Get OAuth URL](#get-oauth-url)
- [Activate account](#activate-account)
- [Update Sashimi Quality](#update-sashimi-quality)
- [Update escape-term](#update-escape-term)
- [Toggle automatic deletion](#toggle-automatic-deletion)
- [Toggle 8th grader mode](#toggle-8th-grader-mode)
- [Tweet via Sashimi](#tweet-via-sashimi)

## Register new user 

Register a new user to `SASHIMI`. 

### Parameters
`POST` `http://sashimiquality.com:9000/join`
- `name`: a user name, `.+`
- `pass`: a password for the user, `.+`
- `sashimi`: sashimi quality, lifetime of your tweets in minutes, `[1-9]+[0-9]*`

### Response 
`user` model. 

    HTTP/1.1 200 OK
    Content-Type: application/json;charset=UTF-8
    Content-Length: 331

    {
       "id":"2",
       "twitter_id":"",
       "name":"name",
       "pass":"password",
       "last_tweet_id":"",
       "sashimi":"13",
       "escape_term":"",
       "is_premium":true,
       "is_active":false,
       "is_8th":false,
       "request_token":"4tv4uUVpyf2kAfCRvDTisar9OieZ2ENa1uvYdfBBjI",
       "request_token_secret":"kXQ5R8NSIeXlptZsUl9J3EiwGNvgXidvIdWwfM2sY",
       "access_token":"",
       "access_token_secret":""
    }

___
[[back to the list]](#apis)

## Login 

Login/Get session key. This API tries to set cookie. 
If this API is called from web-browser whose cookie is enabled, cookie, `SashimiSessionKey` will be set.

### Parameters 

`POST` `http://sashimiquality.com:9000/login`

- `name`: the name of user, `.+`
- `pass`: the password, `.+`

### Response 


	HTTP/1.1 200 OK
	Content-Type: application/json;charset=UTF-8
	Set-Cookie: SashimiSessionKey=w7QyH9b8Ke+zIK5LBdWnTmM7jcE=_ea3456f0-263f-11e2-b0c3-525405014302_1352006672863;
	Content-Length: 97

	{
	   "session_key":"w7QyH9b8Ke+zIK5LBdWnTmM7jcE=_ea3456f0-263f-11e2-b0c3-525405014302_13552683455"
	}

___

[[back to the list]](#apis)

## Logout

Logout/Expire session. 

### Parameters
`POST` `http://sashimiquality.com:9000/login`

- `key`: session key

### Response

	HTTP/1.1 200 OK
	Content-Type: application/json;charset=UTF-8
	Set-Cookie: SashimiSessionKey=foo;expires=Tue, 1-Jan-2000 00:00:00 GMT;
	Content-Length: 28

	{"message":"You Logged Out"}
	
___
[[back to the list]](#apis)
## Get OAuth URL 

Get twitter OAuth URL. Note that this URL expires after some time. 

### Parameters
`GET` `http://sashimiquality:9000/oauth_url`
- `key`: session key

### Response

	HTTP/1.1 200 OK
	Content-Type: application/json;charset=UTF-8
	Content-Length: 109

	{
		"auth_url":"https://api.twitter.com/oauth/authorize?oauth_token=4tv4uUVpyf2kAfCRvDTisar9OieZ2ENa1uvYdfBBjI"
	}

___
[[back to the list]](#apis)

## Activate account 

Activate account with `oauth_token` and `oauth_verifier` given by `oauth_url`.
### Parameters
`POST` `http://sashimiquality.com:9000/oauth`
- `oauth_token`: OAuth token
- `oauth_verifier`: OAuth Verifier
- `key`: session key

### Response

    HTTP/1.1 200 OK
    Content-Type: application/json;charset=UTF-8
    Content-Length: 542

    {
       "id":"2",
       "twitter_id":"43119438",
       "name":"name",
       "pass":"password",
       "last_tweet_id":"3222340382993333",
       "sashimi":"13",
       "escape_term":"",
       "is_premium":true,
       "is_active":true,
       "is_8th":false,
       "request_token":"4tv4uUVpyf2kAfCRvDTisar9OieZ2ENa1uvYdfBBjI",
       "request_token_secret":"kXQ5R8NSIeXlptZsUl9J3EiwGNvgXidvIdWwfM2sY",
       "access_token":"hgeBsfffVhdh52SBthwwdCXgdfbje",
       "access_token_secret":"43119438_sfSGw4423GsfGseEr4hsdGCCxXfgse634"
    }

___
[[back to the list]](#apis)

## Update Sashimi Quality 

Update sashimi quality a.k.a lifetime of your tweets.
### Parameters
`POST` `http://sashimiqulity.com:9000/update_sashimi`
- `sashimi`: new sashimi quality in minutes, `[1-9]+[0-9]`
- `key`: session key

### Response
`sashimi` field is updated.


    HTTP/1.1 200 OK
    Content-Type: application/json;charset=UTF-8
    Content-Length: 542

    {
       "id":"2",
       "twitter_id":"43119438",
       "name":"name",
       "pass":"password",
       "last_tweet_id":"3222340382993333",
       "sashimi":"90",
       "escape_term":"",
       "is_premium":true,
       "is_active":false,
       "is_8th":false,
       "request_token":"4tv4uUVpyf2kAfCRvDTisar9OieZ2ENa1uvYdfBBjI",
       "request_token_secret":"kXQ5R8NSIeXlptZsUl9J3EiwGNvgXidvIdWwfM2sY",
       "access_token":"hgeBsfffVhdh52SBthwwdCXgdfbje",
       "access_token_secret":"43119438_sfSGw4423GsfGseEr4hsdGCCxXfgse634"
    }

___
[[back to the list]](#apis)
## Update escape-term 
You can specify `Regular Expression`. Tweets contains matches is excluded from deletion. 
### Parameters
`POST` `http://sashimiqulity.com:9000/update_escape_term`
- `escape_term`: new sashimi quality in minutes, `Regular Expression`
- `key`: session key

### Response
`escape_term` field updated.


    HTTP/1.1 200 OK
    Content-Type: application/json;charset=UTF-8
    Content-Length: 542

    {
       "id":"2",
       "twitter_id":"43119438",
       "name":"name",
       "pass":"password",
       "last_tweet_id":"3222340382993333",
       "sashimi":"13",
       "escape_term":"((^Exclude)|(.+fuc.+))",
       "is_premium":true,
       "is_active":false,
       "is_8th":false,
       "request_token":"4tv4uUVpyf2kAfCRvDTisar9OieZ2ENa1uvYdfBBjI",
       "request_token_secret":"kXQ5R8NSIeXlptZsUl9J3EiwGNvgXidvIdWwfM2sY",
       "access_token":"hgeBsfffVhdh52SBthwwdCXgdfbje",
       "access_token_secret":"43119438_sfSGw4423GsfGseEr4hsdGCCxXfgse634"
    }



___
[[back to the list]](#apis)
## Toggle automatic deletion 
Toggle automatic deletion for tweets from now.
### Parameters
`GET` `http://sashimiqulity.com:9000/toggle`
- `key`: session key

### Response
`is_active` field updated.


    HTTP/1.1 200 OK
    Content-Type: application/json;charset=UTF-8
    Content-Length: 542

    {
       "id":"2",
       "twitter_id":"43119438",
       "name":"name",
       "pass":"password",
       "last_tweet_id":"3222340382993333",
       "sashimi":"13",
       "escape_term":"",
       "is_premium":true,
       "is_active":true,
       "is_8th":false,
       "request_token":"4tv4uUVpyf2kAfCRvDTisar9OieZ2ENa1uvYdfBBjI",
       "request_token_secret":"kXQ5R8NSIeXlptZsUl9J3EiwGNvgXidvIdWwfM2sY",
       "access_token":"hgeBsfffVhdh52SBthwwdCXgdfbje",
       "access_token_secret":"43119438_sfSGw4423GsfGseEr4hsdGCCxXfgse634"
    }



___
[[back to the list]](#apis)
## Toggle 8th grader mode 
Toggle automatic deletion of ALL tweets including tweets in the past.

### Parameters
- activate `GET` `http://sashimiqulity.com:9000/8`
- deactivate `GET` `http://sashimiqulity.com:9000/9`

___

-`key`: session key

### Response
`is8th` field updated.

    HTTP/1.1 200 OK
    Content-Type: application/json;charset=UTF-8
    Content-Length: 542

    {
       "id":"2",
       "twitter_id":"43119438",
       "name":"name",
       "pass":"password",
       "last_tweet_id":"3222340382993333",
       "sashimi":"13",
       "escape_term":"",
       "is_premium":true,
       "is_active":false,
       "is_8th":true,
       "request_token":"4tv4uUVpyf2kAfCRvDTisar9OieZ2ENa1uvYdfBBjI",
       "request_token_secret":"kXQ5R8NSIeXlptZsUl9J3EiwGNvgXidvIdWwfM2sY",
       "access_token":"hgeBsfffVhdh52SBthwwdCXgdfbje",
       "access_token_secret":"43119438_sfSGw4423GsfGseEr4hsdGCCxXfgse634"
    }



___
[[back to the list]](#apis)

## Tweet via Sashimi 
Tweet via Sashimi. 

### Parameters 
- `status`: status message, `.+`
- `key`: session key

### Response

    HTTP/1.1 200 OK
    Content-Type: application/json;charset=UTF-8
    Content-Length: 22

    {
	"in_reply_to_user_id_str": null,
	"retweet_count": 0,
	"contributors": null,
	"truncated": false,
	"text": "yello",
	"geo": null,
	"retweeted": false,
	"in_reply_to_screen_name": null,
	"id_str": "265026331409018880",
	"in_reply_to_user_id": null,
	"in_reply_to_status_id": null,
	"created_at": "Sun Nov 04 09:42:56 +0000 2012",
	"place": null,
	"user": {
		"id": 46686525,
		"profile_background_image_url": "http://a0.twimg.com/images/themes/theme1/bg.png",
		"screen_name": "sumioturk",
		"profile_link_color": "0084B4",
		"default_profile_image": false,
		"created_at": "Fri Jun 12 16:20:43 +0000 2009",
		"contributors_enabled": false,
		"friends_count": 100,
		"time_zone": "Tokyo",
		"favourites_count": 104,
		"utc_offset": 32400,
		"profile_use_background_image": true,
		"url": "http://sashimiquality.com/sashimi",
		"geo_enabled": false,
		"lang": "en",
		"profile_image_url": "http://a0.twimg.com/profile_images/2775421609/c5f11496176bd75fa6f8d64cf00f1a43_normal.jpeg",
		"profile_text_color": "333333",
		"name": "???? ??????",
		"profile_background_image_url_https": "https://si0.twimg.com/images/themes/theme1/bg.png",
		"description": "Hi, I'm Sashimi Quality.\r\n",
		"protected": false,
		"profile_sidebar_border_color": "C0DEED",
		"followers_count": 99,
		"is_translator": false,
		"default_profile": true,
		"profile_background_tile": false,
		"location": "Armteriyask, Russia",
		"profile_sidebar_fill_color": "DDEEF6",
		"following": false,
		"statuses_count": 93,
		"follow_request_sent": false,
		"listed_count": 13,
		"id_str": "46686525",
		"profile_background_color": "C0DEED",
		"notifications": false,
		"verified": false,
		"profile_image_url_https": "https://si0.twimg.com/profile_images/2775421609/c5f11496176bd75fa6f8d64cf00f1a43_normal.jpeg"
        },
	"coordinates": null,
	"source": "<a href=\"http://sashimiquality.net\" rel=\"nofollow\">SashimiQuality</a>",
	"in_reply_to_status_id_str": null,
	"favorited": false,
	"id": 265026331409018880
    }
   

___
[[back to the list]](#apis)

# Errors
Errors are represented by json: 

    {"error" : "Error Message"}

List of Errors:

- `404` "User Not Found"
- `409` "User Already Exists"
- `403` "Invalid Params"
- `403` "Authentication Required"
- `401` "Could Not Authorize"
- `403` "Expired Cookie"
- `402` "Premium Only"


# Footnote
SASHIMI is the only way that you can automatically delete your tweets in damn accurate timing. Fault tolerance architecture guarantees damn certainty of elimination of every single shit you tweeted. This is indeed the only way to keep your tweets fresh and Sashimi Quality. 

# Benchmark Results

`ab -n 10000 -c 500 -v 0 -C "SashimiSessionKey=SessionKey;" "http://sashimiquality.com:9000/8"`

    Concurrency Level:      500
    Time taken for tests:   11.020 seconds 
    Complete requests:      10000
    Failed requests:        0
    Write errors:           0
    Total transferred:      5190000 bytes
    HTML transferred:       4330000 bytes
    Requests per second:    907.44 [#/sec] (mean)
    Time per request:       551.001 [ms] (mean)
    Time per request:       1.102 [ms] (mean, across all concurrent requests)
    Transfer rate:          459.92 [Kbytes/sec] received
    
    Connection Times (ms)
                  min  mean[+/-sd] median   max
    Connect:        0   40 261.4      5    3009
    Processing:    21  499 144.6    503    1082
    Waiting:       18  495 144.5    498    1081
    Total:         62  540 307.3    513    3673

    Percentage of the requests served within a certain time (ms)
      50%    513
      66%    541
      75%    563
      80%    579
      90%    690
      95%    850
      98%    954
      99%   1581
     100%   3673 (longest request)

___


`ab -n 1000 -c 100 "http://sashimiquality.com:9000/login?name=name&pass=password"`


    Concurrency Level:      100
    Time taken for tests:   2.249 seconds
    Complete requests:      1000
    Failed requests:        0
    Write errors:           0
    Total transferred:      748000 bytes
    HTML transferred:       97000 bytes
    Requests per second:    444.67 [#/sec] (mean)
    Time per request:       224.887 [ms] (mean)
    Time per request:       2.249 [ms] (mean, across all concurrent requests)
    Transfer rate:          324.82 [Kbytes/sec] received

    Connection Times (ms)
                  min  mean[+/-sd] median   max
    Connect:        0   31 167.9      0    1001
    Processing:    21  183  54.8    177     363
    Waiting:        6  183  54.9    177     363
    Total:         29  213 175.1    181    1211

    Percentage of the requests served within a certain time (ms)
      50%    181
      66%    216
      75%    233
      80%    241
      90%    259
      95%    295
      98%   1168
      99%   1182
     100%   1211 (longest request)

