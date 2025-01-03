import { Component, OnInit } from '@angular/core';
import { ImageService } from '../services/image.service';
import { ActivatedRoute, Router } from '@angular/router';
import { GroupService } from '../services/group.service';
import { ToastrService } from 'ngx-toastr';
import { GroupRequestService } from '../services/group-request.service';
import { PostService } from '../services/post.service';
import { Post } from '../model/post.model';
import { ReactionService } from '../services/reaction.service';
import { Reaction } from '../model/reaction';
import { UserServiceService } from '../services/user.service';
import { Group } from '../model/group';
import { GroupRequest } from '../model/groupRequest';
import { User } from '../model/user.model';
import { GroupAdmin } from '../model/groupAdmin';
import { BannedService } from '../services/banned.service';

@Component({
  selector: 'app-main-page-in-group',
  templateUrl: './main-page-in-group.component.html',
  styleUrls: ['./main-page-in-group.component.css']
})
export class MainPageInGroupComponent implements OnInit{

  groupId!:number;
  groupPosts!: Post[];
  group!: Group;
  showRequest: boolean = false;
  showUser: boolean = false;
  groupRequests!: GroupRequest[];
  loggedUser!: User;

  constructor(private bannedService:BannedService, private userService:UserServiceService, private reactionService:ReactionService, private postService:PostService,private imageService:ImageService, private route: ActivatedRoute, private router: Router, private groupService:GroupService,private toast: ToastrService,private groupRequestService:GroupRequestService ){
  }

  ngOnInit(): void {

    console.log("onInit")
  
    this.route.params.subscribe(params => {
      this.groupId = params['id']; 
    });

    this.groupService.getGroupById(this.groupId).subscribe(
      (group: Group) =>{
        this.group = group;
        console.log(group);
      }
    )

    this.userService.getProfileData(this.userService.getUsernameFromToken()).subscribe(
      (data: any) => {
        console.log(data);  
        this.loggedUser = data;   
      },
      (error: any) => {
        console.error('Error getting user:', error);
        this.toast.error('Error getting user!');
      }
    );


    this.postService.getGroupPosts(this.groupId).subscribe(
      (posts: any) => {
        this.groupPosts = posts.map((post: any) => ({
          ...post,
          creationDate: this.parseDateArrayToDate(post.creationDate)
        }));
        
        this.groupPosts.forEach(post => {
          this.reactionService.getPostReaction(post.id).subscribe(
            (reaction: Reaction[]) => {
              
              if(reaction.length>0){
                reaction.forEach(r => {

                if(r.madeBy.username === this.userService.getUsernameFromToken()){
                console.log(r);
                if(r.type === "LIKE"){
                  post.isLiked = true;
                }else if(r.type === "DISLIKE"){
                  post.isDisliked = true;
                }else{ post.isHearted = true;}

              }
              });  
            }
                        
            }
          )
          this.imageService.getPostImage(post.id).subscribe(
            (images: any) =>{
              post.images = images;
              console.log(images);
            }
          )
          this.imageService.getProfileImageByUser(post.postedBy.username).subscribe(
            (image: any) =>{
              post.postedBy.profileImage = image;
              console.log(image);
            }
          )
        });
      },
      (error) => {
        console.error('Error fetching user data:', error);
      }
    );

    this.groupRequestService.getRequests(this.groupId).subscribe(
      (requests: GroupRequest[]) =>{

        requests.forEach(request=>{
          this.imageService.getProfileImageByUser(request.from.username).subscribe(
            (image: any) =>{
              request.from.profileImage = image;
              console.log(image);
            }
          )
        })

        this.groupRequests = requests.map((request: any) => ({
          ...request,
          createdAt: this.parseDateArrayToDate(request.createdAt)
        }));
       
      }
    )

    this.groupService.getGroupUsers(this.groupId).subscribe(
      (users: any) => {
        console.log(users);  
        this.group.groupUsers = users;   
      },
      (error: any) => {
        console.error('Error getting group user:', error);
        this.toast.error('Error getting group user!');
      }
    );

    this.groupService.getGroupAdmins(this.groupId).subscribe(
      (admins: any) => {
        console.log(admins);  
        this.group.groupAdmins = admins;   
      },
      (error: any) => {
        console.error('Error getting group admin:', error);
        this.toast.error('Error getting group admin!');
      }
    );

  }
  

  

