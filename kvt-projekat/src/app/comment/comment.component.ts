import { Component } from '@angular/core';

@Component({
  selector: 'app-comment',
  templateUrl: './comment.component.html',
  styleUrls: ['./comment.component.css']
})
export class CommentComponent {

  comments: any[] = [{"text":"test1","timestamp":"16.02.2020","isDeleted":"false"},{"text":"test2","timestamp":"20.09.2021","isDeleted":"false"},{"text":"test3","timestamp":"08.10.2023","isDeleted":"false"}];
    
    constructor(){
      
    }

}
