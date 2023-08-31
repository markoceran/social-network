import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddReplyOnCommentComponent } from './add-reply-on-comment.component';

describe('AddReplyOnCommentComponent', () => {
  let component: AddReplyOnCommentComponent;
  let fixture: ComponentFixture<AddReplyOnCommentComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AddReplyOnCommentComponent]
    });
    fixture = TestBed.createComponent(AddReplyOnCommentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
