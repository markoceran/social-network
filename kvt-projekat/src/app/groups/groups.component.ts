import { Component } from '@angular/core';
import { Group } from '../model/group';
import { Router } from '@angular/router';
import { GroupService } from '../services/group.service';
import { ToastrService } from 'ngx-toastr';
import { GroupRequestService } from '../services/group-request.service';
import { GroupRequest } from '../model/groupRequest';
import { ImageService } from '../services/image.service';

@Component({
  selector: 'app-groups',
  templateUrl: './groups.component.html',
  styleUrls: ['./groups.component.css']
})
export class GroupsComponent {

  groups!: Group[];
  searchGroup!: Group[];
  search: boolean = false;
  show: boolean = false;
  groupRequests!: GroupRequest[];

  constructor(private imageService:ImageService, private router: Router, private groupService:GroupService,private toastr: ToastrService,private groupRequestService:GroupRequestService ){
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

        this.groups.forEach(group => {

        this.groupRequestService.getRequests(group.id).subscribe(
          (requests: GroupRequest[]) =>{

            requests.forEach(request=>{
              this.imageService.getProfileImageByUser(request.from.username).subscribe(
                (image: any) =>{
                  request.from.profileImage = image;
                  console.log(image);
                }
              )
            })

            group.groupRequests = requests.map((request: any) => ({
              ...request,
              createdAt: this.parseDateArrayToDate(request.createdAt)
            }));
           
          }
        )

      },
      (error: any) => {
        console.error('Error get groups:', error);
        this.toastr.error('Error get groups!');
    })});


  }

  addGroup(){
    this.router.navigate(['group/add']);
  }

  findGroup(){

    this.search = true;

    const unos = document.querySelector('#unos') as HTMLInputElement;

    this.groupService.findGroup(unos.value).subscribe(
      (data: any) => {
        console.log(data);  
        this.searchGroup = data;   
        this.searchGroup = data.map((group: any) => ({
          ...group,
          creationDate: this.parseDateArrayToDate(group.creationDate)
        }));
      },
      (error) => {
        console.error('Error get groups:', error);
        this.toastr.error('Error get groups!');
    });
  }

  addRequest(id:number){

    this.groupRequestService.createRequest(id).subscribe(
      (data: any) => {
        console.log(data);  
        this.toastr.success('Request successfully created!');
      },
      (error) => {
        console.error('Error creating group request:', error);
        this.toastr.error('Error creating group request!');
    });
  }

  showRequests(){

    this.show = !this.show;

  }

  accept(id:number){
    this.groupRequestService.accept(id).subscribe(
      (data: any) => {
        console.log(data);  
        this.toastr.success('Request was successfully accepted!');
        window.location.reload();
        
      },
      (error) => {
        this.toastr.error('Error accept request!');
        console.error('Error accept request:', error);
        
    });
  }

  deny(id:number){
    this.groupRequestService.deny(id).subscribe(
      (data: any) => {
        console.log(data);  
        this.toastr.success('Request was successfully denied!');
        window.location.reload();
        
      },
      (error) => {
        this.toastr.error('Error deny request!');
        console.error('Error deny request:', error);
        
    });
  }



  parseDateArrayToDate(dateArray: number[]): Date {
    // Destructure the array elements to get individual parts
    const [year, month, day, hour, minute] = dateArray;
    // Note: Months in JavaScript Date are 0-based (0 - January, 1 - February, etc.)
    return new Date(year, month-1, day, hour, minute);
  }

  getImageUrl(imageName: string): string {
    return `http://localhost:4200/api/images/getImage/${imageName}`;
  }

}
