import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Reaction } from '../model/reaction';

@Injectable({
  providedIn: 'root'
})
export class ReactionService {

  constructor(private http: HttpClient) {}

  likePost(id: number): Observable<any> {

    const token = localStorage.getItem('user');
    const params = { id: id.toString() };

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return this.http.post<any>('api/reactions/likePost', id, { headers, params});
  }

  heartPost(id: number): Observable<any> {

    const token = localStorage.getItem('user');
    const params = { id: id.toString() };

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return this.http.post<any>('api/reactions/heartPost', id, { headers, params});
  }

  dislikePost(id: number): Observable<any> {

    const token = localStorage.getItem('user');
    const params = { id: id.toString() };

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return this.http.post<any>('api/reactions/dislikePost', id, { headers, params});
  }

  likeComment(id: number): Observable<any> {

    const token = localStorage.getItem('user');
    const params = { id: id.toString() };

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return this.http.post<any>('api/reactions/likeComment', id, { headers, params });
  }

  dislikeComment(id: number): Observable<any> {

    const token = localStorage.getItem('user');
    const params = { id: id.toString() };

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return this.http.post<any>('api/reactions/dislikeComment', id, { headers,params });
  }

  heartComment(id: number): Observable<any> {

    const token = localStorage.getItem('user');
    const params = { id: id.toString() };

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return this.http.post<any>('api/reactions/heartComment', id, { headers, params });
  }

  getPostReaction(id:number): Observable<Array<Reaction>> {

    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return this.http.get<Array<Reaction>>('api/reactions/byPost/'+ id, {headers});
  }
 
  getCommentReaction(id:number): Observable<Array<Reaction>> {

    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return this.http.get<Array<Reaction>>('api/reactions/byComment/'+ id, {headers});
  }

}
