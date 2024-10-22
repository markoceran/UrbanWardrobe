export interface PaginationResponse {
    message: string;
    data: any;          // Can be adjusted to a specific type if needed
    totalElements: number;
    totalPages: number;
    currentPage: number;
}
