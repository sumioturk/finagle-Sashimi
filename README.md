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

## Register new user 


`curl -i --data "name=sumioturk&pass=password&sashimi=15" "http://host:port/join"`
  

    HTTP/1.1 200 OK
    Content-Type: application/json;charset=UTF-8
    Content-Length: 351

    {
      "id":"3",
      "twitter_id":"",
      "name":"john",
      "pass":"password",
      "last_tweet_id":"",
      "sashimi":"15",
      "escape_term":"",
      "is_premium":0,
      "is_active":0,
      "request_token":"DLSsK50ngjYmidcZP07UOi5UxhrpsyAyS2DmPm6oEg",
      "request_token_secret":"SxkJjPQQaSENTGGbM4klMLYq5nW8VqdAJci8nUlukyA",
      "access_token":"",
      "access_token_secret":""
    }  


## Login

### Login

`curl -i --data "name=sumioturk&pass=password" "http://host:port/login"`

    HTTP/1.1 200 OK
    Content-Type: application/json;charset=UTF-8
    Set-Cookie: key=2Fw/fRXNp6LfIUiKe0EbRJHeRdA=_82a83670-2511-11e2-8ddd-00270e020e4c_1351876791127;
    Content-Length: 27

    {"message":"You Logged In"}
____

You should identify yourself by passing the session key given when you logged in to call rest of APIs; 
you either pass the session key via body parameter:

    curl -i --data "key=key=2Fw/fRXNp6LfIUiKe0EbRJHeRdA=_82a83670-2511-11e2-8ddd-00270e020e4c_1351876791127 ...

or cookie:

    curl -i --header "Cookie: key=2Fw/fRXNp6LfIUiKe0EbRJHeRdA=_82a83670-2511-11e2-8ddd-00270e020e4c_1351876791127;" ...

    

### Logout
Call login API whlile you logged in.

     curl -i \
     --header "Cookie: key=2Fw/fRXNp6LfIUiKe0EbRJHeRdA=_82a83670-2511-11e2-8ddd-00270e020e4c_1351876791127;" \
     "http://host:port/login"



## Get twitter OAuth URL
  
    curl -i \ 
    --header "Cookie: key=2Fw/fRXNp6LfIUiKe0EbRJHeRdA=_82a83670-2511-11e2-8ddd-00270e020e4c_1351876791127;" \
    --data "name=sumioturk&pass=password" \
    "http://host:port/oauth_url"

____
    HTTP/1.1 200 OK
    Content-Type: application/json;charset=UTF-8
    Content-Length: 112
 
    {
      "auth_url":"https://api.twitter.com/oauth/authorize?oauth_token=VFiyIRwClQ8PRn1JmXtsJDKhNHJhck8XaZ5iT8b8g"
    }



## Activate an account via twitter OAuth 
    
    curl -i \
    --header "Cookie: key=2Fw/fRXNp6LfIUiKe0EbRJHeRdA=_82a83670-2511-11e2-8ddd-00270e020e4c_1351876791127;" \
     --data "oauth_token=tokenhere&oauth_verifier=verifierhere" \
    "http://host:port/oauth"


## On/Off automatic deletion
    
    curl -i \
    --header "Cookie: key=2Fw/fRXNp6LfIUiKe0EbRJHeRdA=_82a83670-2511-11e2-8ddd-00270e020e4c_1351876791127;" \
    "http://host:port/toggle"

## Change lifetime of tweets (unit: min.)
 
    curl -i \
    --header "Cookie: key=2Fw/fRXNp6LfIUiKe0EbRJHeRdA=_82a83670-2511-11e2-8ddd-00270e020e4c_1351876791127;" \
    --data "sashimi=12" "http://host:port/update_sashimi"`


## Get the user's profile
    
    curl -i \
    --header "Cookie: key=2Fw/fRXNp6LfIUiKe0EbRJHeRdA=_82a83670-2511-11e2-8ddd-00270e020e4c_1351876791127;" \
    "http://host:port/get_user_profile" 

## Tweet via Sashimi
    
    curl -i \
    --header "Cookie: key=2Fw/fRXNp6LfIUiKe0EbRJHeRdA=_82a83670-2511-11e2-8ddd-00270e020e4c_1351876791127;" \
    "status=Hi, I\'m Sashimi Quality!" "http://host:port/tweet"

## 8th Grader Mode
This mode is also called `厨二病モード`. Once this API is called, SASHIMI will start deleting ALL tweets in the past  including tweets you tweeted before you signed up for SASHIMI. Notice that you can NOT revert this action. 
It is unstoppable.
# Footnote
SASHIMI is the only way that you can automatically delete your tweets in damn accurate timing. Fault tolerance architecture guarantees damn certainty of elimination of every single shit you tweeted. This is indeed the only way to keep your tweets fresh and Sashimi Quality. 