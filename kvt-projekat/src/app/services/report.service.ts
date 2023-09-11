import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ReportReason } from '../model/ReportReason';
import { Report } from '../model/report';

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  constructor(
    private http: HttpClient
  ) { }

  createReportForPost(id:number,reason:String): Observable<ReportReason> {

    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    const params = {id: id.toString(), reason: reason.toString() };

    return this.http.post<ReportReason>('api/reports/createForPost', null, { headers,params });
  }

  createReportForUser(id:number,reason:String): Observable<ReportReason> {

    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    const params = {id: id.toString(), reason: reason.toString() };

    return this.http.post<ReportReason>('api/reports/createForUser', null, { headers,params });
  }

  createReportForComment(id:number,reason:String): Observable<ReportReason> {

    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    const params = {id: id.toString(), reason: reason.toString() };

    return this.http.post<ReportReason>('api/reports/createForComment', null, { headers,params });
  }

  getByPost(): Observable<Array<Report>> {

    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return this.http.get<Array<Report>>('api/reports/allPost', {headers});
  }

  getByComment(): Observable<Array<Report>> {

    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return this.http.get<Array<Report>>('api/reports/allComment', {headers});
  }

  getByUser(): Observable<Array<Report>> {

    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return this.http.get<Array<Report>>('api/reports/allUser', {headers});
  }

  acceptReportForPost(id:number): Observable<ReportReason> {

    const params = {id: id.toString()};
    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return this.http.post<ReportReason>('api/reports/acceptReportForPost', null, {headers,params});
  }

  acceptReportForUser(id:number): Observable<ReportReason> {

    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    const params = {id: id.toString()};

    return this.http.post<ReportReason>('api/reports/acceptReportForUser', null, {headers, params});
  }

  acceptReportForComment(id:number): Observable<ReportReason> {

    const params = {id: id.toString()};
    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return this.http.post<ReportReason>('api/reports/acceptReportForComment', null, {headers,params});
  }

  denyReport(id:number): Observable<ReportReason> {

    const params = {id: id.toString()};
    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return this.http.post<ReportReason>('api/reports/denyReport', null, {headers,params});
  }
}
