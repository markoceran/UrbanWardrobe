import { BasketItem } from "./basketItem";
import { OrderStatus } from "./orderStatus";
import { UserDTO } from "./userDTO";

export interface Order {
    id: number;
    creationTime: string;
    estimatedDeliveryTime: string;
    basketItems: BasketItem[];
    user: UserDTO;
    productsAmount: number;
    shippingAmount: number;
    totalAmount: number;
    status: OrderStatus;
}