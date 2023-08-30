import { AfterViewInit, Component, OnInit } from '@angular/core';
import { UserServiceService } from '../services/user.service';
import { PostService } from '../services/post.service';
import { ReactionService } from '../services/reaction.service';
import { ToastrService } from 'ngx-toastr';
import { ImageService } from '../services/image.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CommentService } from '../services/comment.service';
import { Comments } from '../model/comment';
import { Reaction } from '../model/reaction';


@Component({
  selector: 'app-comment',
  templateUrl: './comment.component.html',
  styleUrls: ['./comment.component.css']
})
export class CommentComponent implements OnInit{

  commentForm: FormGroup;
  postId!:number;
  comments!:Comments[];
  showReactions: boolean = false;

  
  constructor(private commentService:CommentService, private fb: FormBuilder,private route: ActivatedRoute, private userService: UserServiceService, private postService: PostService, private reactionService: ReactionService, private toast:ToastrService, private imageService:ImageService,private router:Router) {
    this.commentForm = this.fb.group({
      text: [null, Validators.required]
    });
  } 
 
  ngOnInit(): void {

    console.log("OnInit");

    this.route.params.subscribe(params => {
        this.postId = params['postId']; 
    });

    this.commentService.getComments(this.postId).subscribe(
      (data: any) => {
        this.comments = data.map((comment: any) => ({
          ...comment,
          timestamp: this.parseDateArrayToDate(comment.timestamp)
          
      }));
      this.comments.forEach(comment => {

        this.imageService.getProfileImageByUser(comment.belongsTo.username).subscribe(
          (image: any) =>{
            comment.belongsTo.profileImage = image;
            console.log(image);
          }
        )

        this.reactionService.getCommentReaction(comment.id).subscribe(
          (reaction: Reaction[]) => {
            
            if(reaction.length>0){
              
              comment.reactions = reaction;

              reaction.forEach(r => {

              if(r.madeBy.username === this.userService.getUsernameFromToken()){
              console.log(r);
              if(r.type === "LIKE"){
                comment.isLiked = true;
              }else if(r.type === "DISLIKE"){
                comment.isDisliked = true;
              }else{ comment.isHearted = true;}

            }
            });  
          }
                      
          }
        )
      })
    },
      (error) => {
        console.error('Error fetching comment data:', error);
  });

  }


  onSubmit(){
    if (this.commentForm.valid) {
      const newComment: Comments = this.commentForm.value;
      this.commentService.createComment(newComment,this.postId).subscribe(
        (created: Comments) => {
          console.log('Comment created successfully:', created);
          this.toast.success('Comment created successfully!');
          window.location.reload();
        },
        (error) => {
          console.error('Error creating comment:', error);
          this.toast.error('Error creating comment!');
        }
      );
    }
  }

  heart(id: number, commentIndex: number){

    this.reactionService.heartComment(id).subscribe(
      (data: any) => {
        
        console.log(data);
        this.toast.success('You add heart on comment');
        this.comments[commentIndex].isHearted = true;
    
        this.comments[commentIndex].isLiked = false;
        this.comments[commentIndex].isDisliked = false;
      },
      (error) => {
        console.error('Error add heart on comment', error);
      }
    );

  }

  like(id: number, commentIndex: number){

    this.reactionService.likeComment(id).subscribe(
      (data: any) => {
       
        console.log(data);
        this.toast.success('You add like on comment');
        this.comments[commentIndex].isLiked = true;
       
        this.comments[commentIndex].isHearted = false;
        this.comments[commentIndex].isDisliked = false;
      },
      (error) => {
        console.error('Error add like on comment', error);
      }
    );
  }

  dislike(id: number, commentIndex: number){
    
    this.reactionService.dislikeComment(id).subscribe(
      (data: any) => {
        
        console.log(data);
        this.toast.success('You add dislike on comment');
        this.comments[commentIndex].isDisliked = true;
        
        this.comments[commentIndex].isHearted = false;
        this.comments[commentIndex].isLiked = false;
      },
      (error) => {
        console.error('Error add dislike on comment', error);
      }
    );
  }

  getImageUrl(imageName: string): string {
    return `http://localhost:4200/api/images/getImage/${imageName}`;
  }

  parseDateArrayToDate(dateArray: number[]): Date {
    // Destructure the array elements to get individual parts
    const [year, month, day] = dateArray;
    // Note: Months in JavaScript Date are 0-based (0 - January, 1 - February, etc.)
    return new Date(year, month-1, day);
  }

  parseDateArrayToDate2(dateArray: number[]): Date {
    // Destructure the array elements to get individual parts
    const [year, month, day] = dateArray;
    // Note: Months in JavaScript Date are 0-based (0 - January, 1 - February, etc.)
    return new Date(year, month-1, day+1);
  }
  

  show(){
    this.showReactions = !this.showReactions;
  }




