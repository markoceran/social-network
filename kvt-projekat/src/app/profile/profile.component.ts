import { Component, OnInit } from '@angular/core';
import { User } from '../model/user.model';
import { UserServiceService } from '../services/user.service';
import { PostService } from '../services/post.service';
import { Router } from '@angular/router';
import { ImageService } from '../services/image.service';
import { Post } from '../model/post.model';
import { CommentService } from '../services/comment.service';
import { ReactionService } from '../services/reaction.service';
import { Reaction } from '../model/reaction';
import { Comments } from '../model/comment';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit{

  user!: User; 
  userPosts! : Post[];
  showReactions: boolean = false;
  showComments: boolean = false;

  constructor(private commentService:CommentService,private reactionService:ReactionService, private userService: UserServiceService, private postService: PostService, private router:Router,private imageService:ImageService) {} 

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
      this.commentService.getComments(post.id).subscribe(
        (comments: Comments[]) =>{
          post.comments = comments.map((comment: any) => ({
            ...comment,
            timestamp: this.parseDateArrayToDateWithoutTime(comment.timestamp)
          }))
          console.log(comments);
        }
    )
    this.reactionService.getPostReaction(post.id).subscribe(
      (reactions: Reaction[]) =>{
        post.reactions = reactions;
        console.log(reactions);
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

  parseDateArrayToDateWithoutTime(dateArray: number[]): Date {
    // Destructure the array elements to get individual parts
    const [year, month, day] = dateArray;
    // Note: Months in JavaScript Date are 0-based (0 - January, 1 - February, etc.)
    return new Date(year, month-1, day);
  }
  
  addProfileImage(){
    this.router.navigate(['addProfileImage']);
  }

  getImageUrl(imageName: string): string {
    return `http://localhost:4200/api/images/getImage/${imageName}`;
  }

  editProfile(){
    this.router.navigate(['editProfile']);
  }
  
  showReaction(){
    this.showReactions = !this.showReactions
  }
  showComment(){
    this.showComments = !this.showComments
  }
  
}

