import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ReportReason } from '../model/ReportReason';
import { ReportService } from '../services/report.service';
import { ToastrService } from 'ngx-toastr';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-add-report-user',
  templateUrl: './add-report-user.component.html',
  styleUrls: ['./add-report-user.component.css']
})
export class AddReportUserComponent implements OnInit{

  reportReasonForm: FormGroup;
  reportReasons: typeof ReportReason = ReportReason;
  userId!:number;

  constructor(private formBuilder: FormBuilder, private reportService:ReportService,private toastr: ToastrService,private route: ActivatedRoute) {
    this.reportReasonForm = this.formBuilder.group({
      selectedReason: [null, Validators.required] 
    });
  }

  ngOnInit() {
    this.reportReasonForm = this.formBuilder.group({
      selectedReason: [ReportReason.BREAKES_RULES] 
    });

    this.route.params.subscribe(params => {
      this.userId = params['id']; 
  });
  }

  submit() {
    const selectedReason = this.reportReasonForm.get('selectedReason')?.value;
    this.reportService.createReportForUser(this.userId,selectedReason).subscribe(
      (created: any) => {
        console.log('Report created successfully:', created);
        this.toastr.success('Report created successfully!');
        
      },
      (error) => {
        console.error('Error creating report:', error);
        this.toastr.error('Error creating report!');
      }
    );
  }
    
}
