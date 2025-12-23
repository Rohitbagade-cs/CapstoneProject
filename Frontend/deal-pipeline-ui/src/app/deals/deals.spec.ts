import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DealsComponent } from './deals';

describe('Deals', () => {
  let component: DealsComponent;
  let fixture: ComponentFixture<DealsComponent>;
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DealsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DealsComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
