import {Component, Inject} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {AccessRequestDto} from '../../../../services/openapi/models/access-request-dto';
import {
  ReportCollectionControllerService
} from '../../../../services/openapi/services/report-collection-controller.service';
import {TokenAuthService} from '../../services/token-auth/token-auth.service';
import {MatSnackBar} from '@angular/material/snack-bar';
import {take} from 'rxjs';

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
    private tokenAuthService: TokenAuthService,
    private snackBar: MatSnackBar
  ) {
    this.tokenForm = this.formBuilder.group({
      token: ['', [
        Validators.required,
        Validators.pattern(/^\d{6}$/)
      ]],
    });

    this.accessRequestForm = this.formBuilder.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      message: ['', /* Validators.required */],
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
        this.tokenAuthService.storeToken(this.data.collectionId, token);
        this.tokenAuthService.watchTokenValidity(this.data.collectionId)
          .pipe(take(1))
          .subscribe(() => this.dialogRef.close({ accessGranted: true }));
        this.snackBar.open('Access granted!', 'Close', { duration: 3000 });
        this.dialogRef.close({ accessGranted: true });
      },
      error: () => {
        this.snackBar.open('Invalid access token', 'Close', { duration: 3000 });
        this.tokenForm.get('token')?.setErrors({ invalid: true });
      }
    });
  }

  submitAccessRequest() {
    if (this.accessRequestForm.invalid) {
      this.snackBar.open('Please fill required fields', 'Close', { duration: 3000 });
      return;
    }

    const dto: AccessRequestDto = {
      ...this.accessRequestForm.value,
      reportCollectionId: this.data.collectionId,
    };

    this.reportCollectionService.createAccessRequest({
      collectionId: this.data.collectionId,
      body: dto,
    }).subscribe({
      next: () => {
        this.snackBar.open('Access request submitted', 'Close', { duration: 3000 });
        this.dialogRef.close({requestSubmitted: true})
      },
      error: () => {
        this.snackBar.open('Failed to submit request', 'Close', { duration: 3000 });
        this.accessRequestForm.setErrors({submissionError: true})
      }
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
