import { HttpClient } from '@angular/common/http';
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

    const params = {id: id.toString()};

    return this.http.post<any>('api/banneds/delete', null, { params });
  }

  getAllUser(): Observable<Array<User>> {
    return this.http.get<Array<User>>('api/banneds/allBannedUser');
  }
}
