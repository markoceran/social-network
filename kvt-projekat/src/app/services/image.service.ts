import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Image } from '../model/image';

@Injectable({
  providedIn: 'root'
})
export class ImageService {

  constructor(private http: HttpClient) { }

  uploadImageForPost(imageFile: FormData): Observable<any> {
    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    return this.http.post<any>('api/images/setPostImage', imageFile, {headers});
    
  }

  getPostImage(postId:number): Observable<any> {
    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
    return this.http.get<any>('api/images/'+ postId, {headers});
  }

  uploadProfileImage(imageFile: FormData): Observable<any> {

    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    return this.http.post<any>('api/images/setProfileImage', imageFile, { headers });
    
  }

  getProfileImage(): Observable<any> {

    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    return this.http.get<any>('api/images/profileImage', { headers });
  }

  getProfileImageByUser(username: String): Observable<any> {

    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    return this.http.get<any>('api/images/profileImage/' + username, {headers});
    
  }
  
}
