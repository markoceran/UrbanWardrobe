import { ShippingAddress } from "./shippingAddress";

export class NewUser {
    email!: string;
    password!: string;
    firstName!: string;
    lastName!: string;
    phoneNumber!: string;
    shippingAddress!: ShippingAddress;
}