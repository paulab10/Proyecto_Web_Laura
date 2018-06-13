import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LoadFilesCitiesComponent } from './load-files-cities.component';

describe('LoadFilesCitiesComponent', () => {
  let component: LoadFilesCitiesComponent;
  let fixture: ComponentFixture<LoadFilesCitiesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LoadFilesCitiesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LoadFilesCitiesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
