import { Injectable } from '@angular/core';
import { Post } from '../model/post.model';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ArrayType } from '@angular/compiler';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  constructor(private http: HttpClient) {}

  createPost(post: Post): Observable<Post> {

    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return this.http.post<Post>('api/posts', post, { headers });
  }

  getMyPosts(username:string): Observable<Array<Post>> {
    return this.http.get<Array<Post>>('api/posts/my/'+ username);
  }
 
  getLastId(): Observable<number> {
    return this.http.get<number>('api/posts/lastId');
  }

  getFriendsPosts(username:string): Observable<Array<Post>> {
    return this.http.get<Array<Post>>('api/posts/myFriends/'+ username);
  }

}
