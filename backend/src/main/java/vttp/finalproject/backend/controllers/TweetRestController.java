package vttp.finalproject.backend.controllers;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import vttp.finalproject.backend.models.Response;
import vttp.finalproject.backend.models.Retweet;
import vttp.finalproject.backend.models.Tweet;
import vttp.finalproject.backend.models.AppUser;
import vttp.finalproject.backend.models.Count;
import vttp.finalproject.backend.models.Like;
import vttp.finalproject.backend.models.Post;
import vttp.finalproject.backend.models.Quote;
import vttp.finalproject.backend.models.Reply;
import vttp.finalproject.backend.services.QuoteService;
import vttp.finalproject.backend.services.TweetService;
import vttp.finalproject.backend.services.UserService;

@RestController
@RequestMapping(path = "/api")
public class TweetRestController {
    
    @Autowired
    private TweetService tweetSvc;

    @Autowired
    private UserService userSvc;

    @Autowired
    private QuoteService quoteSvc;

    @GetMapping(path = "/posts")
    public ResponseEntity<String> getAllTweets(
        @RequestParam(defaultValue = "3") Integer limit, @RequestParam(defaultValue = "0") Integer offset) {

        Response resp;

        List<Tweet> tweets = tweetSvc.getAllTweets(limit, offset);
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Tweet tweet: tweets) {
            arrayBuilder.add(Tweet.toJson(tweet));
        }

