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

    return this.http.post<any>('api/images/setPostImage', imageFile, { headers });
    
  }

  getPostImage(postId:number): Observable<any> {
    return this.http.get<any>('api/images/'+ postId);
  }
  
}
