import { Component, OnInit } from '@angular/core';
import { UserServiceService } from '../services/user.service';
import { PostService } from '../services/post.service';
import { User } from '../model/user.model';
import { Post } from '../model/post.model';

@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.css']
})
export class MainPageComponent implements OnInit{

  friendPosts!: Post[]; 
  myPosts!: Post[];
  user!: User;
  allPosts!: Post[];

  constructor(private userService: UserServiceService, private postService: PostService) {} 

  ngOnInit() {
    const username = this.userService.getUsernameFromToken();
    
    this.postService.getMyPosts(username).subscribe(
      (userData: any) => {
        this.myPosts = userData.map((post: any) => ({
          ...post,
          creationDate: this.parseDateArrayToDate(post.creationDate)
        }));
        this.fetchFriendPosts(username); 
      },
      (error) => {
        console.error('Error fetching user data:', error);
      }
    );
  }
  
  fetchFriendPosts(username: string) {
    this.postService.getFriendsPosts(username).subscribe(
      (data: any) => {
        this.friendPosts = data.map((post: any) => ({
          ...post,
          creationDate: this.parseDateArrayToDate(post.creationDate)
        }));
        this.mergePosts();
      },
      (error) => {
        console.error('Error fetching user data:', error);
      }
    );
  }
  
  mergePosts() {
    this.allPosts = [...this.friendPosts, ...this.myPosts]; 
    this.shuffleArray(this.allPosts);
    console.log(this.allPosts); 
  }

  parseDateArrayToDate(dateArray: number[]): Date {
    // Destructure the array elements to get individual parts
    const [year, month, day, hour, minute] = dateArray;
    // Note: Months in JavaScript Date are 0-based (0 - January, 1 - February, etc.)
    return new Date(year, month-1, day, hour, minute);
  }

  shuffleArray(array: any[]) {
    for (let i = array.length - 1; i > 0; i--) {
      const j = Math.floor(Math.random() * (i + 1));
      [array[i], array[j]] = [array[j], array[i]]; // Swap elements
    }
  }
  

}
