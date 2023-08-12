import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { UserServiceService } from '../services/user.service';

@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html',
  styleUrls: ['./logout.component.css']
})
export class LogoutComponent implements OnInit{

  constructor(private router: Router, private userService: UserServiceService,private toastr: ToastrService ){
  }

  ngOnInit() {
    console.log("OnInit");

    this.userService.logout().subscribe(
    () => {
      localStorage.removeItem('user');
      this.toastr.success('Logout success!');
      this.router.navigate(['']);
    },
    (error) => {
      console.error('Error logout request:', error);
    }
    
  );


}
}
