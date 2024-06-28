import { Component, OnInit } from '@angular/core';
import { Group } from '../model/group';
import { Router } from '@angular/router';
import { GroupService } from '../services/group.service';
import { ToastrService } from 'ngx-toastr';
import { GroupRequestService } from '../services/group-request.service';
import { GroupRequest } from '../model/groupRequest';
import { ImageService } from '../services/image.service';
import { User } from '../model/user.model';
import { UserServiceService } from '../services/user.service';
import { GroupDocument } from '../model/groupDocument';
import { PostService } from '../services/post.service';
import { Post } from '../model/post.model';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-groups',
  templateUrl: './groups.component.html',
  styleUrls: ['./groups.component.css']
})
export class GroupsComponent implements OnInit{

  groups!: Group[];
  searchGroup!: GroupDocument[];
  search: boolean = false;
  postCount: number = 0;


  constructor(private postService:PostService, private imageService:ImageService, private router: Router, private groupService:GroupService,private toastr: ToastrService,private groupRequestService:GroupRequestService ){
  }

  ngOnInit(): void {

    console.log("onInit")
  
    this.groupService.getMyGroups().subscribe(
      (data: any) => {
        console.log(data);  
        this.groups = data;   
        this.groups = data.map((group: any) => ({
          ...group,
          creationDate: this.parseDateArrayToDate(group.creationDate)
        }));

      },
      (error: any) => {
        console.error('Error get groups:', error);
        this.toastr.error('Error get groups!');
      }
    );


  }

  addGroup(){
    this.router.navigate(['group/add']);
  }

  addGroupWithPdf(){
    this.router.navigate(['group/addWithPdf']);
  }

  async getGroupPostCount(id: number): Promise<number> {
    try {
        const posts: Post[] | undefined = await this.postService.getGroupPosts(id).toPromise();
        if(posts!=undefined){
          return posts.length;
        }else{
          return 0;
        }
    } catch (error) {
        console.error('Error getting group posts:', error);
        return 0;
    }
  }

  
  findGroupByName(){

    this.search = true;

    const unos = document.querySelector('#unos') as HTMLInputElement;

    this.groupService.findGroupByName(unos.value).subscribe(
      async (data: any) => {
          console.log(data);
          this.searchGroup = await Promise.all(data.map(async (group: any) => {
              const postCount = await this.getGroupPostCount(group.id);
              return {
                  ...group,
                  postCount: postCount
              };
          }));
      },
      (error) => {
          console.error('Error getting groups:', error);
          this.toastr.error('Error getting groups!');
      });
  }

  findGroupByDescription(){

    this.search = true;

    const unos = document.querySelector('#unos') as HTMLInputElement;

    this.groupService.findGroupByDescription(unos.value).subscribe(
      async (data: any) => {
          console.log(data);
          this.searchGroup = await Promise.all(data.map(async (group: any) => {
              const postCount = await this.getGroupPostCount(group.id);
              return {
                  ...group,
                  postCount: postCount
              };
          }));
      },
      (error) => {
          console.error('Error getting groups:', error);
          this.toastr.error('Error getting groups!');
      });
  }

  findGroupByPDFDescription(){

    this.search = true;

    const unos = document.querySelector('#unos') as HTMLInputElement;

    this.groupService.findGroupByPDFDescription(unos.value).subscribe(
      async (data: any) => {
          console.log(data);
          this.searchGroup = await Promise.all(data.map(async (group: any) => {
              const postCount = await this.getGroupPostCount(group.id);
              return {
                  ...group,
                  postCount: postCount
              };
          }));
      },
      (error) => {
          console.error('Error getting groups:', error);
          this.toastr.error('Error getting groups!');
      });
  }

  addRequest(id:number){

    this.groupRequestService.createRequest(id).subscribe(
      (data: any) => {
        console.log(data);  
        this.toastr.success('Request successfully created!');
      },
      (error) => {
        console.error('Error creating group request:', error);
        this.toastr.error('Error creating group request!');
    });
  }


  parseDateArrayToDate(dateArray: number[]): Date {
    // Destructure the array elements to get individual parts
    const [year, month, day, hour, minute] = dateArray;
    // Note: Months in JavaScript Date are 0-based (0 - January, 1 - February, etc.)
    return new Date(year, month-1, day, hour, minute);
  }

  getImageUrl(imageName: string): string {
    return `http://localhost:4200/api/images/getImage/${imageName}`;
  }

}