  heart(id: number, postIndex: number){

    this.reactionService.heartPost(id).subscribe(
      (data: any) => {
        
        console.log(data);
        this.toast.success('You add heart on post');
        this.groupPosts[postIndex].isHearted = true;
    
        this.groupPosts[postIndex].isLiked = false;
        this.groupPosts[postIndex].isDisliked = false;
      },
      (error) => {
        console.error('Error add heart on post', error);
      }
    );

  }

  like(id: number, postIndex: number){

    this.reactionService.likePost(id).subscribe(
      (data: any) => {
       
        console.log(data);
        this.toast.success('You add like on post');
        this.groupPosts[postIndex].isLiked = true;
       
        this.groupPosts[postIndex].isHearted = false;
        this.groupPosts[postIndex].isDisliked = false;
      },
      (error) => {
        console.error('Error add like on post', error);
      }
    );
  }

  dislike(id: number, postIndex: number){
    
    this.reactionService.dislikePost(id).subscribe(
      (data: any) => {
        
        console.log(data);
        this.toast.success('You add dislike on post');
        this.groupPosts[postIndex].isDisliked = true;
        
        this.groupPosts[postIndex].isHearted = false;
        this.groupPosts[postIndex].isLiked = false;
      },
      (error) => {
        console.error('Error add dislike on post', error);
      }
    );
  }

  comments(postId:number){
    this.router.navigate(['comments/', postId]);
  }

  isUserAdmin(userToCheck:User, adminList:GroupAdmin[]) {
    if(adminList !== undefined){
      return adminList.some(admin => admin.user.username === userToCheck.username);
    }
    return false;
  }

  groupAdmin(){

    const admin = this.group.groupAdmins.find(admin => admin.user.username === this.loggedUser.username);
    return admin ? admin.id : 0;

  }

  blockUser(groupId:number, userId:number, groupAdminId:number){

    this.bannedService.banUserInGroup(groupId, userId, groupAdminId).subscribe(
      (data: any) => {
        console.log(data);  
        this.toast.success('User successfully banned!')
      },
      (error: any) => {
        console.error('Error:', error);
        this.toast.error('Error!');
      }
    );
  }

  unblockUser(id:number, groupId:number){

    this.bannedService.unbanUserInGroup(id, groupId).subscribe(
      (data: any) => {
        console.log(data);  
        this.toast.success('User successfully unbanned!')
      },
      (error: any) => {
        console.error('Error:', error);
        this.toast.error('Error!');
      }
    );
  }

  deleteGroup(id:number){

    this.groupService.deleteGroup(id).subscribe(
      (data: any) => {
        console.log(data);  
        this.toast.success('Group successfully deleted!');
        this.router.navigate(['/group']);
      },
      (error: any) => {
        console.error('Error:', error);
        this.toast.error('Error!');
      }
    );
  }
  
  showRequests(){

    this.showRequest = !this.showRequest;

  }

  showUsers(){

    this.showUser = !this.showUser;

  }

  accept(id:number){
    this.groupRequestService.accept(id).subscribe(
      (data: any) => {
        console.log(data);  
        this.toast.success('Request was successfully accepted!');
        window.location.reload();
        
      },
      (error) => {
        this.toast.error('Error accept request!');
        console.error('Error accept request:', error);
        
    });
  }

  deny(id:number){
    this.groupRequestService.deny(id).subscribe(
      (data: any) => {
        console.log(data);  
        this.toast.success('Request was successfully denied!');
        window.location.reload();
        
      },
      (error) => {
        this.toast.error('Error deny request!');
        console.error('Error deny request:', error);
        
    });
  }
  
  getImageUrl(imageName: string): string {
    return `http://localhost:4200/api/images/getImage/${imageName}`;
  }


  parseDateArrayToDate(dateArray: number[]): Date {
    // Destructure the array elements to get individual parts
    const [year, month, day, hour, minute] = dateArray;
    // Note: Months in JavaScript Date are 0-based (0 - January, 1 - February, etc.)
    return new Date(year, month - 1, day, hour, minute);
  }

  shuffleArray(array: any[]) {
    for (let i = array.length - 1; i > 0; i--) {
      const j = Math.floor(Math.random() * (i + 1));
      [array[i], array[j]] = [array[j], array[i]]; // Swap elements
    }
  }
}
