import { ProductCategory } from "./productCategory";
import { SizeQuantity } from "./sizeQuantity";

export interface Product {
    id: number;
    code: string;
    name: string;
    description: string;
    sizeQuantities: SizeQuantity[];
    price: number;
    category: ProductCategory;
    imagesName: string[];
    images: string[];
    inWishlist: boolean;
    outOfStock: boolean;
}