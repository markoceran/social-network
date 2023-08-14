import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { GroupService } from '../services/group.service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Group } from '../model/group';

@Component({
  selector: 'app-add-group',
  templateUrl: './add-group.component.html',
  styleUrls: ['./add-group.component.css']
})
export class AddGroupComponent {

  groupForm: FormGroup;

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
      this.groupService.createGroup(newGroup).subscribe(
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
}
