package pl.ahendzel.voucherstore.productcatalog;

import java.util.List;
import java.util.Optional;

public interface ProductStorage {
    void save(Product newProduct);

    Optional<Product> getById(String productId);

    List<Product> getAllPublished();
}
