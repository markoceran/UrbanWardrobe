import { ProductCategory } from "./productCategory";
import { SizeQuantity } from "./sizeQuantity";

export interface ProductWithImages {
    id: number;
    code: string;
    name: string;
    description: string;
    sizeQuantities: Set<SizeQuantity>;
    price: number;
    category: ProductCategory;
    imagesName: string[];
    images: string[];
}