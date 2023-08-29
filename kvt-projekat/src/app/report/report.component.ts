import { Component, OnInit } from '@angular/core';
import { ReportService } from '../services/report.service';
import { ToastrService } from 'ngx-toastr';
import { Report } from '../model/report';

@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.css']
})
export class ReportComponent implements OnInit{

  postReports!: Report[]
  commentReports!: Report[]
  userReports!: Report[]

  constructor(private reportService:ReportService,private toastr: ToastrService) {
  
  }

  ngOnInit() {

    this.reportService.getByPost().subscribe(
      (reports: any) => {
        this.postReports = reports;        
      },
      (error) => {
        console.error('Error getting report:', error);
        this.toastr.error('Error getting report!');
      }
    );

    this.reportService.getByComment().subscribe(
      (reports: any) => {
        this.commentReports = reports;        
      },
      (error) => {
        console.error('Error getting report:', error);
        this.toastr.error('Error getting report!');
      }
    );

    this.reportService.getByUser().subscribe(
      (reports: any) => {
        this.userReports = reports;        
      },
      (error) => {
        console.error('Error getting report:', error);
        this.toastr.error('Error getting report!');
      }
    );
  }

  acceptForPost(id:number){
    this.reportService.acceptReportForPost(id).subscribe(
      (report: any) => {
        console.log('Report successfully accepted:', report);
        this.toastr.success('Report successfully accepted:');
        window.location.reload();
      },
      (error) => {
        console.error('Error accepting report:', error);
        this.toastr.error('Error accepting report!');
      }
    );
  }

  acceptForUser(id:number){
    this.reportService.acceptReportForUser(id).subscribe(
      (report: any) => {
        console.log('Report successfully accepted:', report);
        this.toastr.success('Report successfully accepted:');
        window.location.reload();
      },
      (error) => {
        console.error('Error accepting report:', error);
        this.toastr.error('Error accepting report!');
      }
    );
  }

  acceptForComment(id:number){
    this.reportService.acceptReportForComment(id).subscribe(
      (report: any) => {
        console.log('Report successfully accepted:', report);
        this.toastr.success('Report successfully accepted:');
        window.location.reload();
      },
      (error) => {
        console.error('Error accepting report:', error);
        this.toastr.error('Error accepting report!');
      }
    );
  }

  deny(id:number){
    this.reportService.denyReport(id).subscribe(
      (report: any) => {
        console.log('Report successfully denied:', report);
        this.toastr.success('Report successfully denied:');
        window.location.reload();
      },
      (error) => {
        console.error('Denied error:', error);
        this.toastr.error('Denied error!');
      }
    );
  }
}
