package pl.ahendzel.voucherstore.sales;

public interface Inventory {
    boolean isAvailable(String productId);
}
