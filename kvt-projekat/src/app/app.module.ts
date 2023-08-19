import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CommentComponent } from './comment/comment.component';
import { LoginComponent } from './login/login.component';
import { MainPageComponent } from './main-page/main-page.component';
import { HttpClientModule } from '@angular/common/http';
import { AuthenticationService } from './services/authentication.service';
import { ToastrModule, ToastrService } from 'ngx-toastr';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NavbarComponent } from './navbar/navbar.component';
import { FriendRequestsComponent } from './friend-requests/friend-requests.component';
import { GroupsComponent } from './groups/groups.component';
import { ProfileComponent } from './profile/profile.component';
import { AddPostComponent } from './add-post/add-post.component';
import { SearchComponent } from './search/search.component';
import { LogoutComponent } from './logout/logout.component';
import { AddGroupComponent } from './add-group/add-group.component';
import { RegistrationComponent } from './registration/registration.component';
import { AddProfileImageComponent } from './add-profile-image/add-profile-image.component';


@NgModule({
  declarations: [
    AppComponent,
    CommentComponent,
    LoginComponent,
    MainPageComponent,
    NavbarComponent,
    FriendRequestsComponent,
    GroupsComponent,
    ProfileComponent,
    AddPostComponent,
    SearchComponent,
    LogoutComponent,
    AddGroupComponent,
    RegistrationComponent,
    AddProfileImageComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ToastrModule.forRoot(),
    ReactiveFormsModule,
    BrowserAnimationsModule
  ],
  providers: [
    AuthenticationService,
    ToastrService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
