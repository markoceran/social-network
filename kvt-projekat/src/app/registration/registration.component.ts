import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthenticationService } from '../services/authentication.service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent {

  form: FormGroup;

	constructor(
		private fb: FormBuilder,
		private authenticationService: AuthenticationService,
		private router: Router,
		private toastr: ToastrService
	) {
		this.form = this.fb.group({
			username : [null, Validators.required],
			password: [null, Validators.required],
      		email: [null, Validators.required],
      		firstName: [null, Validators.required],
      		lastName: [null, Validators.required]
		});
	}


	submit() {
		const user: any = {};
		user.username = this.form.value.username;
		user.password = this.form.value.password;
    	user.email = this.form.value.email;
    	user.firstName = this.form.value.firstName;
    	user.lastName = this.form.value.lastName;

		this.authenticationService.register(user).subscribe(
			result => {
				this.toastr.success('Successful sign up!');
				this.router.navigate(['']);
			},
			error => {
				this.toastr.error('Sign up error, invalid input value');
			}
		);
	}
}
