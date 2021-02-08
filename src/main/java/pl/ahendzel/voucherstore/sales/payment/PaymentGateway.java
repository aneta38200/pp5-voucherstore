package pl.ahendzel.voucherstore.sales.payment;

import pl.ahendzel.voucherstore.sales.ordering.Reservation;

public interface PaymentGateway {
    PaymentDetails register(Reservation reservation);

    boolean isTrusted(PaymentUpdateStatusRequest paymentUpdateStatusRequest);
}
