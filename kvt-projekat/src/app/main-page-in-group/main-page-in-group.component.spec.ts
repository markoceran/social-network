import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MainPageInGroupComponent } from './main-page-in-group.component';

describe('MainPageInGroupComponent', () => {
  let component: MainPageInGroupComponent;
  let fixture: ComponentFixture<MainPageInGroupComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MainPageInGroupComponent]
    });
    fixture = TestBed.createComponent(MainPageInGroupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
