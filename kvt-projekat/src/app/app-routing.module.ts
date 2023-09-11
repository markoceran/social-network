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
import { MainPageInGroupComponent } from './main-page-in-group/main-page-in-group.component';
import { AddPostInGroupComponent } from './add-post-in-group/add-post-in-group.component';
import { GroupAdminComponent } from './group-admin/group-admin.component';
import { SuspendGroupComponent } from './suspend-group/suspend-group.component';
import { EditGroupComponent } from './edit-group/edit-group.component';
import { RoleGuardService } from './guards/role-guard.service';
import { LoginGuardService } from './guards/login-guard.service';

const routes: Routes = [

  	{
		
		path: '',
		component: LoginComponent,
		canActivate: [LoginGuardService]
	},
	{
		
		path: 'main',
		component: MainPageComponent,
	  	canActivate: [RoleGuardService],
		data: {expectedRoles: 'ADMIN|USER|GROUP_ADMIN'}
	},
	{
		path: 'profile/:username',
		component: ProfileComponent,
	  	canActivate: [RoleGuardService],
		data: {expectedRoles: 'ADMIN|USER|GROUP_ADMIN'}
	},
	{
		path: 'posts/add',
		component: AddPostComponent,
	  	canActivate: [RoleGuardService],
		data: {expectedRoles: 'ADMIN|USER|GROUP_ADMIN'}
	},
	{
		path: 'users/search/:input',
		component: SearchComponent,
	    canActivate: [RoleGuardService],
		data: {expectedRoles: 'ADMIN|USER|GROUP_ADMIN'}
	},
	{
		path: 'friendRequests',
		component: FriendRequestsComponent,
	    canActivate: [RoleGuardService],
		data: {expectedRoles: 'ADMIN|USER|GROUP_ADMIN'}
	},
	{
		path: 'user/logout',
		component: LogoutComponent,
	    canActivate: [RoleGuardService],
		data: {expectedRoles: 'ADMIN|USER|GROUP_ADMIN'}
	},
	{
		path: 'group',
		component: GroupsComponent,
	    canActivate: [RoleGuardService],
		data: {expectedRoles: 'ADMIN|USER|GROUP_ADMIN'}
	},
	{
		path: 'group/add',
		component: AddGroupComponent,
	    canActivate: [RoleGuardService],
		data: {expectedRoles: 'ADMIN|USER|GROUP_ADMIN'}
	},
	{
		path: 'register',
		component: RegistrationComponent
	},
	{
		path: 'addProfileImage',
		component: AddProfileImageComponent,
	    canActivate: [RoleGuardService],
		data: {expectedRoles: 'ADMIN|USER|GROUP_ADMIN'}
	},
	{
		path: 'comments/:postId',
		component: CommentComponent,
	    canActivate: [RoleGuardService],
		data: {expectedRoles: 'ADMIN|USER|GROUP_ADMIN'}
	},
	{
		path: 'editProfile',
		component: EditProfileComponent,
	    canActivate: [RoleGuardService],
		data: {expectedRoles: 'ADMIN|USER|GROUP_ADMIN'}
	},
	{
		path: 'report',
		component: ReportComponent,
	    canActivate: [RoleGuardService],
		data: {expectedRoles: 'ADMIN'}
	},
	{
		path: 'banned',
		component: BannedComponent,
	    canActivate: [RoleGuardService],
		data: {expectedRoles: 'ADMIN'}
	},
	{
		
		path: 'addReportForPost/:id',
		component: AddReportPostComponent,
	    canActivate: [RoleGuardService],
		data: {expectedRoles: 'ADMIN|USER|GROUP_ADMIN'}
	},
	{
		
		path: 'addReportForUser/:id',
		component: AddReportUserComponent,
	    canActivate: [RoleGuardService],
		data: {expectedRoles: 'ADMIN|USER|GROUP_ADMIN'}
	},
	{
		
		path: 'addReportForComment/:id',
		component: AddReportCommentComponent,
	    canActivate: [RoleGuardService],
		data: {expectedRoles: 'ADMIN|USER|GROUP_ADMIN'}
	},
	{
		
		path: 'editPost/:id',
		component: EditPostComponent,
	    canActivate: [RoleGuardService],
		data: {expectedRoles: 'ADMIN|USER|GROUP_ADMIN'}
	},
	{
		
		path: 'reply/:id',
		component: AddReplyOnCommentComponent,
	    canActivate: [RoleGuardService],
		data: {expectedRoles: 'ADMIN|USER|GROUP_ADMIN'}
	},
	{
		
		path: 'mainPageGroup/:id',
		component: MainPageInGroupComponent,
	    canActivate: [RoleGuardService],
		data: {expectedRoles: 'ADMIN|USER|GROUP_ADMIN'}
	},
	{
		
		path: 'addPostInGroup/:id',
		component: AddPostInGroupComponent,
	    canActivate: [RoleGuardService],
		data: {expectedRoles: 'ADMIN|USER|GROUP_ADMIN'}
	},
	{
		
		path: 'groupAdmin',
		component: GroupAdminComponent,
	    canActivate: [RoleGuardService],
		data: {expectedRoles: 'ADMIN'}
	},
	{
		
		path: 'suspendGroup/:id',
		component: SuspendGroupComponent,
	    canActivate: [RoleGuardService],
		data: {expectedRoles: 'ADMIN'}
	},
	{
		
		path: 'editGroup/:id',
		component: EditGroupComponent,
	    canActivate: [RoleGuardService],
		data: {expectedRoles: 'GROUP_ADMIN'}
	}
];




@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
