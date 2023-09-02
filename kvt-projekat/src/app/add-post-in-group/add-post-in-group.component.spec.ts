import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddPostInGroupComponent } from './add-post-in-group.component';

describe('AddPostInGroupComponent', () => {
  let component: AddPostInGroupComponent;
  let fixture: ComponentFixture<AddPostInGroupComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AddPostInGroupComponent]
    });
    fixture = TestBed.createComponent(AddPostInGroupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
