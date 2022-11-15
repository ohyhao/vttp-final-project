import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { first, firstValueFrom, lastValueFrom } from "rxjs";
import { Count, Like, NewTweet, Quote, ReplyTweet, Response, Retweet, Tweet } from "../models";

const httpHeaders = {
    headers: new HttpHeaders()
        .set('Content-Type', 'application/json')
        .set('Accept', 'application/json')
}

 
@Injectable()
export class TweetService {

    constructor(private http: HttpClient) {}

    getAllTweets(limit: number, offset: number): Promise<Tweet[]> {

        const params = new HttpParams()
            .set("limit", limit)
            .set("offset", offset)
           
        return firstValueFrom(
            this.http.get<Tweet[]>('/api/posts', { params }))   
    }

    newTweet(newTweet: NewTweet): Promise<any> {
        
        return firstValueFrom(
            this.http.post<string>("/api/posts/create", newTweet, httpHeaders)
        )
    }

    getQuote(): Promise<Quote> {

        return firstValueFrom(
            this.http.get<Quote>('/api/quote', httpHeaders)
        )
    }

    getTweet(id: number): Promise<Tweet> {

        return firstValueFrom(
            this.http.get<Tweet>(`/api/posts/tweet/${id}`, httpHeaders))
    }


    deleteTweet(id: number): Promise<any> {

        return firstValueFrom(
            this.http.post(`/api/posts/delete/${id}`, httpHeaders)
        )
    }

    getTweetsByUsername(username: string): Promise<Tweet[]> {
        
        return firstValueFrom(
            this.http.get<Tweet[]>(`/api/posts/tweets/${username}`, httpHeaders)
            ) 
        }
        
    getRetweetsByUsername(username: string): Promise<Tweet[]> {
        
        return firstValueFrom(
            this.http.get<Tweet[]>(`/api/posts/retweets/${username}`, httpHeaders)
        ) 
    }

    getLikesByUsername(username: string): Promise<Tweet[]> {
        
        return firstValueFrom(
            this.http.get<Tweet[]>(`/api/posts/likes/${username}`, httpHeaders)
        ) 
    }
    
    getRepliesByUsername(username: string): Promise<Tweet[]> {
        
        return firstValueFrom(
            this.http.get<Tweet[]>(`/api/posts/replies/${username}`, httpHeaders)
        ) 
    }

    getRepliesByTweet(id: number): Promise<Tweet[]> {

        return firstValueFrom(
            this.http.get<Tweet[]>(`/api/posts/${id}/replies`, httpHeaders)
        )
    }


    likeTweet(like: Like): Promise<Response> {

        return firstValueFrom(
            this.http.post<Response>('/api/like', like, httpHeaders)
        )
    }

    retweetTweet(retweet: Retweet): Promise<any> {

        return firstValueFrom(
            this.http.post('/api/retweet', retweet, httpHeaders)
        )
    }

    replyTweet(reply: ReplyTweet): Promise<any> {

        return firstValueFrom(
            this.http.post('/api/posts/reply', reply, httpHeaders)
        )
    }

    getTweetCountById(id: number): Promise<Count> {

        return firstValueFrom(
            this.http.get<Count>(`/api/posts/count/${id}`, httpHeaders)
        )
    }
}