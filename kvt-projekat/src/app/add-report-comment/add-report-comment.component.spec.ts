import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddReportCommentComponent } from './add-report-comment.component';

describe('AddReportCommentComponent', () => {
  let component: AddReportCommentComponent;
  let fixture: ComponentFixture<AddReportCommentComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AddReportCommentComponent]
    });
    fixture = TestBed.createComponent(AddReportCommentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
