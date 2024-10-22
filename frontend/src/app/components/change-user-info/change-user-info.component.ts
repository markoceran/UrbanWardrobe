import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { defaultIfEmpty } from 'rxjs';
import { UserDTO } from 'src/app/models/userDTO';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-change-user-info',
  templateUrl: './change-user-info.component.html',
  styleUrls: ['./change-user-info.component.css']
})
export class ChangeUserInfoComponent implements OnInit {

  editForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<ChangeUserInfoComponent>,
    private authService: AuthService,
    private _snackBar: MatSnackBar,
    @Inject(MAT_DIALOG_DATA) private data: UserDTO | undefined
  ){}

  ngOnInit(): void {
    this.editForm = this.fb.group({
      firstName: [this.data?.firstName, Validators.required],
      lastName: [this.data?.lastName, Validators.required],
      phoneNumber: [this.data?.phoneNumber, Validators.required],
      password: ["", Validators.minLength(6)],
    });
  }

  onSubmit(): void {
    console.log('Form submitted');
    if (this.editForm.valid) {
      let formValues:UserDTO; 
      formValues = this.editForm.value;
      if(formValues.password === ""){
        formValues.password = "dontChange"
      }
      this.authService.updateUser(formValues).subscribe(
        (message) => {
          this.dialogRef.close(this.editForm.value);
          this.openSnackBar(message.message, "");
          console.log(message.message);
          setTimeout(() => {
            window.location.reload();
          }, 700);
          
        },
        (error) => {
          this.dialogRef.close(this.editForm.value);
          this.openSnackBar(error.error?.message, "");
          console.error(error.error?.message);
          setTimeout(() => {
            window.location.reload();
          }, 2000);
          
        }
      );
    }
  }

  openSnackBar(message: string, action: string) {
    this._snackBar.open(message, action,  {
      duration: 3500
    });
  }


}
