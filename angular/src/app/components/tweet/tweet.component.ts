import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Like, ReplyTweet, Retweet, Tweet } from 'src/app/models';
import { TweetService } from 'src/app/services/tweet.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-tweet',
  templateUrl: './tweet.component.html',
  styleUrls: ['./tweet.component.css']
})
export class TweetComponent implements OnInit {

  username!: string
  id!: string
  tweet!: Tweet
  replies: Tweet[] = []
  replyForm!: FormGroup
  loggedUser = JSON.parse(localStorage.getItem('currentUser')!)

  constructor(
    private tweetSvc: TweetService,
    private userSvc: UserService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private fb: FormBuilder) { }

  ngOnInit(): void {
    this.username = this.activatedRoute.snapshot.params['username']
    this.id = this.activatedRoute.snapshot.params['id']
    console.info(' >>> username: ', this.username)
    console.info(' >>> tweet_id: ', this.id)

    this.replyForm = this.fb.group({
      reply: this.fb.control<string>('', [ Validators.required ]),
      user_id: this.fb.control<number>(this.loggedUser.id)
    })

    this.tweetSvc.getTweet(+this.id)
      .then(result => {
        this.tweet = result;
        console.info(' >>> result: ', result)
      }).catch(error => {
        console.info(' >>> error: ', error)
      })

    this.tweetSvc.getRepliesByTweet(+this.id)
      .then(result => {
        this.replies = result;
        console.info(' >>> result: ', result)
      }).catch(error => {
        console.info(' >>> error: ', error)
      })
  }

  onClick() {
      this.tweet.showForm = !this.tweet.showForm; 
  }

  convert(date: String) {
    date.substring(0, 11)
  }

  likeTweet(id: number) {
    const like: Like = {
      tweet_id: id,
      user_id: this.loggedUser.id
    }
    this.tweetSvc.likeTweet(like)
      .then(result => {
        this.tweet.likes += +result.data
        console.info(' >>> result: ', result)
      }).catch(error => {
        console.info(' >>> error: ', error)
      })
  }

  retweetTweet(id: number) {
    const retweet: Retweet = {
      tweet_id: id,
      user_id: this.loggedUser.id
    }
    this.tweetSvc.retweetTweet(retweet)
      .then(result => {
        this.tweet.retweets += +result.data
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
        this.tweet.replies += +result.data;
        console.info(' >>> result: ', result)
        this.ngOnInit()
      }).catch(error => {
        console.info(' >>> error: ', error)
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
