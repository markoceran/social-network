import { Component, OnInit } from '@angular/core';
import { FriendRequestService } from '../services/friend-request.service';
import { ToastrService } from 'ngx-toastr';
import { ActivatedRoute, Router } from '@angular/router';
import { FriendRequest } from '../model/friendRequest';
import { ImageService } from '../services/image.service';

@Component({
  selector: 'app-friend-requests',
  templateUrl: './friend-requests.component.html',
  styleUrls: ['./friend-requests.component.css']
})
export class FriendRequestsComponent implements OnInit{

  requests: Array<FriendRequest> = [];

  constructor(private imageService:ImageService, private router: Router, private friendRequestService:FriendRequestService,private toastr: ToastrService ){
  }

  ngOnInit(): void {

    console.log("onInit")
  
    this.friendRequestService.getRequests().subscribe(
      (data: any) => {
        console.log(data);  
        this.requests = data;   
        this.requests = data.map((request: any) => ({
          ...request,
          createdAt: this.parseDateArrayToDate(request.createdAt)
        }));

        this.requests.forEach(request => {
          this.imageService.getProfileImageByUser(request.from.username).subscribe(
            (image: any) =>{
              request.from.profileImage = image;
              console.log(image);
            }
          )
        })
      },
      (error) => {
        console.error('Error get friend request:', error);
        this.toastr.error('Error get friend request!');
    });


  }

  acceptClick(id:number){
    this.friendRequestService.accept(id).subscribe(
      (data: any) => {
        console.log(data);  
        this.toastr.success('Request was successfully accepted!');

        /*const index = this.requests.findIndex(request => request.id === id);
        this.requests.splice(index, 1);*/
        window.location.reload();
        
      },
      (error) => {
        this.toastr.error('Error accept request!');
        console.error('Error accept request:', error);
        
    });
  }

  denyClick(id:number){
    this.friendRequestService.deny(id).subscribe(
      (data: any) => {
        console.log(data);  
        this.toastr.success('Request was successfully denied!');

        /*const index = this.requests.findIndex(request => request.id === id);
        this.requests.splice(index, 1);*/
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


