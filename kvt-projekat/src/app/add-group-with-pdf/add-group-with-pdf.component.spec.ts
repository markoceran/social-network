import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddGroupWithPDFComponent } from './add-group-with-pdf.component';

describe('AddGroupWithPDFComponent', () => {
  let component: AddGroupWithPDFComponent;
  let fixture: ComponentFixture<AddGroupWithPDFComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AddGroupWithPDFComponent]
    });
    fixture = TestBed.createComponent(AddGroupWithPDFComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
