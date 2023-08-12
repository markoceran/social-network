import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserServiceService } from '../services/user.service';
import { User } from '../model/user.model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { FriendRequestService } from '../services/friend-request.service';
import { ToastrService } from 'ngx-toastr';
import { FriendRequest } from '../model/friendRequest';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit{

  input:any;
  searchUsers: Array<User> = [];
  logedUser!: User;
  requests: Array<FriendRequest> = [];

  constructor(private route: ActivatedRoute, private userService: UserServiceService,private fb: FormBuilder,private friendRequestService:FriendRequestService,private toastr: ToastrService ){
  }


  ngOnInit() {
    
    console.log("OnInit");
    this.route.params.subscribe(params => {
        this.input = params['input']; 
    });

    
    this.userService.findUsers(this.input).subscribe(
    (userData: any) => {
      this.searchUsers = userData; 
      console.log(this.searchUsers);
    },
    (error) => {
      console.error('Error fetching user data:', error);
    });

    this.userService.getProfileData(this.userService.getUsernameFromToken()).subscribe(
      (userData: any) => {
        this.logedUser = userData; 
        console.log(this.logedUser);
      },
      (error) => {
        console.error('Error fetching user data:', error);
      });

    this.friendRequestService.getAll().subscribe(
        (userData: any) => {
          this.requests = userData; 
          console.log(this.requests);
        },
        (error) => {
          console.error('Error fetching user data:', error);
    });

}

isFriend(user: any): boolean {
  if (this.logedUser && this.logedUser.friendsWith) {
    return this.logedUser.friendsWith.some(friend => friend.id === user.id);
  }
  return false;
}

isAlreadySend(user: any): boolean {
  if (this.logedUser) {
    return this.requests.some(r => r.approved === false &&  r.from.id === this.logedUser.id && r.forr.id === user.id);
  }
  return false;
}



addFriend(id:number) {
    console.log("onSubmit")
    console.log(id);
    this.friendRequestService.createRequest(id).subscribe(
      (created: any) => {
        console.log(created);
        this.toastr.success('Friend request created successfully!');
        window.location.reload();       
      },
      (error) => {
        console.error('Error creating friend request:', error);
        this.toastr.error('Error creating friend request!');
      }
    );
  
}

}
