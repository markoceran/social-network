import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../model/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserServiceService {

  private headers = new HttpHeaders({'Content-Type': 'application/json'});

  constructor(
    private http: HttpClient
  ) { }


  getProfileData(username: string): Observable<any> {
    return this.http.get<any>('api/users/profile/' + username);
  }

  getUsernameFromToken(): any {

    const token = localStorage.getItem('user');

    if (token) {
      const payload = token.split('.')[1]; // The payload is the middle part of the token
      const decodedPayload = atob(payload); // Decode the base64-encoded payload
      const user = JSON.parse(decodedPayload); // Parse the JSON data in the payload

      const username = user.sub; // 'sub' is the key where the username is stored in the payload
      return username;

    } else {
      console.error('Token not found.');
    }
    return null;
  }

  findUsers(unos: string): Observable<Array<User>> {

    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return this.http.get<Array<User>>('api/users/' + unos , {headers});
  }

  logout(): Observable<any> {
    return this.http.post('api/users/logout', null);
  }

  editProfileData(user:User): Observable<any> {

    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return this.http.put('api/users/editProfile', user, {headers});
  }
  
  editPassword(oldPassword:string, newPassword:string): Observable<any> {

    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    const params = { oldPassword: oldPassword.toString(), newPassword: newPassword.toString()};

    return this.http.put('api/users/editPassword', null, {headers, params});
  }

  deleteAdminFromGroup(groupId:number, groupAdminId:number): Observable<any> {

    const params = { groupId: groupId.toString(), groupAdminId: groupAdminId.toString()};

    return this.http.delete('api/users/deleteGroupAdmin', {params});
  }

}
