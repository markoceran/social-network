import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { ImageService } from '../services/image.service';
import { UserServiceService } from '../services/user.service';
import { User } from '../model/user.model';

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.css']
})
export class EditProfileComponent implements OnInit{

  editProfileForm: FormGroup;
  username:string;
  loggedUser!:User;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private toastr: ToastrService,
    private imageService: ImageService,
    private userService: UserServiceService

  ) {
    this.editProfileForm = this.fb.group({
      firstName: [null, Validators.required],
      lastName: [null, Validators.required],
      username: [null, Validators.required],
      email: [null, Validators.required],
      displayName: [null],
      description: [null]
    });

    this.username = userService.getUsernameFromToken();

  }
  ngOnInit(): void {

    this.userService.getProfileData(this.username).subscribe(
      (user: User) => {
        this.loggedUser = user;
        
      },
      (error) => {
        console.error('Error get user data:', error);
        this.toastr.error('Error get user data!');
      }
    );
  }

  submit() {

    if (this.editProfileForm.valid) {
      const editedUser: User = this.editProfileForm.value;
      this.userService.editProfileData(editedUser).subscribe(
        (user: User) => {
          console.log('User profile data successfully changed:', user);
          this.toastr.success('User profile data successfully changed!');
          this.router.navigate(['profile/', this.username]);
          
        },
        (error) => {
          console.error('Error changing profile data:', error);
          this.toastr.error('Error changing profile data!');
          window.location.reload();
        }
      );
    }
  
  }
}
