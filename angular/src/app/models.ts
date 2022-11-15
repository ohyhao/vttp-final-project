export interface Credentials {
    email: string
    password: string
}

export interface Registration {
    username: string
    name: string
    email: string
    password: string
}

export interface AppUser {
    id: number
    name: string
    username: string
    email: string
}

export interface Tweet {
    id: number
    tweet: string
    likes: number
    replies: number
    retweets: number
    date_created: string
    tweet_id: number
    user_id: number
    username: string
    name: string
    showForm?: boolean   
}

export interface NewTweet {
    text: string
    user_id: number
}

export interface Like {
    user_id: number
    tweet_id: number
}

export interface Retweet {
    user_id: number
    tweet_id: number
}

export interface ReplyTweet {
    text: string
    user_id: number
    tweet_id: number
}

export interface Response {
    code: number
    message: string
    data: string
}

export interface Quote {
    quote: string
    author: string
}

export interface Count {
    count: number
}