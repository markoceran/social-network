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
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
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
import { EditProfileComponent } from './edit-profile/edit-profile.component';
import { EditPasswordComponent } from './edit-password/edit-password.component';
import { ReportComponent } from './report/report.component';
import { BannedComponent } from './banned/banned.component';
import { AddReportPostComponent } from './add-report-post/add-report-post.component';
import { AddReportCommentComponent } from './add-report-comment/add-report-comment.component';
import { AddReportUserComponent } from './add-report-user/add-report-user.component';
import { EditPostComponent } from './edit-post/edit-post.component';
import { AddReplyOnCommentComponent } from './add-reply-on-comment/add-reply-on-comment.component';
import { MainPageInGroupComponent } from './main-page-in-group/main-page-in-group.component';
import { AddPostInGroupComponent } from './add-post-in-group/add-post-in-group.component';
import { GroupAdminComponent } from './group-admin/group-admin.component';
import { SuspendGroupComponent } from './suspend-group/suspend-group.component';
import { EditGroupComponent } from './edit-group/edit-group.component';



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
    AddProfileImageComponent,
    EditProfileComponent,
    EditPasswordComponent,
    ReportComponent,
    BannedComponent,
    AddReportPostComponent,
    AddReportCommentComponent,
    AddReportUserComponent,
    EditPostComponent,
    AddReplyOnCommentComponent,
    MainPageInGroupComponent,
    AddPostInGroupComponent,
    GroupAdminComponent,
    SuspendGroupComponent,
    EditGroupComponent,
    
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ToastrModule.forRoot(),
    ReactiveFormsModule,
    BrowserAnimationsModule,
    FormsModule
  ],
  providers: [
    AuthenticationService,
    ToastrService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
