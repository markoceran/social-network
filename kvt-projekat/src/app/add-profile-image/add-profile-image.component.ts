import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { ImageService } from '../services/image.service';
import { UserServiceService } from '../services/user.service';

@Component({
  selector: 'app-add-profile-image',
  templateUrl: './add-profile-image.component.html',
  styleUrls: ['./add-profile-image.component.css']
})
export class AddProfileImageComponent {

  postFormImage: FormGroup;
  username!: string;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private toastr: ToastrService,
    private imageService: ImageService,
    private userService: UserServiceService

  ) {
    this.postFormImage = this.fb.group({
      image: [null, Validators.required]
    });

    this.username = userService.getUsernameFromToken();
  }



  onSubmitImage() {

    const imageInput = document.querySelector('#imageInput') as HTMLInputElement;

    if (imageInput.files && imageInput.files.length > 0) {

      const formData = new FormData();
      formData.append('imageFile', imageInput.files[0]);

      this.imageService.uploadProfileImage(formData)
        .subscribe(response => {
          console.log(response);
          this.toastr.success("Successfully added profile image!");
          this.router.navigate(['profile',this.username]);
        }, error => {
          console.error('Error:', error);
        });
    } 
  
  }
}
