import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PostService } from '../services/post.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { ImageService } from '../services/image.service';
import { Post } from '../model/post.model';

@Component({
  selector: 'app-add-post-in-group',
  templateUrl: './add-post-in-group.component.html',
  styleUrls: ['./add-post-in-group.component.css']
})
export class AddPostInGroupComponent {
  
  postForm: FormGroup;
  postFormImage: FormGroup;
  groupId!:number;

  constructor(
    private fb: FormBuilder,
    private postService: PostService,
    private router: Router,
    private toastr: ToastrService,
    private imageService: ImageService,
    private route: ActivatedRoute

  ) {
    this.postForm = this.fb.group({
      content: [null, Validators.required]
    });
    this.postFormImage = this.fb.group({
      image: [null, Validators.required]
    });

    this.route.params.subscribe(params => {
      this.groupId = params['id']; 
    });
  }

  onSubmit() {

    if (this.postForm.valid) {
      const newPost: Post = this.postForm.value;
      this.postService.createPostInGroup(this.groupId,newPost).subscribe(
        (createdPost: Post) => {
          console.log('Post created successfully:', createdPost);
          this.toastr.success('Post created successfully!');
          
        },
        (error) => {
          console.error('Error creating post:', error);
          this.toastr.error('Error creating post!');
        }
      );
    }
  }



  onSubmitImage() {

    const imageInput = document.querySelector('#imageInput') as HTMLInputElement;

    if (imageInput.files && imageInput.files.length > 0) {

      const formData = new FormData();
      formData.append('imageFile', imageInput.files[0]);

      this.imageService.uploadImageForPost(formData)
        .subscribe(response => {
          console.log(response);
          this.toastr.success("Successfully added image!");
        }, error => {
          console.error('Error:', error);
        });
    } 
  
  }
}
