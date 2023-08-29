import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BannedComponent } from './banned.component';

describe('BannedComponent', () => {
  let component: BannedComponent;
  let fixture: ComponentFixture<BannedComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BannedComponent]
    });
    fixture = TestBed.createComponent(BannedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
