import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ReportReason } from '../model/ReportReason';
import { ReportService } from '../services/report.service';
import { ToastrService } from 'ngx-toastr';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-add-report-post',
  templateUrl: './add-report-post.component.html',
  styleUrls: ['./add-report-post.component.css']
})
export class AddReportPostComponent implements OnInit{

  reportReasonForm: FormGroup;
  reportReasons: typeof ReportReason = ReportReason;
  postId!:number;

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
      this.postId = params['id']; 
  });
  }

  submit() {
    const selectedReason = this.reportReasonForm.get('selectedReason')?.value;
    this.reportService.createReportForPost(this.postId,selectedReason).subscribe(
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

