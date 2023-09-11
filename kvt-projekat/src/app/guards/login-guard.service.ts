import { Injectable } from '@angular/core';
import { Router, CanActivate } from '@angular/router';
import { AuthenticationService } from '../services/authentication.service';

@Injectable({
  providedIn: 'root'
})
export class LoginGuardService implements CanActivate{

  constructor(
		public auth: AuthenticationService,
		public router: Router
	) { }

	canActivate(): boolean {
		if (this.auth.isLoggedIn()) {
			this.router.navigate(['main']);
			return false;
		}
		return true;
	}
}
