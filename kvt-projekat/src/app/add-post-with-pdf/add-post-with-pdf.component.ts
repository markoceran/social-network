import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PostService } from '../services/post.service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { ImageService } from '../services/image.service';
import { PostDocument } from '../model/postDocument';

@Component({
  selector: 'app-add-post-with-pdf',
  templateUrl: './add-post-with-pdf.component.html',
  styleUrls: ['./add-post-with-pdf.component.css']
})
export class AddPostWithPdfComponent {

  postForm: FormGroup;
  postFormImage: FormGroup;
  pdfFile: File | null = null;
 

  constructor(
    private fb: FormBuilder,
    private postService: PostService,
    private router: Router,
    private toastr: ToastrService,
    private imageService: ImageService

  ) {
    this.postForm = this.fb.group({
      content: [null, Validators.required],
      title: [null, Validators.required]

    });
    this.postFormImage = this.fb.group({
      image: [null, Validators.required]
    });
  }

  onSubmit() {
    if (this.postForm.valid) {
      const newPost: PostDocument = this.postForm.value;
      const formData = new FormData();
      formData.append('post', new Blob([JSON.stringify(newPost)], { type: 'application/json' }));
      if (this.pdfFile) {
        formData.append('pdfFile', this.pdfFile, this.pdfFile.name);
      }

      this.postService.createPostWithPDF(formData).subscribe(
        (createdPost: PostDocument) => {
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

  onFileSelected(event: Event): void {
    const inputElement = event.target as HTMLInputElement;
    if (inputElement.files?.length) {
      this.pdfFile = inputElement.files[0];
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
