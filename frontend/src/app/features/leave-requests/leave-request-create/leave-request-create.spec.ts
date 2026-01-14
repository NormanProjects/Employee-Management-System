import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LeaveRequestCreate } from './leave-request-create';

describe('LeaveRequestCreate', () => {
  let component: LeaveRequestCreate;
  let fixture: ComponentFixture<LeaveRequestCreate>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LeaveRequestCreate]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LeaveRequestCreate);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
