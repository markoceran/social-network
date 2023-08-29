import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddReportPostComponent } from './add-report-post.component';

describe('AddReportPostComponent', () => {
  let component: AddReportPostComponent;
  let fixture: ComponentFixture<AddReportPostComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AddReportPostComponent]
    });
    fixture = TestBed.createComponent(AddReportPostComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
