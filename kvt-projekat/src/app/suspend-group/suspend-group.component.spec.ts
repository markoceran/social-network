import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SuspendGroupComponent } from './suspend-group.component';

describe('SuspendGroupComponent', () => {
  let component: SuspendGroupComponent;
  let fixture: ComponentFixture<SuspendGroupComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SuspendGroupComponent]
    });
    fixture = TestBed.createComponent(SuspendGroupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
