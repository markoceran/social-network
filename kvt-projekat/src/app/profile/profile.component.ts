import { Component, OnInit } from '@angular/core';
import { User } from '../model/user.model';
import { UserServiceService } from '../services/user.service';
import { PostService } from '../services/post.service';
import { Router } from '@angular/router';
import { ImageService } from '../services/image.service';
import { Post } from '../model/post.model';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit{

  user!: User; 
  userPosts! : Post[];

  constructor(private userService: UserServiceService, private postService: PostService, private router:Router,private imageService:ImageService) {} 

  ngOnInit() {
    
    const username = this.userService.getUsernameFromToken();
    this.userService.getProfileData(username).subscribe(
      (userData: any) => {
        this.user = userData; 

        this.imageService.getProfileImage().subscribe(
        (image: any) => {
          this.user.profileImage = image; 
        },
        (error) => {
          console.log("test test test")
          console.error('Error fetching user profile image:', error);
        }
    );
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
        this.userPosts.forEach(post => {
        this.imageService.getPostImage(post.id).subscribe(
        (images: any) =>{
          post.images = images;
          console.log(images);
        }
      )
    })
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
  
  addProfileImage(){
    this.router.navigate(['addProfileImage']);
  }

  getImageUrl(imageName: string): string {
    return `http://localhost:4200/api/images/getImage/${imageName}`;
  }
  
  
}

