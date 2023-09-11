import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../model/user.model';

@Injectable({
  providedIn: 'root'
})
export class BannedService {


  constructor(
    private http: HttpClient
  ) { }

  deleteBan(id:number): Observable<any> {
    
    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    const params = {id: id.toString()};

    return this.http.post<any>('api/banneds/delete', null, {headers, params });
  }

  getAllUser(): Observable<Array<User>> {
    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return this.http.get<Array<User>>('api/banneds/allBannedUser', {headers});
  }

  banUserInGroup(groupId:number, userId:number, groupAdminId:number): Observable<any> {
    
    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    const params = {groupId: groupId.toString(), userId: userId.toString(), groupAdminId: groupAdminId.toString()};

    return this.http.post<any>('api/banneds/createForGroupUser', null, {headers, params });
  }

  unbanUserInGroup(id:number, groupId:number): Observable<any> {
    
    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    const params = {id: id.toString(), groupId: groupId.toString()};

    return this.http.post<any>('api/banneds/deleteForUserInGroup', null, {headers, params });
  }
}
