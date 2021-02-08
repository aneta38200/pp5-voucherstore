package pl.ahendzel.voucherstore.sales;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.ahendzel.payu.http.JavaHttpPayUApiClient;
import pl.ahendzel.payu.PayU;
import pl.ahendzel.payu.PayUCredentials;
import pl.ahendzel.voucherstore.productcatalog.ProductCatalogFacade;
import pl.ahendzel.voucherstore.sales.basket.InMemoryBasketStorage;
import pl.ahendzel.voucherstore.sales.offer.OfferMaker;
import pl.ahendzel.voucherstore.sales.ordering.ReservationRepository;
import pl.ahendzel.voucherstore.sales.payment.PayUPaymentGateway;
import pl.ahendzel.voucherstore.sales.payment.PaymentGateway;
import pl.ahendzel.voucherstore.sales.product.ProductCatalogProductDetailsProvider;
import pl.ahendzel.voucherstore.sales.product.ProductDetailsProvider;

@Configuration
public class SalesConfiguration {

    @Bean
    SalesFacade salesFacade(ProductCatalogFacade productCatalogFacade, OfferMaker offerMaker, PaymentGateway paymentGateway, ReservationRepository reservationRepository) {
        return new SalesFacade(
                productCatalogFacade,
                new InMemoryBasketStorage(),
                () -> "customer_1",
                (productId) -> true,
                offerMaker,
                paymentGateway,
                reservationRepository);
    }

    @Bean
    PaymentGateway payUPaymentGateway() {
        return new PayUPaymentGateway(new PayU(
                PayUCredentials.productionOfEnv(),
                new JavaHttpPayUApiClient()
        ));
    }

    @Bean
    OfferMaker offerMaker(ProductDetailsProvider productDetailsProvider) {
        return new OfferMaker(productDetailsProvider);
    }

    @Bean
    ProductDetailsProvider productDetailsProvider(ProductCatalogFacade productCatalogFacade) {
        return new ProductCatalogProductDetailsProvider(productCatalogFacade);
    }
}
