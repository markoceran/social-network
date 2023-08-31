import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MainPageComponent } from './main-page/main-page.component';
import { LoginComponent } from './login/login.component';
import { ProfileComponent } from './profile/profile.component';
import { AddPostComponent } from './add-post/add-post.component';
import { SearchComponent } from './search/search.component';
import { FriendRequestsComponent } from './friend-requests/friend-requests.component';
import { LogoutComponent } from './logout/logout.component';
import { GroupsComponent } from './groups/groups.component';
import { AddGroupComponent } from './add-group/add-group.component';
import { RegistrationComponent } from './registration/registration.component';
import { AddProfileImageComponent } from './add-profile-image/add-profile-image.component';
import { CommentComponent } from './comment/comment.component';
import { EditProfileComponent } from './edit-profile/edit-profile.component';
import { ReportComponent } from './report/report.component';
import { BannedComponent } from './banned/banned.component';
import { AddReportPostComponent } from './add-report-post/add-report-post.component';
import { AddReportUserComponent } from './add-report-user/add-report-user.component';
import { AddReportCommentComponent } from './add-report-comment/add-report-comment.component';
import { EditPostComponent } from './edit-post/edit-post.component';
import { AddReplyOnCommentComponent } from './add-reply-on-comment/add-reply-on-comment.component';

const routes: Routes = [

  	{
		
		path: '',
		component: LoginComponent,
	  //canActivate: [RoleGuard],
		//data: {expectedRoles: 'ADMIN|WINE_USER'}
	},
	{
		
		path: 'main',
		component: MainPageComponent,
	  //canActivate: [RoleGuard],
		//data: {expectedRoles: 'ADMIN|WINE_USER'}
	},
	{
		
		path: 'profile/:username',
		component: ProfileComponent,
	  //canActivate: [RoleGuard],
		//data: {expectedRoles: 'ADMIN|WINE_USER'}
	},
	{
		path: 'posts/add',
		component: AddPostComponent,
	  //canActivate: [RoleGuard],
		//data: {expectedRoles: 'ADMIN|WINE_USER'}
	},
	{
		path: 'users/search/:input',
		component: SearchComponent,
	  //canActivate: [RoleGuard],
		//data: {expectedRoles: 'ADMIN|WINE_USER'}
	},
	{
		path: 'friendRequests',
		component: FriendRequestsComponent,
	  //canActivate: [RoleGuard],
		//data: {expectedRoles: 'ADMIN|WINE_USER'}
	},
	{
		path: 'user/logout',
		component: LogoutComponent,
	  //canActivate: [RoleGuard],
		//data: {expectedRoles: 'ADMIN|WINE_USER'}
	},
	{
		path: 'group',
		component: GroupsComponent,
	  //canActivate: [RoleGuard],
		//data: {expectedRoles: 'ADMIN|WINE_USER'}
	},
	{
		path: 'group/add',
		component: AddGroupComponent,
	  //canActivate: [RoleGuard],
		//data: {expectedRoles: 'ADMIN|WINE_USER'}
	},
	{
		path: 'register',
		component: RegistrationComponent,
	  //canActivate: [RoleGuard],
		//data: {expectedRoles: 'ADMIN|WINE_USER'}
	},
	{
		path: 'addProfileImage',
		component: AddProfileImageComponent,
	  //canActivate: [RoleGuard],
		//data: {expectedRoles: 'ADMIN|WINE_USER'}
	},
	{
		path: 'comments/:postId',
		component: CommentComponent,
	  //canActivate: [RoleGuard],
		//data: {expectedRoles: 'ADMIN|WINE_USER'}
	},
	{
		path: 'editProfile',
		component: EditProfileComponent,
	  //canActivate: [RoleGuard],
		//data: {expectedRoles: 'ADMIN|WINE_USER'}
	},
	{
		path: 'report',
		component: ReportComponent,
	  //canActivate: [RoleGuard],
		//data: {expectedRoles: 'ADMIN|WINE_USER'}
	},
	{
		path: 'banned',
		component: BannedComponent,
	  //canActivate: [RoleGuard],
		//data: {expectedRoles: 'ADMIN|WINE_USER'}
	},
	{
		
		path: 'addReportForPost/:id',
		component: AddReportPostComponent,
	  //canActivate: [RoleGuard],
		//data: {expectedRoles: 'ADMIN|WINE_USER'}
	},
	{
		
		path: 'addReportForUser/:id',
		component: AddReportUserComponent,
	  //canActivate: [RoleGuard],
		//data: {expectedRoles: 'ADMIN|WINE_USER'}
	},
	{
		
		path: 'addReportForComment/:id',
		component: AddReportCommentComponent,
	  //canActivate: [RoleGuard],
		//data: {expectedRoles: 'ADMIN|WINE_USER'}
	},
	{
		
		path: 'editPost/:id',
		component: EditPostComponent,
	  //canActivate: [RoleGuard],
		//data: {expectedRoles: 'ADMIN|WINE_USER'}
	},
	{
		
		path: 'reply/:id',
		component: AddReplyOnCommentComponent,
	  //canActivate: [RoleGuard],
		//data: {expectedRoles: 'ADMIN|WINE_USER'}
	}
];





@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
