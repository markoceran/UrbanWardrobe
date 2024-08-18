import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { NewProduct } from 'src/app/models/newProduct';
import { Product } from 'src/app/models/product';
import { ProductCategory } from 'src/app/models/productCategory';
import { Size } from 'src/app/models/size';
import { ImageService } from 'src/app/services/image.service';
import { ProductService } from 'src/app/services/product.service';

@Component({
  selector: 'app-create-product',
  templateUrl: './create-product.component.html',
  styleUrls: ['./create-product.component.css']
})
export class CreateProductComponent implements OnInit {

  productForm!: FormGroup;
  submitted = false;
  imageFiles: Set<File> = new Set<File>;
  responseProductCode: any;

  categories = Object.values(ProductCategory);
  sizes = Object.values(Size);

  constructor(private fb: FormBuilder,private _snackBar: MatSnackBar,private imageService:ImageService,private productService:ProductService,private router: Router) {}

  ngOnInit(): void {
    this.productForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      price: [0, [Validators.required, Validators.min(1)]],
      category: ['', Validators.required],
      sizeQuantities: this.fb.array([this.createSizeQuantity()]),
      images: [null]
    });
  }

  get sizeQuantities() {
    return this.productForm.get('sizeQuantities') as FormArray;
  }

  createSizeQuantity(): FormGroup {
    return this.fb.group({
      size: ['', Validators.required],
      quantity: [1, [Validators.required, Validators.min(1)]]
    });
  }

  addSizeQuantity(): void {
    this.sizeQuantities.push(this.createSizeQuantity());
  }

  removeSizeQuantity(index: number): void {
    this.sizeQuantities.removeAt(index);
  }

  get f(): { [key: string]: AbstractControl } {
    return this.productForm.controls;
  }


  onFileChange(event: any): void {
    // Handle file input change
    const files: FileList = event.target.files;
    for (let i = 0; i < files.length; i++) {
      this.imageFiles.add(files[i]);
    }
  }

  getAvailableSizes(index: number): string[] {
    // Get the list of currently selected sizes
    const selectedSizes = this.sizeQuantities.controls
      .map((control, i) => i !== index ? control.get('size')?.value : null)
      .filter(size => size != null);

    // Return sizes that are not selected
    return this.sizes.filter(size => !selectedSizes.includes(size));
  }
  

  uploadImages(): void {
    if (this.imageFiles.size === 0) {
      this.openSnackBar("No images selected for upload!", "");
      console.warn('No images selected for upload.');
      return;
    }

    // Use FormData to send files to the backend
    const formData = new FormData();
    for (const file of this.imageFiles) {
      formData.append('images', file);
    }

    this.imageService.uploadImages(this.responseProductCode, formData).subscribe(
      () => {
        console.log('Images uploaded successfully!');
      },
      (error) => {
        console.error('Error uploading images:', error);
        this.openSnackBar("Error uploading images!", "");
      }
    );
  }


  openSnackBar(message: string, action: string) {
    this._snackBar.open(message, action,  {
      duration: 3500
    });
  }

  onSubmit(): void {
    this.submitted = true;
    if (this.productForm.invalid) {
      return;
    }

    if (this.imageFiles.size === 0) {
      this.openSnackBar("No images selected for upload!", "");
      console.warn('No images selected for upload.');
      return;
    }

    const formValues = this.productForm.value;

    const newProduct: NewProduct = {
      name: formValues.name,
      description: formValues.description,
      sizeQuantities: formValues.sizeQuantities,
      price: formValues.price,
      category: formValues.category
    };

    this.productService.createProduct(newProduct).subscribe(
    (product:Product) => {
      this.responseProductCode = product.code;
      this.uploadImages();
      this.openSnackBar("Product created successfully!", "");
      console.log('Product created successfully!');
      this.router.navigate(['/Main-Page']);
    },
    (error) => {
      this.openSnackBar("Error creating product!", "");
      console.error('Error creating product:', error);
    }
    );

  }
}
