import { ProductCategory } from "./productCategory";
import { SizeQuantity } from "./sizeQuantity";

export interface NewProduct {
    name: string;
    description: string;
    sizeQuantities: SizeQuantity[];
    price: number;
    category: ProductCategory;
}