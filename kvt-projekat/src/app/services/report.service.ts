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
    return this.http.get<Array<Report>>('api/reports/allPost');
  }

  getByComment(): Observable<Array<Report>> {
    return this.http.get<Array<Report>>('api/reports/allComment');
  }

  getByUser(): Observable<Array<Report>> {
    return this.http.get<Array<Report>>('api/reports/allUser');
  }

  acceptReportForPost(id:number): Observable<ReportReason> {

    const params = {id: id.toString()};

    return this.http.post<ReportReason>('api/reports/acceptReportForPost', null, {params});
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

    return this.http.post<ReportReason>('api/reports/acceptReportForComment', null, {params});
  }

  denyReport(id:number): Observable<ReportReason> {

    const params = {id: id.toString()};

    return this.http.post<ReportReason>('api/reports/denyReport', null, {params});
  }
}
