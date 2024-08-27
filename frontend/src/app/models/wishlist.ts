import { ProductWithImages } from "./productWithImages";

export interface Wishlist {
    id: number;
    products: ProductWithImages[];
}