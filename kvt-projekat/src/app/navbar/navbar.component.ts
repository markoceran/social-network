import { Component, OnInit } from '@angular/core';
import { User } from '../model/user.model';
import { UserServiceService } from '../services/user.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit{
 
  username: any;
  findUserForm: FormGroup;
  loggedUser!: User;
  
  constructor(
    private userService: UserServiceService,
    private fb: FormBuilder,
    private router: Router,
    private toast: ToastrService
  ) {
    this.findUserForm = this.fb.group({
      input: [null, Validators.required]
    });
  } 
  

  ngOnInit() {
    
    this.username = this.userService.getUsernameFromToken();
    
    this.userService.getProfileData(this.username).subscribe(
      (user: User) => {
        this.loggedUser = user;
        
      },
      (error) => {
        console.error('Error get user data:', error);
        this.toast.error('Error get user data!');
      }
    );

  }

  onSubmit(){
    
    if (this.findUserForm.valid) {
      const input = this.findUserForm.get('input');

      if (input && input.value) {
        this.router.navigate(['users/search/', input.value]);
        
      }

    }

  }


}
