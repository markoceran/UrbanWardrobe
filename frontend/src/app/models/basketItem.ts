import { Basket } from "./basket";
import { Order } from "./order";
import { Product } from "./product";
import { Size } from "./size";

export interface BasketItem {
    id: number;
    product: Product;
    size: Size;
    basket: Basket;
    order: Order; 
    quantity: number;
}