        if (tweets.isEmpty()) {
            resp = new Response();
                resp.setCode(400);
                resp.setMessage("There are no tweets!");
                return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(arrayBuilder.build().toString());
        } else {
            resp = new Response();
                resp.setCode(200);
                resp.setMessage("Tweets successfully retrieved");
                resp.setData(tweets.toString());
                return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(arrayBuilder.build().toString());
            }
    }

    @PostMapping(path = "/posts/create")
    public ResponseEntity<String> createTweet(@RequestBody String payload) {

        Post post;
        Response resp;

        try {
            post = Post.create(payload);
            System.out.printf(">>> post: %s".formatted(post));
        } catch (Exception ex) {
            resp = new Response();
            resp.setCode(400);
            resp.setMessage(ex.getMessage());
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(resp.toJson().toString());
        }

        try {
            tweetSvc.createTweet(post.getText(), post.getUser_id());
            resp = new Response();
            resp.setCode(201);
            resp.setMessage("Tweet created");
            resp.setData(resp.toJson().toString());
            return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(resp.toJson().toString());
        } catch (Exception ex) {
            resp = new Response();
            resp.setCode(400);
            resp.setMessage(ex.getMessage());
            resp.setData(resp.toJson().toString());
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(resp.toJson().toString());
        }
    }

    @GetMapping(path = "/posts/tweet/{id}")
    public ResponseEntity<String> getTweet(@PathVariable Integer id) {

        Response resp;

        try {
            Tweet tweet = tweetSvc.getTweetById(id);
            resp = new Response();
            resp.setCode(200);
            resp.setMessage("Tweet retrieved");
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(Tweet.toJson(tweet).toString());
        } catch (Exception ex) {
            resp = new Response();
            resp.setCode(400);
            resp.setMessage(ex.getMessage());
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(resp.toJson().toString());
        }
    }


    @PostMapping(path = "/posts/reply")
    public ResponseEntity<String> replyTweet(@RequestBody String payload) {

        Reply reply;
        Response resp;

        try {
            reply = Reply.create(payload);
            System.out.printf(">>> reply: %s".formatted(reply));
        } catch (Exception ex) {
            resp = new Response();
            resp.setCode(400);
            resp.setMessage(ex.getMessage());
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(resp.toJson().toString());
        }

        try {
            Integer i = tweetSvc.replyTweet(reply.getText(), reply.getUser_id(), reply.getTweet_id());
            resp = new Response();
            resp.setCode(200);
            resp.setMessage("Tweet replied");
            resp.setData(i.toString());
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(resp.toJson().toString());
        } catch (Exception ex) {
            resp = new Response();
            resp.setCode(400);
            resp.setMessage(ex.getMessage());
            resp.setData(resp.toJson().toString());
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(resp.toJson().toString());
        }
    } 

    @PostMapping(path = "/posts/delete/{id}")
    public ResponseEntity<String> deleteTweet(@PathVariable Integer id) {

        Response resp;

        System.out.println("delete tweet");

        try {
            tweetSvc.deleteTweet(id);
            resp = new Response();
            resp.setCode(200);
            resp.setMessage("Tweet deleted");
            resp.setData(resp.toJson().toString());
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(resp.toJson().toString());
        } catch (Exception ex) {
            resp = new Response();
            resp.setCode(400);
            resp.setMessage(ex.getMessage());
            resp.setData(resp.toJson().toString());
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(resp.toJson().toString());
        }
    }

    @GetMapping(path = "/posts/tweets/{username}")
    public ResponseEntity<String> getTweetsByUsername(@PathVariable String username) {

        System.out.println("get tweets by user\n");

        Response resp;
        AppUser user = userSvc.getUserDetailsByUsername(username);
        List<Tweet> tweets = tweetSvc.getTweetsById(user.getId());

        System.out.printf(">>> list: %s\n".formatted(tweets));

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Tweet tweet: tweets) {
            arrayBuilder.add(Tweet.toJson(tweet));
        }

        if (tweets.isEmpty()) {
            resp = new Response();
                resp.setCode(200);
                resp.setMessage("There are no tweets!");
                return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(arrayBuilder.build().toString());
        } else {
            resp = new Response();
                resp.setCode(200);
                resp.setMessage("Tweets successfully retrieved");
                return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(arrayBuilder.build().toString());
            }
    }

    @GetMapping(path = "/posts/retweets/{username}")
    public ResponseEntity<String> getRetweetsByUsername(@PathVariable String username) {

        System.out.println("get retweets by id");

        Response resp;
        AppUser user = userSvc.getUserDetailsByUsername(username);
        List<Retweet> retweets = tweetSvc.getRetweetsById(user.getId());
        List<Tweet> retweetedTweets = new ArrayList<>();

        for (int i = 0; i < retweets.size(); i++) {

            Tweet tweet = tweetSvc.getTweetById(retweets.get(i).getTweet_id());
            retweetedTweets.add(tweet);
        }

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Tweet tweet: retweetedTweets) {
            arrayBuilder.add(Tweet.toJson(tweet));
        }

        if (retweetedTweets.isEmpty()) {
            resp = new Response();
                resp.setCode(200);
                resp.setMessage("There are no liked tweets!");
                return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(arrayBuilder.build().toString());
        } else {
            resp = new Response();
                resp.setCode(200);
                resp.setMessage("Tweets successfully retrieved");
                return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(arrayBuilder.build().toString());
            }
    }


    @GetMapping(path = "/posts/likes/{username}")
    public ResponseEntity<String> getLikesByUsername(@PathVariable String username) {

        System.out.println("get likes by id");

        Response resp;
        AppUser user = userSvc.getUserDetailsByUsername(username);
        List<Like> likes = tweetSvc.getLikesById(user.getId());
        List<Tweet> likedTweets = new ArrayList<>();

        for (int i = 0; i < likes.size(); i++) {

            Tweet tweet = tweetSvc.getTweetById(likes.get(i).getTweet_id());
            likedTweets.add(tweet);
        }

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Tweet tweet: likedTweets) {
            arrayBuilder.add(Tweet.toJson(tweet));
        }

        if (likedTweets.isEmpty()) {
            resp = new Response();
                resp.setCode(200);
                resp.setMessage("There are no liked tweets!");
                return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(arrayBuilder.build().toString());
        } else {
            resp = new Response();
                resp.setCode(200);
                resp.setMessage("Tweets successfully retrieved");
                resp.setData(likedTweets.toString());
                return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(arrayBuilder.build().toString());
            }
    }

    @GetMapping(path = "/posts/replies/{username}")
    public ResponseEntity<String> getRepliesByUsername(@PathVariable String username) {

        System.out.println("get replies by id");

        Response resp;
        AppUser user = userSvc.getUserDetailsByUsername(username);
        List<Tweet> replies = tweetSvc.getRepliesById(user.getId());

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Tweet tweet: replies) {
            arrayBuilder.add(Tweet.toJson(tweet));
        }

        if (replies.isEmpty()) {
            resp = new Response();
                resp.setCode(200);
                resp.setMessage("There are no replies!");
                return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(arrayBuilder.build().toString());
        } else {
            resp = new Response();
                resp.setCode(200);
                resp.setMessage("Replies successfully retrieved");
                return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(arrayBuilder.build().toString());
            }
    }

    @GetMapping(path = "/posts/{id}/replies")
    public ResponseEntity<String> getRepliesForTweet(@PathVariable Integer id) {

        Response resp;
        List<Tweet> repliesForTweet = tweetSvc.getRepliesForTweet(id);

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Tweet reply: repliesForTweet) {
            arrayBuilder.add(Tweet.toJson(reply));
        }

        if (repliesForTweet.isEmpty()) {
            resp = new Response();
                resp.setCode(200);
                resp.setMessage("There are no replies for this tweet!");
                return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(arrayBuilder.build().toString());
        } else {
            resp = new Response();
                resp.setCode(200);
                resp.setMessage("Replies successfully retrieved");
                resp.setData(repliesForTweet.toString());
                return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(arrayBuilder.build().toString());
            }

    }

    @PostMapping(path = "/like")
    public ResponseEntity<String> likeTweet(@RequestBody String payload) {

        Like like = new Like();
        Response resp;
        try {
            like = Like.create(payload);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }

        try {
            Integer i = tweetSvc.likeTweet(like.getTweet_id(), like.getUser_id());
            resp = new Response();
            resp.setCode(201);
            resp.setMessage("Tweet liked");
            resp.setData(i.toString());
            return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(resp.toJson().toString());
        } catch (Exception ex) {
            resp = new Response();
            resp.setCode(400);
            resp.setMessage(ex.getMessage());
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(resp.toJson().toString());
        }    
    }

    @PostMapping(path = "/retweet")
    public ResponseEntity<String> retweet(@RequestBody String payload) {

        Retweet retweet = new Retweet();
        Response resp;
        try {
            retweet = Retweet.create(payload);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }

        try {
            Integer i = tweetSvc.retweetTweet(retweet.getTweet_id(), retweet.getUser_id());
            resp = new Response();
            resp.setCode(201);
            resp.setMessage("Tweet retweeted");
            resp.setData(i.toString());
            return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(resp.toJson().toString());
        } catch (Exception ex) {
            resp = new Response();
            resp.setCode(400);
            resp.setMessage(ex.getMessage());
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(resp.toJson().toString());
        }    
    }

    @GetMapping(path = "/posts/count/{id}")
    public ResponseEntity<String> getTweetCountById(@PathVariable Integer id) {

        Count count;
        Response resp;
        
        try {
            count = tweetSvc.getTweetCountById(id);
            resp = new Response();
            resp.setCode(200);
            resp.setMessage("Tweet count retrieved");
            resp.setData(count.toString());
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(Count.toJson(count).toString());
        } catch (Exception ex) {
            resp = new Response();
            resp.setCode(400);
            resp.setMessage(ex.getMessage());
            resp.setData(resp.toJson().toString());
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(resp.toJson().toString());
        }
    }

    @GetMapping(path = "/quote")
    public ResponseEntity<String> getQuote() {

        Quote quote;
        Response resp;

        try {
            Optional<Quote> opt = quoteSvc.getRandomQuote();
            quote = opt.get();
            resp = new Response();
            resp.setCode(200);
            resp.setMessage("Quote retrieved");
            resp.setData(Quote.toJson(quote).toString());
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(Quote.toJson(quote).toString());
        } catch (Exception ex) {
            resp = new Response();
            resp.setCode(400);
            resp.setMessage("Quote retrieved unsuccessfully");
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(resp.toJson().toString());
        }
    }
}

