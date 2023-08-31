import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Comments } from '../model/comment';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  constructor(private http: HttpClient) {}

  createComment(comment: Comments, postId:number): Observable<Comments> {

    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    const params = { postId: postId.toString() };

    return this.http.post<Comments>('api/comments', comment, { headers, params});
  }

  getComments(postId:number): Observable<Array<Comments>> {
    return this.http.get<Array<Comments>>('api/comments/postComments/'+ postId);
  }


  orderAscLikes(comments:Comments[]): Observable<Array<Comments>> {
    return this.http.put<Array<Comments>>('api/comments/orderAscLikes', comments);
  }

  orderAscDislikes(comments:Comments[]): Observable<Array<Comments>> {
    return this.http.put<Array<Comments>>('api/comments/orderAscDislikes', comments);
  }

  orderAscHearts(comments:Comments[]): Observable<Array<Comments>> {
    return this.http.put<Array<Comments>>('api/comments/orderAscHearts', comments);
  }


  orderDescLikes(comments:Comments[]): Observable<Array<Comments>> {
    return this.http.put<Array<Comments>>('api/comments/orderDescLikes', comments);
  }

  orderDescDislikes(comments:Comments[]): Observable<Array<Comments>> {
    return this.http.put<Array<Comments>>('api/comments/orderDescDislikes', comments);
  }

  orderDescHearts(comments:Comments[]): Observable<Array<Comments>> {
    return this.http.put<Array<Comments>>('api/comments/orderDescHearts', comments);
  }



  orderAscDate(comments:Comments[]): Observable<Array<Comments>> {
    return this.http.put<Array<Comments>>('api/comments/orderAscDate', comments);
  }

  orderDescDate(comments:Comments[]): Observable<Array<Comments>> {
    return this.http.put<Array<Comments>>('api/comments/orderDescDate', comments);
  }


  reply(comment: Comments, commendId:number): Observable<Comments> {

    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    const params = { commendId: commendId.toString() };

    return this.http.post<Comments>('api/comments/reply', comment, { headers, params});
  }
}
