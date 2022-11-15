import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { Credentials } from 'src/app/models';
import { UserService } from 'src/app/services/user.service';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  showPassword!: boolean;
  loginForm!: FormGroup

  constructor(private fb: FormBuilder, private userSvc: UserService, private router: Router, private title: Title) { }

  ngOnInit(): void {
    this.title.setTitle('Tweeter')

    this.loginForm = this.fb.group({
      email: this.fb.control<string>('', [ Validators.required, Validators.email ]),
      password: this.fb.control<string>('', [ Validators.required ]),
    })

  }

  processLogin() {
    const cred: Credentials = this.loginForm.value
    console.info(">>> credentials: ", cred)
    this.userSvc.authenticateUser(cred)
      .then(result => {
        console.info('>>> result: ', result)
        localStorage.setItem('currentUser', JSON.stringify(result))
        this.router.navigate(['home'])
      })
      .catch(error => {
        console.info('>>> error: ', error)
        alert("Invalid credentials")
        this.loginForm.reset()
      })
  }
}
