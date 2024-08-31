import { BasketItem } from "./basketItem";
import { OrderStatus } from "./orderStatus";

export interface Order {
    id: number;
    creationTime: Date;
    estimatedDeliveryTime: Date;
    basketItems: BasketItem[];
    productsAmount: number;
    shippingAmount: number;
    totalAmount: number;
    status: OrderStatus;
}