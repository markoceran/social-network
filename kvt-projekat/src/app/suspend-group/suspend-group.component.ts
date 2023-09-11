import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { GroupService } from '../services/group.service';

@Component({
  selector: 'app-suspend-group',
  templateUrl: './suspend-group.component.html',
  styleUrls: ['./suspend-group.component.css']
})
export class SuspendGroupComponent implements OnInit{

  groupId!:number;

  constructor(private fb: FormBuilder,private route: ActivatedRoute, private groupService: GroupService, private toast:ToastrService, private router:Router) {
  
  } 

  ngOnInit(): void {

    this.route.params.subscribe(params => {
        this.groupId = params['id']; 
    });
  }

  onSubmit(){

    const reason = document.querySelector('#reason') as HTMLInputElement;

    if(reason.value !== ''){
      this.groupService.suspendGroup(this.groupId, reason.value).subscribe(
        (data: any) => {
          this.toast.success('Group successfully suspended!');
          this.router.navigate(['/groupAdmin']);
        },
        (error) => {
          console.error('Error suspending group:', error);
        }
    );
    }
  }
}
