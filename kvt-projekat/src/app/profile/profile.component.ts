import { Component, OnInit } from '@angular/core';
import { User } from '../model/user.model';
import { UserServiceService } from '../services/user.service';
import { PostService } from '../services/post.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit{

  user: any; 
  userPosts : any;

  constructor(private userService: UserServiceService, private postService: PostService) {} 

  ngOnInit() {
    
    const username = this.userService.getUsernameFromToken();
    this.userService.getProfileData(username).subscribe(
      (userData: any) => {
        this.user = userData; 
      },
      (error) => {
        console.error('Error fetching user profile data:', error);
      }
    );

    this.postService.getMyPosts(username).subscribe(
      (data: any) => {
        console.log(data);
        this.userPosts = data.map((post: any) => ({
          ...post,
          creationDate: this.parseDateArrayToDate(post.creationDate)
        }));
        console.log(this.userPosts);
      },
      (error) => {
        console.error('Error fetching user posts data:', error);
      }
    );

  }

  parseDateArrayToDate(dateArray: number[]): Date {
    // Destructure the array elements to get individual parts
    const [year, month, day, hour, minute] = dateArray;
    // Note: Months in JavaScript Date are 0-based (0 - January, 1 - February, etc.)
    return new Date(year, month-1, day, hour, minute);
  }
  

  
  
}

