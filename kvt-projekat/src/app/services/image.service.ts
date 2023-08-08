import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ImageService {

  constructor(private http: HttpClient) { }

  uploadImageForPost(image: any): Observable<any> {
    return this.http.post<any>('api/images/setPostImage', image);
  }
}