  orderAscLikes(){

    console.log(this.comments);
    
    this.commentService.orderAscLikes(this.comments).subscribe(
      (data: Comments[]) => {
        console.log(data);
        this.toast.success('Order success');
        this.comments = data.map((comment: any) => ({
          ...comment,
          timestamp: this.parseDateArrayToDate2(comment.timestamp)
          
        }));

        this.comments.forEach(comment => {

          this.imageService.getProfileImageByUser(comment.belongsTo.username).subscribe(
            (image: any) =>{
              comment.belongsTo.profileImage = image;
              console.log(image);
            }
          )
  
          this.reactionService.getCommentReaction(comment.id).subscribe(
            (reaction: Reaction[]) => {
              
              if(reaction.length>0){
                
                comment.reactions = reaction;
  
                reaction.forEach(r => {
  
                if(r.madeBy.username === this.userService.getUsernameFromToken()){
                console.log(r);
                if(r.type === "LIKE"){
                  comment.isLiked = true;
                }else if(r.type === "DISLIKE"){
                  comment.isDisliked = true;
                }else{ comment.isHearted = true;}
  
              }
              });  
            }
                        
            }
          )
        })
      },
      (error) => {
        console.error('Order error', error);
      }
    );
  }

  orderAscDislikes(){

    console.log(this.comments);
    
    this.commentService.orderAscDislikes(this.comments).subscribe(
      (data: Comments[]) => {
        console.log(data);
        this.toast.success('Order success');
        this.comments = data.map((comment: any) => ({
          ...comment,
          timestamp: this.parseDateArrayToDate2(comment.timestamp)
          
        }));

        this.comments.forEach(comment => {

          this.imageService.getProfileImageByUser(comment.belongsTo.username).subscribe(
            (image: any) =>{
              comment.belongsTo.profileImage = image;
              console.log(image);
            }
          )
  
          this.reactionService.getCommentReaction(comment.id).subscribe(
            (reaction: Reaction[]) => {
              
              if(reaction.length>0){
                
                comment.reactions = reaction;
  
                reaction.forEach(r => {
  
                if(r.madeBy.username === this.userService.getUsernameFromToken()){
                console.log(r);
                if(r.type === "LIKE"){
                  comment.isLiked = true;
                }else if(r.type === "DISLIKE"){
                  comment.isDisliked = true;
                }else{ comment.isHearted = true;}
  
              }
              });  
            }
                        
            }
          )
        })
      },
      (error) => {
        console.error('Order error', error);
      }
    );
  }

  orderAscHearts(){

    console.log(this.comments);
    
    this.commentService.orderAscHearts(this.comments).subscribe(
      (data: Comments[]) => {
        console.log(data);
        this.toast.success('Order success');
        this.comments = data.map((comment: any) => ({
          ...comment,
          timestamp: this.parseDateArrayToDate2(comment.timestamp)
          
        }));

        this.comments.forEach(comment => {

          this.imageService.getProfileImageByUser(comment.belongsTo.username).subscribe(
            (image: any) =>{
              comment.belongsTo.profileImage = image;
              console.log(image);
            }
          )
  
          this.reactionService.getCommentReaction(comment.id).subscribe(
            (reaction: Reaction[]) => {
              
              if(reaction.length>0){
                
                comment.reactions = reaction;
  
                reaction.forEach(r => {
  
                if(r.madeBy.username === this.userService.getUsernameFromToken()){
                console.log(r);
                if(r.type === "LIKE"){
                  comment.isLiked = true;
                }else if(r.type === "DISLIKE"){
                  comment.isDisliked = true;
                }else{ comment.isHearted = true;}
  
              }
              });  
            }
                        
            }
          )
        })
      },
      (error) => {
        console.error('Order error', error);
      }
    );
  }



  orderDescLikes(){

    console.log(this.comments);
    
    this.commentService.orderDescLikes(this.comments).subscribe(
      (data: Comments[]) => {
        console.log(data);
        this.toast.success('Order success');
        this.comments = data.map((comment: any) => ({
          ...comment,
          timestamp: this.parseDateArrayToDate2(comment.timestamp)
          
        }));

        this.comments.forEach(comment => {

          this.imageService.getProfileImageByUser(comment.belongsTo.username).subscribe(
            (image: any) =>{
              comment.belongsTo.profileImage = image;
              console.log(image);
            }
          )
  
          this.reactionService.getCommentReaction(comment.id).subscribe(
            (reaction: Reaction[]) => {
              
              if(reaction.length>0){
                
                comment.reactions = reaction;
  
                reaction.forEach(r => {
  
                if(r.madeBy.username === this.userService.getUsernameFromToken()){
                console.log(r);
                if(r.type === "LIKE"){
                  comment.isLiked = true;
                }else if(r.type === "DISLIKE"){
                  comment.isDisliked = true;
                }else{ comment.isHearted = true;}
  
              }
              });  
            }
                        
            }
          )
        })
      },
      (error) => {
        console.error('Order error', error);
      }
    );
  }

