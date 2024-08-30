import { BasketItem } from "./basketItem";
import { OrderStatus } from "./orderStatus";

export interface Order {
    id: number;
    creationTime: string;
    estimatedDeliveryTime: string;
    basketItems: BasketItem[];
    productsAmount: number;
    shippingAmount: number;
    totalAmount: number;
    status: OrderStatus;
}