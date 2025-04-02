import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportCollectionLockedDialogComponent } from './report-collection-locked-dialog.component';

describe('ReportCollectionLockedDialogComponent', () => {
  let component: ReportCollectionLockedDialogComponent;
  let fixture: ComponentFixture<ReportCollectionLockedDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ReportCollectionLockedDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReportCollectionLockedDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
