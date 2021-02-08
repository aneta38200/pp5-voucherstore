package pl.ahendzel.voucherstore.sales.payment;

import pl.ahendzel.payu.exceptions.PayUException;

public class PaymentException extends IllegalStateException {
    public PaymentException(PayUException e) {
        super(e);
    }
}
