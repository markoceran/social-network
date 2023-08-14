import { Component } from '@angular/core';
import { Group } from '../model/group';
import { Router } from '@angular/router';
import { GroupService } from '../services/group.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-groups',
  templateUrl: './groups.component.html',
  styleUrls: ['./groups.component.css']
})
export class GroupsComponent {

  groups: Array<Group> = [];

  constructor(private router: Router, private groupService:GroupService,private toastr: ToastrService ){
  }

  ngOnInit(): void {

    console.log("onInit")
  
    this.groupService.getMyGroups().subscribe(
      (data: any) => {
        console.log(data);  
        this.groups = data;   
        this.groups = data.map((group: any) => ({
          ...group,
          creationDate: this.parseDateArrayToDate(group.creationDate)
        }));
      },
      (error) => {
        console.error('Error get groups:', error);
        this.toastr.error('Error get groups!');
    });


  }

  addGroup(){
    this.router.navigate(['group/add']);
  }

  parseDateArrayToDate(dateArray: number[]): Date {
    // Destructure the array elements to get individual parts
    const [year, month, day, hour, minute] = dateArray;
    // Note: Months in JavaScript Date are 0-based (0 - January, 1 - February, etc.)
    return new Date(year, month-1, day, hour, minute);
  }
}
