import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CommentService } from '../services/comment.service';
import { ActivatedRoute, Router } from '@angular/router';
import { UserServiceService } from '../services/user.service';
import { ReactionService } from '../services/reaction.service';
import { PostService } from '../services/post.service';
import { ToastrService } from 'ngx-toastr';
import { ImageService } from '../services/image.service';
import { Comments } from '../model/comment';

@Component({
  selector: 'app-add-reply-on-comment',
  templateUrl: './add-reply-on-comment.component.html',
  styleUrls: ['./add-reply-on-comment.component.css']
})
export class AddReplyOnCommentComponent implements OnInit{

  replyForm!: FormGroup;
  commentId!:number;

  constructor(private commentService:CommentService, private fb: FormBuilder,private route: ActivatedRoute, private userService: UserServiceService, private postService: PostService, private reactionService: ReactionService, private toast:ToastrService, private imageService:ImageService,private router:Router) {
    this.replyForm = this.fb.group({
      text: [null, Validators.required]
    });
  } 

  ngOnInit(): void {

    this.route.params.subscribe(params => {
        this.commentId = params['id']; 
    });

  }


  onSubmit(){
    if (this.replyForm.valid) {
      const newComment: Comments = this.replyForm.value;

      this.commentService.reply(newComment, this.commentId).subscribe(
        (created: Comments) => {
          console.log('Reply created successfully:', created);
          this.toast.success('Reply created successfully!');
        },
        (error) => {
          console.error('Error creating comment:', error);
          this.toast.error('Error creating comment!');
        }
      );
    }
  }
}
