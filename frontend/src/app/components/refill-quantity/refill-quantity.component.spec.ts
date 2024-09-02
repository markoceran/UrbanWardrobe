import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RefillQuantityComponent } from './refill-quantity.component';

describe('RefillQuantityComponent', () => {
  let component: RefillQuantityComponent;
  let fixture: ComponentFixture<RefillQuantityComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RefillQuantityComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RefillQuantityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
