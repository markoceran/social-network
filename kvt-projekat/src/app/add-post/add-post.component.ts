import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PostService } from '../services/post.service';
import { Post } from '../model/post.model';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { toArray } from 'rxjs';
import { ImageService } from '../services/image.service';

@Component({
  selector: 'app-add-post',
  templateUrl: './add-post.component.html',
  styleUrls: ['./add-post.component.css']
})
export class AddPostComponent {

  postForm: FormGroup;
  postFormImage: FormGroup;

  constructor(
    private fb: FormBuilder,
    private postService: PostService,
    private router: Router,
    private toastr: ToastrService,
    private imageService: ImageService

  ) {
    this.postForm = this.fb.group({
      content: [null, Validators.required]
    });
    this.postFormImage = this.fb.group({
      image: [null, Validators.required]
    });
  }

  onSubmit() {
    if (this.postForm.valid) {
      const newPost: Post = this.postForm.value;
      this.postService.createPost(newPost).subscribe(
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
    const imageFile = this.postFormImage.get('image');
    
    if (imageFile && imageFile.value) {
      console.log(imageFile);
      console.log(imageFile.value);
        this.imageService.uploadImageForPost(imageFile.value).subscribe(
            (response) => {
                console.log('Image uploaded successfully:', response);
                this.toastr.success("Image uploaded successfully!");
                
            },
            (error) => {
                console.error('Error uploading image:', error);
            }
        );
    }
  }



}
