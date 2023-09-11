import { Component, OnInit } from '@angular/core';
import { Group } from '../model/group';
import { ImageService } from '../services/image.service';
import { Router } from '@angular/router';
import { GroupService } from '../services/group.service';
import { ToastrService } from 'ngx-toastr';
import { GroupRequestService } from '../services/group-request.service';
import { UserServiceService } from '../services/user.service';
import { GroupAdmin } from '../model/groupAdmin';

@Component({
  selector: 'app-group-admin',
  templateUrl: './group-admin.component.html',
  styleUrls: ['./group-admin.component.css']
})
export class GroupAdminComponent implements OnInit{

  groups!: Group[];
  show: boolean = false;

  constructor(private imageService:ImageService, private router: Router, private groupService:GroupService,private toastr: ToastrService,private userService:UserServiceService){
  }

  ngOnInit(): void {

    console.log("onInit")
  
    this.groupService.getAllGroups().subscribe(
      (data: any) => {
        console.log(data);  
        this.groups = data;   
        this.groups = data.map((group: any) => ({
          ...group,
          creationDate: this.parseDateArrayToDate(group.creationDate)
        }));
        

        this.groups.forEach(group=>{

          this.groupService.getGroupAdmins(group.id).subscribe(
            (data: GroupAdmin[]) => {
              console.log(data);  
              group.groupAdmins = data; 

              group.groupAdmins.forEach(admin=>{
              this.imageService.getProfileImageByUser(admin.user.username).subscribe(
                (image: any) =>{
                  admin.user.profileImage = image;
                }
              )
              })

            }
          )          
          
        })
        

      },
      (error: any) => {
        console.error('Error get groups:', error);
        this.toastr.error('Error get groups!');
    });
  }

  showAdmin(){
    this.show = !this.show;
  }

  remove(groupId:number, adminId:number){

    this.userService.deleteAdminFromGroup(groupId, adminId).subscribe(
      (data: any) => {
        console.log(data);
        window.location.reload();  
      },
      (error: any) => {
        console.error('Error deleting group admin:', error);
        this.toastr.error('Error deleting group admin!');
    });
  }

  getImageUrl(imageName: string): string {
    return `http://localhost:4200/api/images/getImage/${imageName}`;
  }

  parseDateArrayToDate(dateArray: number[]): Date {
    // Destructure the array elements to get individual parts
    const [year, month, day, hour, minute] = dateArray;
    // Note: Months in JavaScript Date are 0-based (0 - January, 1 - February, etc.)
    return new Date(year, month-1, day, hour, minute);
  }
}
