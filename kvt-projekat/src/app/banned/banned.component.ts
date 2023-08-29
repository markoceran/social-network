import { Component, OnInit } from '@angular/core';
import { ReportService } from '../services/report.service';
import { ToastrService } from 'ngx-toastr';
import { User } from '../model/user.model';
import { BannedService } from '../services/banned.service';

@Component({
  selector: 'app-banned',
  templateUrl: './banned.component.html',
  styleUrls: ['./banned.component.css']
})
export class BannedComponent implements OnInit{

  bannedUsers!: User[]

  constructor(private bannedService:BannedService,private toastr: ToastrService) {
  
  }

  ngOnInit() {

    this.bannedService.getAllUser().subscribe(
      (users: User[]) => {
        this.bannedUsers = users;        
      },
      (error) => {
        console.error('Error getting banned users:', error);
        this.toastr.error('Error getting banned users!');
      }
    );
  }

  deleteBan(id:number){
    this.bannedService.deleteBan(id).subscribe(
      (data: any) => {
        console.log('Successfully deleted ban :', data);
        this.toastr.success('Successfully deleted ban!');     
        window.location.reload();   
      },
      (error) => {
        console.error('Error deleting ban:', error);
        this.toastr.error('Error deleting ban!');
      }
    );
  }




}
