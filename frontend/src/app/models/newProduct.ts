import { ProductCategory } from "./productCategory";
import { SizeQuantity } from "./sizeQuantity";

export interface NewProduct {
    name: string;
    description: string;
    sizeQuantities: Set<SizeQuantity>;
    price: number;
    category: ProductCategory;
}