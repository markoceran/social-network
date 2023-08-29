import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ReportReason } from '../model/ReportReason';
import { ReportService } from '../services/report.service';
import { ToastrService } from 'ngx-toastr';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-add-report-comment',
  templateUrl: './add-report-comment.component.html',
  styleUrls: ['./add-report-comment.component.css']
})
export class AddReportCommentComponent implements OnInit{

  reportReasonForm: FormGroup;
  reportReasons: typeof ReportReason = ReportReason;
  commentId!:number;

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
      this.commentId = params['id']; 
  });
  }

  submit() {
    const selectedReason = this.reportReasonForm.get('selectedReason')?.value;
    this.reportService.createReportForComment(this.commentId,selectedReason).subscribe(
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
