import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Group } from '../model/group';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { GroupService } from '../services/group.service';
import { UserServiceService } from '../services/user.service';

@Component({
  selector: 'app-edit-group',
  templateUrl: './edit-group.component.html',
  styleUrls: ['./edit-group.component.css']
})
export class EditGroupComponent implements OnInit{

  editGroup: FormGroup;
  group!:Group;
  groupId!:number;
  

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private toastr: ToastrService,
    private groupService: GroupService,
    private route: ActivatedRoute

  ) {
    this.editGroup = this.fb.group({
      name: [null, Validators.required],
      description: [null, Validators.required]
    });

  }

  ngOnInit(): void {

    this.route.params.subscribe(params => {
      this.groupId = params['id']; 
    });

    this.groupService.getGroupById(this.groupId).subscribe(
      (group: Group) => {
        this.group = group;
      },
      (error) => {
        console.error('Error getting group:', error);
        this.toastr.error('Error getting group!');
      }
    );
  }

  submit() {

    if (this.editGroup.valid) {

      const editedGroup: Group = this.editGroup.value;
      editedGroup.contains = this.group.contains;
      editedGroup.creationDate = this.group.creationDate;
      editedGroup.isSuspended = this.group.isSuspended;
      editedGroup.suspendedReason = this.group.suspendedReason;

      this.groupService.updateGroup(this.groupId, editedGroup).subscribe(
        (group: Group) => {

          console.log('Group data successfully changed:', group);
          this.toastr.success('Group data successfully changed!');
          this.router.navigate(['mainPageGroup/', this.groupId]);
          
        },
        (error) => {
          console.error('Error changing group data:', error);
          this.toastr.error('Error changing group data!');
          window.location.reload();
        }
      );
    }
  
  }

}
