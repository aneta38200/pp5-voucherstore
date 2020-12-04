package pl.ahendzel.voucherstore.productcatalog;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class ProductCatalogConfiguration {


    public ProductCatalogFacade productCatalogFacade() {
        return new ProductCatalogFacade(new HashMapProductStorage());

    }

    @Bean
    public ProductStorage productionProductStorage() {
        return new HashMapProductStorage();
        //      return new JDBCProductStorage("mysql://localhost:3306/voucher-shop");
    }

    @Bean
    public ProductCatalogFacade fixturesAwareProductCatalogFacade() {
        ProductCatalogFacade productCatalogFacade = new ProductCatalogFacade(new HashMapProductStorage());

        String p1 = productCatalogFacade.createProduct();
        productCatalogFacade.applyPrice(p1, BigDecimal.valueOf(20.20));
        productCatalogFacade.updateProductDetails(p1, "My product 1", "http://nice.image.pl");

        String p2 = productCatalogFacade.createProduct();
        productCatalogFacade.applyPrice(p2, BigDecimal.valueOf(80.20));
        productCatalogFacade.updateProductDetails(p2, "My product 1", "http://nice.image.pl");

        String p3 = productCatalogFacade.createProduct();
        productCatalogFacade.applyPrice(p3, BigDecimal.valueOf(320.20));
        productCatalogFacade.updateProductDetails(p3, "My product 1", "http://nice.image.pl");

        return productCatalogFacade;
    }
}
