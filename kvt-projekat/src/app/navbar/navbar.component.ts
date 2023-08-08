import { Component, OnInit } from '@angular/core';
import { User } from '../model/user.model';
import { UserServiceService } from '../services/user.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit{
 
  username: any;
  findUserForm: FormGroup;
  
  constructor(
    private userService: UserServiceService,
    private fb: FormBuilder,
    private router: Router
  ) {
    this.findUserForm = this.fb.group({
      input: [null, Validators.required]
    });
  } 
  

  ngOnInit() {
    
    this.username = this.userService.getUsernameFromToken();

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
