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
	}
	
];





@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
