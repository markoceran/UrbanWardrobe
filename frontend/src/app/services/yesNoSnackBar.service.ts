import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatSnackBarRef, TextOnlySnackBar } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root',
})
export class YesNoSnackBarService {

  constructor(private snackBar: MatSnackBar) {}

  open(message: string): Promise<boolean> {
    return new Promise((resolve) => {
      const snackBarRef: MatSnackBarRef<TextOnlySnackBar> = this.snackBar.open(
        message,
        'Yes', // Primary action
        {
          duration: 5000, // Adjust the duration as needed
        }
      );

      snackBarRef.onAction().subscribe(() => {
        resolve(true); // User clicked 'Yes'
      });

      // Auto close after duration or user clicked 'No'
      snackBarRef.afterDismissed().subscribe((info) => {
        if (!info.dismissedByAction) {
          resolve(false); // User didn't click 'Yes'
        }
      });
    });
  }
}
