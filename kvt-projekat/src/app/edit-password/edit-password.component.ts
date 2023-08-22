import { Component } from '@angular/core';
import { UserServiceService } from '../services/user.service';
import { ToastrService } from 'ngx-toastr';
import { FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-edit-password',
  templateUrl: './edit-password.component.html',
  styleUrls: ['./edit-password.component.css']
})
export class EditPasswordComponent {

  currentPassword = '';
  newPassword = '';
  newPassword2 = '';

  constructor(
    private toastr: ToastrService,
    private userService: UserServiceService

  ) {}
    

  submit(){
    
    if (this.currentPassword !== '' && this.newPassword !== '' && this.newPassword2 !== '' && this.newPassword === this.newPassword2){

      this.userService.editPassword(this.currentPassword, this.newPassword).subscribe(
        response => {
          console.log(response);
          this.toastr.success("You successfully changed password!");
        }, error => {
          console.error('Change password error:', error);
          this.toastr.error("Change password error!");
        });
    }else{
        this.toastr.error("Passwords don't match!");
    }
  }
  
}
