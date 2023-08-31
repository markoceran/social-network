import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Post } from '../model/post.model';
import { ToastrService } from 'ngx-toastr';
import { ActivatedRoute, Router } from '@angular/router';
import { PostService } from '../services/post.service';
import { UserServiceService } from '../services/user.service';

@Component({
  selector: 'app-edit-post',
  templateUrl: './edit-post.component.html',
  styleUrls: ['./edit-post.component.css']
})
export class EditPostComponent implements OnInit{

  editPost: FormGroup;
  post!:Post;
  postId!:number;
  username:string;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private toastr: ToastrService,
    private postService: PostService,
    private route: ActivatedRoute,
    private userService:UserServiceService

  ) {
    this.editPost = this.fb.group({
      content: [null, Validators.required]
    });

    this.username = userService.getUsernameFromToken();
  }

  ngOnInit(): void {

    this.route.params.subscribe(params => {
      this.postId = params['id']; 
  });

    this.postService.getPostById(this.postId).subscribe(
      (post: Post) => {
        this.post = post;
      },
      (error) => {
        console.error('Error get post:', error);
        this.toastr.error('Error get post!');
      }
    );
  }

  submit() {

    if (this.editPost.valid) {

      const editedPost: Post = this.editPost.value;
      editedPost.isDeleted = false;

      this.postService.updatePost(this.postId, editedPost).subscribe(
        (post: Post) => {

          console.log('Post data successfully changed:', post);
          this.toastr.success('Post data successfully changed!');
          this.router.navigate(['profile/', this.username]);
          
        },
        (error) => {
          console.error('Error changing post data:', error);
          this.toastr.error('Error changing post data!');
          window.location.reload();
        }
      );
    }
  
  }
}
