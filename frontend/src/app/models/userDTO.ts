import { ShippingAddress } from "./shippingAddress";

export class UserDTO {
    email!: string;
    password!: string;
    firstName!: string;
    lastName!: string;
    phoneNumber!: string;
    shippingAddress!: ShippingAddress;
}