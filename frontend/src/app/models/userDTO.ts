import { Basket } from "./basket";
import { ShippingAddress } from "./shippingAddress";
import { Wishlist } from "./wishlist";

export class UserDTO {
    email!: string;
    password!: string;
    firstName!: string;
    lastName!: string;
    phoneNumber!: string;
    shippingAddress!: ShippingAddress;
    basket!: Basket;
    wishlist!: Wishlist
}