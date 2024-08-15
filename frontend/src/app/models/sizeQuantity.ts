import { Product } from "./product";
import { Size } from "./size";

export interface SizeQuantity {
    id: number;
    size: Size;
    quantity: number;
    product?: Product;  // Optional to prevent circular dependency issues in TypeScript
}