import { Basket } from "./basket";
import { Order } from "./order";
import { Product } from "./product";
import { ProductWithImages } from "./productWithImages";
import { Size } from "./size";

export interface BasketItem {
    id: number;
    product: ProductWithImages;
    size: Size;
    basket: Basket;
    order: Order; 
    quantity: number;
    sizeOnStock: boolean;
    haveEnoughOnStock: boolean;
}