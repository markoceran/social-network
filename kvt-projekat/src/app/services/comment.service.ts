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
}
