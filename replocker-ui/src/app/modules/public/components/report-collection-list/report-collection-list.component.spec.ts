import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportCollectionListComponent } from './report-collection-list.component';

describe('ReportCollectionListComponent', () => {
  let component: ReportCollectionListComponent;
  let fixture: ComponentFixture<ReportCollectionListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ReportCollectionListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReportCollectionListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
