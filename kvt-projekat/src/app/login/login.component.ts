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

	constructor(
		private fb: FormBuilder,
		private authenticationService: AuthenticationService,
		private router: Router,
		private toastr: ToastrService
	) {
		this.form = this.fb.group({
			username : [null, Validators.required],
			password: [null, Validators.required]
		});
	}

	ngOnInit() {
	}

	submit() {
		const auth: any = {};
		auth.username = this.form.value.username;
		auth.password = this.form.value.password;

		this.authenticationService.login(auth).subscribe(
			(result) => {
				
				this.toastr.success('Successful login!');
				localStorage.setItem('user', JSON.stringify(result));
				this.router.navigate(['main']);
				
			},
			(error) => {

				if (error.status === 401) {
					this.toastr.error('You have been banned!');
				} else {
					this.toastr.error('Bad credencials');
				}
				
			}
		);
	}
}
