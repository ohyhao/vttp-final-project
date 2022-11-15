import { Component, ElementRef, OnInit, ViewChild, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { ModalDismissReasons, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AppUser, Count, Like, ReplyTweet, Retweet, Tweet } from 'src/app/models';
import { TweetService } from 'src/app/services/tweet.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit, OnDestroy {

  imageFile!: File;
  setImageFileValue(fileRef: HTMLInputEvent) {
    // anything the user does after pressing the upload btn will trigger this
    // means if the user cancel the upload box also will trigger
    // you might want to check if the user
    // 1. selects a file > check if file correct and size not too big
    // 2. open the upload box but never choose any file, will cause below to be undefined thats when you just return
    this.imageFile = fileRef.target.files![0]
    
  }
  
  username: string
  profileOwner!: AppUser
  tweets: Tweet[] = []
  repliedTweets: Tweet[] = []
  tweetCount!: Count
  form!: FormGroup
  replyForm!: FormGroup
  loggedUser = JSON.parse(localStorage.getItem('currentUser')!)
  closeResult!: string
  navigationSubscription: any

  constructor(
    private fb: FormBuilder,
    private userSvc: UserService,
    public router: Router,
    private activatedRoute: ActivatedRoute,
    private tweetSvc: TweetService,
    private modalService: NgbModal,
    private title: Title) {

      this.username = this.activatedRoute.snapshot.params['username']

      this.router.routeReuseStrategy.shouldReuseRoute = function () {
        return false;
      };
  
      this.navigationSubscription = this.router.events.subscribe((event: any) => {
        if (event instanceof NavigationEnd) {

          this.router.navigated = false;

          if (this.router.url.includes(`/${this.username}/tweets`)) {
            this.getTweets();
          }
  
          else if (this.router.url.includes(`/${this.username}/retweets`)) {
            this.getRetweets();
          }
  
          else if (this.router.url.includes(`/${this.username}/replies`)) {          
            this.getReplies();
          }
  
          else if (this.router.url.includes(`/${this.username}/likes`)) {
            this.getLikes();
          }
        }
      })
    }    

  ngOnInit(): void {
    this.title.setTitle('Profile')
    
    this.userSvc.getUserDetails(this.username)
    .then(result => {
      this.profileOwner = result
      console.info(' >>> profileOwner: ', result)
      this.tweetSvc.getTweetCountById(this.profileOwner.id)
        .then(result => {
          this.tweetCount = result
          console.info(' >>> tweetCount: ', this.tweetCount)
      }).catch(error => {
          console.info(' >>> error: ', error)
        })
    }).catch(error => {
      console.info(' >>> error: ', error)
    })

    this.form = this.fb.group({
      'image-file': this.fb.control('', [ Validators.required ])
    })

    this.replyForm = this.fb.group({
      reply: this.fb.control<string>('', [ Validators.required ]),
      user_id: this.fb.control<number>(this.loggedUser.id)
    })
  }

  ngOnDestroy() {
    if (this.navigationSubscription) {
      this.navigationSubscription.unsubscribe();
    }
  }

  upload() {
    const formData = new FormData()
    formData.set('myfile', this.imageFile) 
    this.userSvc.upload(formData, this.loggedUser.username)
      .then(result => {
        console.info('>>> result: ', result)
        location.reload()
      })
      .catch(error => {
        console.info('>>> error: ', error)
        alert("Upload failed")
      })
  }

  onClick(id: number) {
    for (let tweet of this.tweets) {
      if (tweet.id == id) {
        tweet.showForm = !tweet.showForm;
      }
    }
    for (let repliedTweet of this.repliedTweets) {
      if (repliedTweet.id == id) {
        repliedTweet.showForm = !repliedTweet.showForm;
      }
    }
  }

  getTweets() {
    this.tweetSvc.getTweetsByUsername(this.username)
      .then(result => {
        this.tweets = result;
        console.info('>>> tweetsByUser: ', this.tweets)
      }).catch(error => {
        console.info(' >>> error: ', error)
      })
  }

  
  getRetweets() {
    this.tweetSvc.getRetweetsByUsername(this.username)
      .then(result => {
        this.tweets = result;
        console.info('>>> retweetsByUser: ', this.tweets)
      }).catch(error => {
        console.info(' >>> error: ', error)
      })
  }

  getReplies() {
    this.tweetSvc.getRepliesByUsername(this.username)
      .then(result => {
        this.tweets = result;
        for (let i = 0; i < this.tweets.length; i++) {
          this.tweetSvc.getTweet(this.tweets[i].tweet_id)
            .then(result => {
              this.repliedTweets.push(result)
            }).catch(error => {
              console.info(' >>> error: ', error)
            })
          }
        console.info('>>> repliesByUser: ', this.tweets)
        console.info('>>> repliedTweets: ', this.repliedTweets)
      }).catch(error => {
        console.info(' >>> error: ', error)
      })
  }

  getLikes() {
    this.tweetSvc.getLikesByUsername(this.username)
      .then(result => {
        this.tweets = result;
        console.info('>>> likesByUser: ', this.tweets)
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

  likeTweet(id: number) {
    const like: Like = {
      tweet_id: id,
      user_id: this.loggedUser.id
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
      user_id: this.loggedUser.id
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

  open(content: any) {
    this.modalService.open(content,
   {ariaLabelledBy: 'modal-basic-title'}).result.then((result) => {
      this.closeResult = `Closed with: ${result}`;
    }, (reason) => {
      this.closeResult = 
         `Dismissed ${this.getDismissReason(reason)}`;
    });
  }
  
  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      return `with: ${reason}`;
    }
  }

}

interface HTMLInputEvent extends Event {
  target: HTMLInputElement & EventTarget;
}