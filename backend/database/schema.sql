drop schema if exists twitter;
create database twitter;

use twitter;

create table users (
	user_id int not null auto_increment,
    name varchar(128) not null,
    email varchar(128) UNIQUE not null,
    username varchar(45) UNIQUE not null,
    password varchar(60) not null,
    primary key (user_id)
);

create table tweets (
	id int not null auto_increment,
    tweet text not null,
    like_counter int default 0,
    reply_counter int default 0,
    retweet_counter int default 0,
    date_created datetime,
    tweet_id int,
    user_id int not null,
    primary key (id),
	foreign key (user_id) references users(user_id)
);

create table likes (
	id int not null auto_increment,
    tweet_id int not null,
    user_id int not null,
    primary key (id),
	foreign key (tweet_id) references tweets(id),
	foreign key (user_id) references users(user_id)
);

create table retweets (
	id int not null auto_increment,
    tweet_id int not null,
    user_id int not null,
    primary key (id),
	foreign key (tweet_id) references tweets(id),
	foreign key (user_id) references users(user_id)
)