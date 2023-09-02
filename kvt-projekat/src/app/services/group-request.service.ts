import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { GroupRequest } from '../model/groupRequest';

@Injectable({
  providedIn: 'root'
})
export class GroupRequestService {

  constructor(private http: HttpClient) { }

  createRequest(id: number): Observable<any> {

    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    const params = { id: id.toString() };

    return this.http.post<any>('api/groupRequest', null, {headers, params});
  }

  getRequests(id:number): Observable<GroupRequest[]> {

    return this.http.get<GroupRequest[]>('api/groupRequest/' + id);
  }



  accept(id: number): Observable<any> {

    const params = { id: id.toString() };

    return this.http.post<any>('api/groupRequest/accept', null, {params});
  }

  deny(id: number): Observable<any> {

    const params = { id: id.toString() };

    return this.http.post<any>('api/groupRequest/deny', null, {params});
  }

}
