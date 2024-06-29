import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddPostWithPdfComponent } from './add-post-with-pdf.component';

describe('AddPostWithPdfComponent', () => {
  let component: AddPostWithPdfComponent;
  let fixture: ComponentFixture<AddPostWithPdfComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AddPostWithPdfComponent]
    });
    fixture = TestBed.createComponent(AddPostWithPdfComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
