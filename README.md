# SASHIMI?
SASHIMI is a web service deleting your tweets periodically. 
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

# APIs
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

## Register new user [[back to the list]](#apis)

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

## Login [[back to the list]](#apis)

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

## Logout [[back to the list]](#apis)

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
## Get OAuth URL [[back to the list]](#apis)

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

## Activate account [[back to the list]](#apis)

Activate account with `oauth_token` and `oauth_verifier` given by `oauth_url`.
### Parameters
`POST` `http://sashimiquality.com:9000/oauth`
- `oauth_token`: OAuth token
- `oauth_verifier`: OAuth Verifier

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
       "is_active":false,
       "is_8th":false,
       "request_token":"4tv4uUVpyf2kAfCRvDTisar9OieZ2ENa1uvYdfBBjI",
       "request_token_secret":"kXQ5R8NSIeXlptZsUl9J3EiwGNvgXidvIdWwfM2sY",
       "access_token":"hgeBsfffVhdh52SBthwwdCXgdfbje",
       "access_token_secret":"43119438_sfSGw4423GsfGseEr4hsdGCCxXfgse634"
    }

___
[[back to the list]](#apis)

## Update Sashimi Quality [[back to the list]](#apis)

Update sashimi quality a.k.a lifetime of your tweets.
### Parameters
`POST` `http://sashimiqulity.com:9000/update_sashimi`
- `sashimi`: new sashimi quality in minutes, `[1-9]+[0-9]`

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
## Update escape-term [[back to the list]](#apis)
You can specify `Regular Expression`. Tweets contains matches is excluded from deletion. 
### Parameters
`POST` `http://sashimiqulity.com:9000/update_escape_term`
- `sashimi`: new sashimi quality in minutes, `Regular Expression`

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
## Toggle automatic deletion [[back to the list]](#apis)

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
       "is_8th":false,
       "request_token":"4tv4uUVpyf2kAfCRvDTisar9OieZ2ENa1uvYdfBBjI",
       "request_token_secret":"kXQ5R8NSIeXlptZsUl9J3EiwGNvgXidvIdWwfM2sY",
       "access_token":"hgeBsfffVhdh52SBthwwdCXgdfbje",
       "access_token_secret":"43119438_sfSGw4423GsfGseEr4hsdGCCxXfgse634"
    }



___
[[back to the list]](#apis)
## Toggle 8th grader mode [[back to the list]](#apis)

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
       "is_8th":false,
       "request_token":"4tv4uUVpyf2kAfCRvDTisar9OieZ2ENa1uvYdfBBjI",
       "request_token_secret":"kXQ5R8NSIeXlptZsUl9J3EiwGNvgXidvIdWwfM2sY",
       "access_token":"hgeBsfffVhdh52SBthwwdCXgdfbje",
       "access_token_secret":"43119438_sfSGw4423GsfGseEr4hsdGCCxXfgse634"
    }



___
[[back to the list]](#apis)
## Tweet via Sashimi [[back to the list]](#apis)

___
[[back to the list]](#apis)



# Footnote
SASHIMI is the only way that you can automatically delete your tweets in damn accurate timing. Fault tolerance architecture guarantees damn certainty of elimination of every single shit you tweeted. This is indeed the only way to keep your tweets fresh and Sashimi Quality. 

