import { Component, OnInit } from '@angular/core';
import { UserServiceService } from '../services/user.service';
import { PostService } from '../services/post.service';
import { User } from '../model/user.model';
import { Post } from '../model/post.model';
import { ReactionService } from '../services/reaction.service';
import { Toast, ToastrService } from 'ngx-toastr';
import { Reaction } from '../model/reaction';
import { ImageService } from '../services/image.service';
import { Router } from '@angular/router';
import { GroupService } from '../services/group.service';
import { Group } from '../model/group';

@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.css']
})
export class MainPageComponent implements OnInit{

  friendPosts!: Post[]; 
  myPosts!: Post[];
  postsFromMyGroups: Post[] = [];
  user!: User;
  allPosts!: Post[];

  isLiked: boolean = false;
  isDisliked: boolean = false;
  isHearted: boolean = false;

  constructor(private groupService:GroupService, private userService: UserServiceService, private postService: PostService, private reactionService: ReactionService, private toast:ToastrService, private imageService:ImageService,private router:Router) {} 

  ngOnInit() {
    const username = this.userService.getUsernameFromToken();
    
    this.postService.getMyPosts(username).subscribe(
      (userData: any) => {
        this.myPosts = userData.map((post: any) => ({
          ...post,
          creationDate: this.parseDateArrayToDate(post.creationDate)
        }));
        this.fetchOtherPosts(username); 
      },
      (error) => {
        console.error('Error fetching user data:', error);
      }
    );
  }
  
  fetchOtherPosts(username: string) {
    this.postService.getFriendsPosts(username).subscribe(
      (data: any) => {
        this.friendPosts = data.map((post: any) => ({
          ...post,
          creationDate: this.parseDateArrayToDate(post.creationDate)
        }));

        this.groupService.getMyGroups().subscribe(
          (groups: any) => {
            groups.forEach((g: Group)=>{
              g.contains.forEach((p: Post)=>{
                p.group = g;
                this.postsFromMyGroups.push(p);
              })
            })

            this.postsFromMyGroups = this.postsFromMyGroups.map((post: any) => ({
              ...post,
              creationDate: this.parseDateArrayToDate(post.creationDate)
            }));

            this.mergePosts();

            this.allPosts.forEach(post => {
              this.reactionService.getPostReaction(post.id).subscribe(
                (reaction: Reaction[]) => {
                  
                  if(reaction.length>0){
                    reaction.forEach(r => {
    
                    if(r.madeBy.username === this.userService.getUsernameFromToken()){
                    console.log(r);
                    if(r.type === "LIKE"){
                      post.isLiked = true;
                    }else if(r.type === "DISLIKE"){
                      post.isDisliked = true;
                    }else{ post.isHearted = true;}
    
                  }
                  });  
                }
                            
                }
              )
              this.imageService.getPostImage(post.id).subscribe(
                (images: any) =>{
                  post.images = images;
                  console.log(images);
                }
              )
              this.imageService.getProfileImageByUser(post.postedBy.username).subscribe(
                (image: any) =>{
                  post.postedBy.profileImage = image;
                  console.log(image);
                }
              )
            });

          }
        );


      },
      (error) => {
        console.error('Error fetching user data:', error);
      }
    );
  }
  

  heart(id: number, postIndex: number){

    this.reactionService.heartPost(id).subscribe(
      (data: any) => {
        
        console.log(data);
        this.toast.success('You add heart on post');
        this.allPosts[postIndex].isHearted = true;
    
        this.allPosts[postIndex].isLiked = false;
        this.allPosts[postIndex].isDisliked = false;
      },
      (error) => {
        console.error('Error add heart on post', error);
      }
    );

  }

  like(id: number, postIndex: number){

    this.reactionService.likePost(id).subscribe(
      (data: any) => {
       
        console.log(data);
        this.toast.success('You add like on post');
        this.allPosts[postIndex].isLiked = true;
       
        this.allPosts[postIndex].isHearted = false;
        this.allPosts[postIndex].isDisliked = false;
      },
      (error) => {
        console.error('Error add like on post', error);
      }
    );
  }

  dislike(id: number, postIndex: number){
    
    this.reactionService.dislikePost(id).subscribe(
      (data: any) => {
        
        console.log(data);
        this.toast.success('You add dislike on post');
        this.allPosts[postIndex].isDisliked = true;
        
        this.allPosts[postIndex].isHearted = false;
        this.allPosts[postIndex].isLiked = false;
      },
      (error) => {
        console.error('Error add dislike on post', error);
      }
    );
  }