  orderDescDislikes(){

    console.log(this.comments);
    
    this.commentService.orderDescDislikes(this.comments).subscribe(
      (data: Comments[]) => {
        console.log(data);
        this.toast.success('Order success');
        this.comments = data.map((comment: any) => ({
          ...comment,
          timestamp: this.parseDateArrayToDate2(comment.timestamp)
          
        }));

        this.comments.forEach(comment => {

          this.imageService.getProfileImageByUser(comment.belongsTo.username).subscribe(
            (image: any) =>{
              comment.belongsTo.profileImage = image;
              console.log(image);
            }
          )
  
          this.reactionService.getCommentReaction(comment.id).subscribe(
            (reaction: Reaction[]) => {
              
              if(reaction.length>0){
                
                comment.reactions = reaction;
  
                reaction.forEach(r => {
  
                if(r.madeBy.username === this.userService.getUsernameFromToken()){
                console.log(r);
                if(r.type === "LIKE"){
                  comment.isLiked = true;
                }else if(r.type === "DISLIKE"){
                  comment.isDisliked = true;
                }else{ comment.isHearted = true;}
  
              }
              });  
            }
                        
            }
          )
        })
      },
      (error) => {
        console.error('Order error', error);
      }
    );
  }

  orderDescHearts(){

    console.log(this.comments);
    
    this.commentService.orderDescHearts(this.comments).subscribe(
      (data: Comments[]) => {
        console.log(data);
        this.toast.success('Order success');
        this.comments = data.map((comment: any) => ({
          ...comment,
          timestamp: this.parseDateArrayToDate2(comment.timestamp)
          
        }));

        this.comments.forEach(comment => {

          this.imageService.getProfileImageByUser(comment.belongsTo.username).subscribe(
            (image: any) =>{
              comment.belongsTo.profileImage = image;
              console.log(image);
            }
          )
  
          this.reactionService.getCommentReaction(comment.id).subscribe(
            (reaction: Reaction[]) => {
              
              if(reaction.length>0){
                
                comment.reactions = reaction;
  
                reaction.forEach(r => {
  
                if(r.madeBy.username === this.userService.getUsernameFromToken()){
                console.log(r);
                if(r.type === "LIKE"){
                  comment.isLiked = true;
                }else if(r.type === "DISLIKE"){
                  comment.isDisliked = true;
                }else{ comment.isHearted = true;}
  
              }
              });  
            }
                        
            }
          )
        })
      },
      (error) => {
        console.error('Order error', error);
      }
    );
  }



  orderAscDate(){

    console.log(this.comments);
    
    this.commentService.orderAscDate(this.comments).subscribe(
      (data: Comments[]) => {
        console.log(data);
        this.toast.success('Order success');
        this.comments = data.map((comment: any) => ({
          ...comment,
          timestamp: this.parseDateArrayToDate2(comment.timestamp)
          
        }));

        this.comments.forEach(comment => {

          this.imageService.getProfileImageByUser(comment.belongsTo.username).subscribe(
            (image: any) =>{
              comment.belongsTo.profileImage = image;
              console.log(image);
            }
          )
  
          this.reactionService.getCommentReaction(comment.id).subscribe(
            (reaction: Reaction[]) => {
              
              if(reaction.length>0){
                
                comment.reactions = reaction;
  
                reaction.forEach(r => {
  
                if(r.madeBy.username === this.userService.getUsernameFromToken()){
                console.log(r);
                if(r.type === "LIKE"){
                  comment.isLiked = true;
                }else if(r.type === "DISLIKE"){
                  comment.isDisliked = true;
                }else{ comment.isHearted = true;}
  
              }
              });  
            }
                        
            }
          )
        })
      },
      (error) => {
        console.error('Order error', error);
      }
    );
  }

  orderDescDate(){

    console.log(this.comments);
    
    this.commentService.orderDescDate(this.comments).subscribe(
      (data: Comments[]) => {
        console.log(data);
        this.toast.success('Order success');
        this.comments = data.map((comment: any) => ({
          ...comment,
          timestamp: this.parseDateArrayToDate2(comment.timestamp)
          
        }));

        this.comments.forEach(comment => {

          this.imageService.getProfileImageByUser(comment.belongsTo.username).subscribe(
            (image: any) =>{
              comment.belongsTo.profileImage = image;
              console.log(image);
            }
          )
  
          this.reactionService.getCommentReaction(comment.id).subscribe(
            (reaction: Reaction[]) => {
              
              if(reaction.length>0){
                
                comment.reactions = reaction;
  
                reaction.forEach(r => {
  
                if(r.madeBy.username === this.userService.getUsernameFromToken()){
                console.log(r);
                if(r.type === "LIKE"){
                  comment.isLiked = true;
                }else if(r.type === "DISLIKE"){
                  comment.isDisliked = true;
                }else{ comment.isHearted = true;}
  
              }
              });  
            }
                        
            }
          )
        })
      },
      (error) => {
        console.error('Order error', error);
      }
    );
  }
  
}
