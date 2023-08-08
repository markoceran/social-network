import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserServiceService } from '../services/user.service';
import { User } from '../model/user.model';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit{

  input:any;
  searchUsers: Array<User> | undefined;

  constructor(private route: ActivatedRoute, private userService: UserServiceService){}


  ngOnInit() {

    this.route.params.subscribe(params => {
        this.input = params['input']; 
    });


    this.userService.findUsers(this.input).subscribe(
    (userData: any) => {
      this.searchUsers = userData; 
      console.log(this.searchUsers);
    },
    (error) => {
      console.error('Error fetching user data:', error);
    }
    
  );


}

}
