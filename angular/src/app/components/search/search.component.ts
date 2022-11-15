import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { Like, ReplyTweet, Retweet, Search, Tweet } from 'src/app/models';
import { TweetService } from 'src/app/services/tweet.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {

  searchForm!: FormGroup
  replyForm!: FormGroup
  user = JSON.parse(localStorage.getItem('currentUser')!)
  username = this.user.username
  tweets: Tweet[] = []
  

  constructor(
    private router: Router,
    private tweetSvc: TweetService,
    private userSvc: UserService,
    private fb: FormBuilder,
    private title: Title
  ) { }

  ngOnInit(): void {
    this.title.setTitle('Search / Tweeter')

    this.searchForm = this.fb.group({
      search: this.fb.control<string>('', [ Validators.required ]),
    })

  }

  onClick(id: number) {
    for (let tweet of this.tweets) {
      if (tweet.id == id) {
        tweet.showForm = !tweet.showForm;
      }
    }   
  }

  processSearch() {
    const search: Search = this.searchForm.value
    this.tweetSvc.searchTweets(search.search)
      .then(result => {
        this.tweets = result;
        console.info(' >>> search: ', result)
        location.reload
      }).catch(error => {
        console.info(' >>> error: ', error)
        alert("There are no tweets!")
      })
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


}
