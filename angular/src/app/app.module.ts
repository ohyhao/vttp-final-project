import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule, Routes } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { UserService } from './services/user.service';
import { HomeComponent } from './components/home/home.component';
import { ProfileComponent } from './components/profile/profile.component';
import { TweetService } from './services/tweet.service';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { MatCardModule } from '@angular/material/card';
import { MatSidenavModule } from '@angular/material/sidenav';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { TweetComponent } from './components/tweet/tweet.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatPaginatorModule} from '@angular/material/paginator';
import { MatFormFieldModule } from '@angular/material/form-field';

const appRoutes: Routes = [
  {path: '', title: 'Tweeter', component: LoginComponent},
  {path: 'login', title: 'Tweeter', component: LoginComponent},
  {path: 'register', title: 'Register', component: RegisterComponent},
  {path: 'home', title: 'Home/Tweeter', component: HomeComponent},
  {path: ':username', title: 'Profile', component: ProfileComponent},
  {path: ':username/tweets', title: 'Profile', component: ProfileComponent},
  {path: ':username/retweets', title: 'Profile', component: ProfileComponent},
  {path: ':username/likes', title: 'Profile', component: ProfileComponent},
  {path: ':username/replies', title: 'Profile', component: ProfileComponent},
  {path: 'tweet/:username/:id', title: 'Tweet', component: TweetComponent},
  {path: '**', redirectTo: '/', pathMatch: 'full'}
]

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    HomeComponent,
    ProfileComponent,
    TweetComponent,
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(appRoutes, { useHash:true }),
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    MatCardModule,
    MatSidenavModule,
    NoopAnimationsModule,
    NgbModule,
    MatToolbarModule,
    InfiniteScrollModule,
    MatPaginatorModule,
    MatFormFieldModule
  ],
  providers: [UserService, TweetService],
  bootstrap: [AppComponent]
})
export class AppModule { }
