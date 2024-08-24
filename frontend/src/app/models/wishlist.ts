import { Product } from "./product";
import { ProductWithImages } from "./productWithImages";
import { UserDTO } from "./userDTO";

export interface Wishlist {
    id: number;
    products: ProductWithImages[];
    user: UserDTO;
}