  comments(postId:number){
    this.router.navigate(['comments/', postId]);
  }

  
  getImageUrl(imageName: string): string {
    return `http://localhost:4200/api/images/getImage/${imageName}`;
  }

  mergePosts() {
    this.allPosts = [...this.friendPosts, ...this.myPosts, ...this.postsFromMyGroups]; 
    this.shuffleArray(this.allPosts);
    console.log(this.allPosts); 
  }

  parseDateArrayToDate(dateArray: number[]): Date {
    // Destructure the array elements to get individual parts
    const [year, month, day, hour, minute] = dateArray;
    // Note: Months in JavaScript Date are 0-based (0 - January, 1 - February, etc.)
    return new Date(year, month - 1, day, hour, minute);
  }

  parseDateArrayToDate2(dateArray: number[]): Date {
    // Destructure the array elements to get individual parts
    const [year, month, day, hour, minute] = dateArray;
    // Note: Months in JavaScript Date are 0-based (0 - January, 1 - February, etc.)
    return new Date(year, month - 1, day, hour+2, minute);
  }

  shuffleArray(array: any[]) {
    for (let i = array.length - 1; i > 0; i--) {
      const j = Math.floor(Math.random() * (i + 1));
      [array[i], array[j]] = [array[j], array[i]]; // Swap elements
    }
  }

  deleteReaction(postId:number){

    const username = this.userService.getUsernameFromToken();
    this.reactionService.deleteReaction(postId, username).subscribe(
      (data: any) =>{
        console.log(data);
        window.location.reload();
      }
    )
  }

  orderAsc(){

    console.log(this.allPosts);
    
    this.postService.orderAsc(this.allPosts).subscribe(
      (data: Post[]) => {
        console.log(data);
        this.toast.success('Order success');
        this.allPosts = data.map((post: any) => ({
          ...post,
          creationDate: this.parseDateArrayToDate2(post.creationDate)
        }));

        this.allPosts.forEach(post => {
          this.reactionService.getPostReaction(post.id).subscribe(
            (reaction: Reaction[]) => {
              
              if(reaction.length>0){
                reaction.forEach(r => {

                if(r.madeBy.username === this.userService.getUsernameFromToken()){
                console.log(r);
                if(r.type === "LIKE"){
                  post.isLiked = true;
                }else if(r.type === "DISLIKE"){
                  post.isDisliked = true;
                }else{ post.isHearted = true;}

              }
              });  
            }
                        
            }
          )
          this.imageService.getPostImage(post.id).subscribe(
            (images: any) =>{
              post.images = images;
              console.log(images);
            }
          )
          this.imageService.getProfileImageByUser(post.postedBy.username).subscribe(
            (image: any) =>{
              post.postedBy.profileImage = image;
              console.log(image);
            }
          )
        });
      },
      (error) => {
        console.error('Order error', error);
      }
    );
    
  }

  orderDesc(){

    console.log(this.allPosts);
    
    this.postService.orderDesc(this.allPosts).subscribe(
      (data: Post[]) => {
        console.log(data);
        this.toast.success('Order success');
        this.allPosts = data.map((post: any) => ({
          ...post,
          creationDate: this.parseDateArrayToDate2(post.creationDate)
        }));

        this.allPosts.forEach(post => {
          this.reactionService.getPostReaction(post.id).subscribe(
            (reaction: Reaction[]) => {
              
              if(reaction.length>0){
                reaction.forEach(r => {

                if(r.madeBy.username === this.userService.getUsernameFromToken()){
                console.log(r);
                if(r.type === "LIKE"){
                  post.isLiked = true;
                }else if(r.type === "DISLIKE"){
                  post.isDisliked = true;
                }else{ post.isHearted = true;}

              }
              });  
            }
                        
            }
          )
          this.imageService.getPostImage(post.id).subscribe(
            (images: any) =>{
              post.images = images;
              console.log(images);
            }
          )
          this.imageService.getProfileImageByUser(post.postedBy.username).subscribe(
            (image: any) =>{
              post.postedBy.profileImage = image;
              console.log(image);
            }
          )
        });
      },
      (error) => {
        console.error('Order error', error);
      }
    );
    
  }
  

}
