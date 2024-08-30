import { Product } from "./product";
import { Size } from "./size";

export interface BasketItem {
    id: number;
    product: Product;
    size: Size;
    quantity: number;
    sizeOnStock: boolean;
    haveEnoughOnStock: boolean;
}