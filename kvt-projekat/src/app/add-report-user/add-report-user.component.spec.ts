import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddReportUserComponent } from './add-report-user.component';

describe('AddReportUserComponent', () => {
  let component: AddReportUserComponent;
  let fixture: ComponentFixture<AddReportUserComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AddReportUserComponent]
    });
    fixture = TestBed.createComponent(AddReportUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
