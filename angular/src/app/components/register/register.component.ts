import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { Router, RouterLink } from '@angular/router';
import { Registration } from 'src/app/models';
import { UserService } from 'src/app/services/user.service';



@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  showPassword!: boolean
  registerForm!: FormGroup

  constructor(private fb: FormBuilder, private userSvc: UserService, private router: Router, private title: Title) { }

  ngOnInit(): void {
    this.title.setTitle('Register')

    this.registerForm = this.fb.group({
      username: this.fb.control<string>('', [ Validators.required ]),
      name: this.fb.control<string>('', [Validators.required]),
      email: this.fb.control<string>('', [ Validators.required, Validators.email ]),
      password: this.fb.control<string>('', [ Validators.required, Validators.minLength(8) ])
    })
  }

  processRegistration() {
    const reg: Registration = this.registerForm.value
    console.info('>>> reg: ', reg)
    this.userSvc.registerUser(reg)
      .then(result => {
        console.info('>>> result: ', result)
        this.router.navigate(['/'])
        alert('Registration is successful. Please kindly login with your email and password.')
      }).catch(error => {
        console.error('>>> error: ', error)
      })
  }
}
