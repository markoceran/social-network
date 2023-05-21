import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MainPageComponent } from './main-page/main-page.component';
import { LoginComponent } from './login/login.component';

const routes: Routes = [

  	{
		path: 'main',
		component: MainPageComponent,
	  //canActivate: [RoleGuard],
		//data: {expectedRoles: 'ADMIN|WINE_USER'}
	}
	
];





@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
