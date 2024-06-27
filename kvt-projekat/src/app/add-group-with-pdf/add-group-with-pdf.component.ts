import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { GroupService } from '../services/group.service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Group } from '../model/group';

@Component({
  selector: 'app-add-group-with-pdf',
  templateUrl: './add-group-with-pdf.component.html',
  styleUrls: ['./add-group-with-pdf.component.css']
})
export class AddGroupWithPDFComponent {

  groupForm: FormGroup;
  pdfFile: File | null = null;

  constructor(
    private fb: FormBuilder,
    private groupService: GroupService,
    private router: Router,
    private toastr: ToastrService,

  ) {
    this.groupForm = this.fb.group({
      name: [null, Validators.required],
      description: [null, Validators.required],
    });
    
  }

  onSubmit() {
    if (this.groupForm.valid) {
      const newGroup: Group = this.groupForm.value;

      const formData = new FormData();
      formData.append('group', new Blob([JSON.stringify(newGroup)], { type: 'application/json' }));
      if (this.pdfFile) {
        formData.append('pdfFile', this.pdfFile, this.pdfFile.name);
      }

      this.groupService.createGroupWithPDF(formData).subscribe(
        (created: Group) => {
          console.log('Group created successfully:', created);
          this.toastr.success('Group created successfully!');
          
        },
        (error) => {
          console.error('Error creating group:', error);
          this.toastr.error('Error creating group!');
        }
      );
    }
  }

  onFileSelected(event: Event): void {
    const inputElement = event.target as HTMLInputElement;
    if (inputElement.files?.length) {
      this.pdfFile = inputElement.files[0];
    }
  }
}
