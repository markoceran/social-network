import { TestBed } from '@angular/core/testing';

import { GroupRequestService } from './group-request.service';

describe('GroupRequestService', () => {
  let service: GroupRequestService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GroupRequestService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
