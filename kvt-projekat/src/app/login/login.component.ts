import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthenticationService } from '../services/authentication.service';
import { finalize } from 'rxjs/operators';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  form: FormGroup;
  isLoggingIn = false;

  constructor(
    private fb: FormBuilder,
    private authenticationService: AuthenticationService,
    private router: Router,
    private toastr: ToastrService
  ) {
    this.form = this.fb.group({
      username: [null, Validators.required],
      password: [null, Validators.required]
    });
  }

  ngOnInit() {
  }

  submit() {
    const auth: any = {};
    auth.username = this.form.controls['username'].value;
    auth.password = this.form.controls['password'].value;

    this.isLoggingIn = true;
    this.authenticationService.login(auth)
      .pipe(
        finalize(() => {
          this.isLoggingIn = false;
        })
      )
      .subscribe({
        next: result => {
          this.toastr.success('Successful login!');
          localStorage.setItem('user', JSON.stringify(result));
          this.router.navigate(['main']);
        },
        error: error => {
          this.toastr.error(error.error);
        }
      });
  }
}
