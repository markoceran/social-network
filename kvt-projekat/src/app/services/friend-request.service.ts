import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { FriendRequest } from '../model/friendRequest';

@Injectable({
  providedIn: 'root'
})
export class FriendRequestService {

  constructor(private http: HttpClient) { }

  createRequest(id: number): Observable<any> {

    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    const params = { id: id.toString() };

    return this.http.post<any>('api/friendRequest', null, {headers, params});
  }

  getRequests(): Observable<Array<FriendRequest>> {

    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return this.http.get<Array<FriendRequest>>('api/friendRequest', {headers});
  }


  getAll(): Observable<Array<FriendRequest>> {

    return this.http.get<Array<FriendRequest>>('api/friendRequest/all');
  }

  accept(id: number): Observable<any> {

    const params = { id: id.toString() };

    return this.http.post<any>('api/friendRequest/accept', null, {params});
  }

  deny(id: number): Observable<any> {

    const params = { id: id.toString() };

    return this.http.post<any>('api/friendRequest/deny', null, {params});
  }
}
