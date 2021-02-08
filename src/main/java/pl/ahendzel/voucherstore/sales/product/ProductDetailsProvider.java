package pl.ahendzel.voucherstore.sales.product;

public interface ProductDetailsProvider {
    ProductDetails getByProductId(String productId);
}
