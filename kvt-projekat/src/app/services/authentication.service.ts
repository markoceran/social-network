import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class AuthenticationService {
	private headers = new HttpHeaders({'Content-Type': 'application/json'});

	constructor(
		private http: HttpClient
	) { }

	login(auth: any): Observable<any> {
		return this.http.post('api/users/login', {username: auth.username, password: auth.password}, {headers: this.headers, responseType: 'json'});
	}

	isLoggedIn(): boolean {
		if (!localStorage.getItem('user')) {
				return false;
		}
		return true;
	}

	register(user: any): Observable<any> {
		return this.http.post('api/users/signup', {username: user.username, password: user.password, email: user.email, firstName: user.firstName, lastName: user.lastName}, {headers: this.headers, responseType: 'json'});
	}


}
