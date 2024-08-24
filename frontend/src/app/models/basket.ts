import { BasketItem } from "./basketItem";
import { UserDTO } from "./userDTO";

export interface Basket {
    id: number;
    basketItems: BasketItem[];
    user: UserDTO;
}
  