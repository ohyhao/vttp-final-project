import { Component, EventEmitter, HostListener, Input, OnInit, Output, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PageEvent } from '@angular/material/paginator';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Like, NewTweet, Quote, ReplyTweet, Retweet, Tweet } from 'src/app/models';
import { TweetService } from 'src/app/services/tweet.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  tweetForm!: FormGroup
  replyForm!: FormGroup
  numberForm!: FormGroup
  tweets: Tweet[] = []
  quote!: Quote
  user = JSON.parse(localStorage.getItem('currentUser')!)
  username = this.user.username
  defaultImage = "src\assets\svg\profile.svg"
  throttle = 0;
  distance = 3;
  limit = 10;
  
  constructor(
    private router: Router,
    private tweetSvc: TweetService,
    private userSvc: UserService,
    private fb: FormBuilder,
    ) {
  }
  
  ngOnInit(): void {
    this.tweetForm = this.fb.group({
      text: this.fb.control<string>('', [ Validators.required ]),
      user_id: this.fb.control<number>(this.user.id)
    })

    this.replyForm = this.fb.group({
      reply: this.fb.control<string>('', [ Validators.required ]),
      user_id: this.fb.control<number>(this.user.id)
    })
    
    this.tweetSvc.getQuote()
    .then(result => {
      this.quote = result
      console.info('>>> quote: ', this.quote)
    }).catch(error => {
      console.info('>>> error: ', error)
    })
    
    this.tweetSvc.getAllTweets(this.limit, 0)
      .then(result => {
        this.tweets = result
        console.info('>>> tweets: ', this.tweets)
      }).catch(error => {
        console.info('>>> error: ', error)
      })
  }

  onScroll(): void {
    this.tweetSvc.getAllTweets(this.limit, this.tweets.length)
      .then(result => {
        for (let i = 0; i < result.length; ++i) {
          this.tweets.push(result[i])
        }
        console.info('>>> tweets: ', this.tweets)
      }).catch(error => {
        console.info('>>> error: ', error)
      })
  }
  
  onClick(id: number) {
    for (let tweet of this.tweets) {
      if (tweet.id == id) {
        tweet.showForm = !tweet.showForm;
      }
    }   
  }

  processTweet() {
    const tweet: NewTweet = this.tweetForm.value
    console.info('>>> new tweet: ', tweet)
    this.tweetSvc.newTweet(tweet)
      .then(result => {
        console.info('>>> result: ', result)
        this.ngOnInit()
    }).catch(error => {
      console.info('>>> error: ', error)
        alert("Tweet failed")
        this.tweetForm.reset()
      }
    )
  }

  deleteTweet(id: number) {
    this.tweetSvc.deleteTweet(id)
      .then(result => {
        console.info('>>> result: ', result)
        this.ngOnInit()
      }).catch(error => {
        console.info('>>> error: ', error)
        alert("Failed to delete tweet")
      })
  }

  
  likeTweet(id: number) {
    const like: Like = {
      tweet_id: id,
      user_id: this.user.id
    }
    this.tweetSvc.likeTweet(like)
      .then(result => {
        for (let tweet of this.tweets) {
          if (tweet.id == id) {
            tweet.likes += +result.data;
          }
        }
        console.info(' >>> result: ', result)
      }).catch(error => {
        console.info(' >>> error: ', error)
      })
  }

  retweetTweet(id: number) {
    const retweet: Retweet = {
      tweet_id: id,
      user_id: this.user.id
    }
    this.tweetSvc.retweetTweet(retweet)
      .then(result => {
        for (let tweet of this.tweets) {
          if (tweet.id == id) {
            tweet.retweets += +result.data;
          }
        }
        console.info(' >>> result: ', result)
      }).catch(error => {
        console.info(' >>> error: ', error)
      })

  }

  replyTweet(id: number) {
    const reply: ReplyTweet = this.replyForm.value
      reply.tweet_id = id;
    console.info('>>> reply: ', reply)
    this.tweetSvc.replyTweet(reply)
      .then(result => {
        for (let tweet of this.tweets) {
          if (tweet.id == id) {
            tweet.replies += +result.data;
          }
        }
        console.info(' >>> result: ', result)
      }).catch(error => {
        console.info(' >>> error: ', error)
      })
  }
  

  logout() {
    localStorage.removeItem('currentUser')
    this.userSvc.logout()
      .then(result => {
        console.info(' >>> result: ', result)
      }).catch(error => {
        console.info(' >>> error: ', error)
      })
    this.router.navigate(['/'])
  }

  bottomReached(): boolean {
    return (window.innerHeight + window.scrollY) >= document.body.offsetHeight;
  }
}
