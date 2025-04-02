import {Component, Inject} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {AccessRequestControllerService} from '../../../../services/openapi/services/access-request-controller.service';
import {AccessRequestDto} from '../../../../services/openapi/models/access-request-dto';
import {
  ReportCollectionControllerService
} from '../../../../services/openapi/services/report-collection-controller.service';
import {AuthService} from '../../services/auth/auth.service';

@Component({
  selector: 'app-report-collection-locked-dialog',
  standalone: false,
  templateUrl: './report-collection-locked-dialog.component.html',
  styleUrl: './report-collection-locked-dialog.component.scss'
})
export class ReportCollectionLockedDialogComponent {
  isRequestMode = false;
  tokenForm: FormGroup;
  accessRequestForm: FormGroup;

  constructor(
    public dialogRef: MatDialogRef<ReportCollectionLockedDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { collectionId: string },
    private formBuilder: FormBuilder,
    private reportCollectionService: ReportCollectionControllerService,
    private authService: AuthService
  ) {
    this.tokenForm = this.formBuilder.group({
      token: ['', Validators.required, Validators.pattern(/^\d{6}$/)],
    });

    this.accessRequestForm = this.formBuilder.group({
      name: ['', Validators.required],
      email: ['', Validators.required],
      message: ['', Validators.required],
    });
  }

  submitToken() {
    if (this.tokenForm.invalid) return;

    const token = this.tokenForm.value.token;
    this.reportCollectionService.getPublishedCollection({
      collectionId: this.data.collectionId,
      accessToken: token
    }).subscribe({
      next: () => {
        this.authService.storeToken(this.data.collectionId, token);
        this.dialogRef.close({ accessGranted: true });
      },
      error: (err) => {
        if (err.status === 403) {
          this.tokenForm.get('token')?.setErrors({ invalid: true });
        }
      }
    });
  }

  submitRequest() {
    if (this.accessRequestForm.invalid) return;

    const dto: AccessRequestDto = {
      ...this.accessRequestForm.value,
      reportCollectionId: this.data.collectionId,
    };

    this.reportCollectionService.createAccessRequest({
      collectionId: this.data.collectionId,
      body: dto,
    }).subscribe({
      next: () => this.dialogRef.close({ requestSubmitted: true }),
      error: () => this.accessRequestForm.setErrors({ submissionError: true })
    });
  }

  switchToRequestMode() {
    this.isRequestMode = true;
    this.tokenForm.reset();
  }

  switchToTokenMode() {
    this.isRequestMode = false;
    this.accessRequestForm.reset();
  }
}
