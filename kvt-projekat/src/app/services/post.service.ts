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

  createPostInGroup(groupId:number, post: Post): Observable<Post> {

    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    const params = { groupId: groupId.toString() };

    return this.http.post<Post>('api/posts/createPostInGroup', post, { headers,params });
  }

  getMyPosts(username:string): Observable<Array<Post>> {

    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return this.http.get<Array<Post>>('api/posts/my/'+ username, {headers});
  }

  getPostsFromMyGroups(username:string): Observable<Array<Post>> {

    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return this.http.get<Array<Post>>('api/posts/myGroupsPosts/'+ username, {headers});
  }
 
  getLastId(): Observable<number> {

    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return this.http.get<number>('api/posts/lastId', {headers});
  }

  getPostById(id:number): Observable<Post> {

    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return this.http.get<Post>('api/posts/' + id, {headers});
  }

  getFriendsPosts(username:string): Observable<Array<Post>> {

    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return this.http.get<Array<Post>>('api/posts/myFriends/'+ username, {headers});
  }

  orderAsc(posts:Post[]): Observable<Array<Post>> {

    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return this.http.put<Array<Post>>('api/posts/orderAsc', posts, {headers});
  }

  orderDesc(posts:Post[]): Observable<Array<Post>> {

    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return this.http.put<Array<Post>>('api/posts/orderDesc', posts, {headers});
  }


  updatePost(id:number,post:Post): Observable<Post> {

    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return this.http.put<Post>('api/posts/' + id , post, {headers});
  }

  deletePost(id:number): Observable<Post> {

    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return this.http.delete<Post>('api/posts/' + id, {headers});
  }

  getGroupPosts(id:number): Observable<Array<Post>> {

    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return this.http.get<Array<Post>>('api/posts/groupPost/'+ id, {headers});
  }
}
