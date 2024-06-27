import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Group } from '../model/group';
import { Observable } from 'rxjs';
import { GroupAdmin } from '../model/groupAdmin';
import { User } from '../model/user.model';

@Injectable({
  providedIn: 'root'
})
export class GroupService {

  constructor(private http: HttpClient) { }

  createGroup(group: Group): Observable<Group> {

    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return this.http.post<Group>('api/groups', group, {headers});
  }

  getMyGroups(): Observable<Array<Group>> {

    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return this.http.get<Array<Group>>('api/groups/my', {headers});
  }

  getAllGroups(): Observable<Array<Group>> {
    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
    return this.http.get<Array<Group>>('api/groups', {headers});
  }

  getGroupById(id:number): Observable<Group> {
    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return this.http.get<Group>('api/groups/' + id, {headers});
  }

  findGroup(unos:String): Observable<Array<Group>> {

    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return this.http.get<Array<Group>>('api/groups/find/' + unos, {headers});
  }

  suspendGroup(id:number, suspendedReason:String): Observable<any> {

    const params = { suspendedReason: suspendedReason.toString() };
    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return this.http.put<any>('api/groups/suspend/' + id, null, {headers,params});
  }


  getGroupAdmins(groupId:number): Observable<Array<GroupAdmin>> {
    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return this.http.get<Array<GroupAdmin>>('api/groups/groupAdmins/' + groupId, {headers});
  }

  getGroupUsers(groupId:number): Observable<Array<User>> {
    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return this.http.get<Array<User>>('api/groups/groupUsers/' + groupId, {headers});
  }

  updateGroup(id:number, group: Group): Observable<Group> {

    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return this.http.put<Group>('api/groups/'+ id , group, {headers});
  }

  deleteGroup(id:number): Observable<Group> {

    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return this.http.delete<Group>('api/groups/'+ id , {headers});
  }

  createGroupWithPDF(formData: FormData): Observable<Group> {

    const token = localStorage.getItem('user');

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    return this.http.post<Group>('api/groups/saveWithPdf', formData, {headers});
  }


}
