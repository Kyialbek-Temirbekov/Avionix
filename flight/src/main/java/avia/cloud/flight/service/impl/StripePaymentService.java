package avia.cloud.flight.service.impl;

import avia.cloud.flight.dto.ChargeRequest;
import avia.cloud.flight.entity.Flight;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentLink;
import com.stripe.model.Price;
import com.stripe.param.PaymentLinkCreateParams;
import com.stripe.param.PriceCreateParams;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

import static avia.cloud.flight.util.CurrencyUtils.convertToSmallestUnit;

@Service
@Transactional
@RequiredArgsConstructor
public class StripePaymentService {
    @Value("${stripe.secret_key}")
    private String secretKey;
    @Value("${application.stripe.afterPaymentUrl}")
    private String afterPaymentUrl;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    public String charge(ChargeRequest chargeRequest)
            throws StripeException {
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", convertToSmallestUnit(chargeRequest.getAmount(), Currency.getInstance(chargeRequest.getCurrency())));
        chargeParams.put("currency", chargeRequest.getCurrency());
        chargeParams.put("description", chargeRequest.getDescription());
        chargeParams.put("source", chargeRequest.getStripeToken());
        Charge charge = Charge.create(chargeParams);
        return charge.getStatus();
    }

    public PaymentLink createPaymentLink(Flight flight, boolean checkedBaggageIncluded) throws StripeException {
        String currency = flight.getCurrency().toString();
        double baggagePrice = checkedBaggageIncluded ? flight.getTariff().getBaggagePrice() : 0.0;
        long amount = convertToSmallestUnit(flight.getTariff().getPrice() + baggagePrice, Currency.getInstance(currency));
        Price price = createPrice(currency.toLowerCase(), amount);

        PaymentLinkCreateParams paymentLinkCreateParams = PaymentLinkCreateParams.builder()
                .addLineItem(PaymentLinkCreateParams.LineItem.builder()
                                .setPrice(price.getId())
                                .setQuantity(1L)
                                .build())
                .setAfterCompletion(PaymentLinkCreateParams.AfterCompletion.builder()
                                .setType(PaymentLinkCreateParams.AfterCompletion.Type.REDIRECT)
                                .setRedirect(PaymentLinkCreateParams.AfterCompletion.Redirect.builder()
                                                .setUrl(afterPaymentUrl)
                                                .build())
                                .build())
                .build();

        return PaymentLink.create(paymentLinkCreateParams);
    }

    private Price createPrice(String currency, long amount) throws StripeException {
        PriceCreateParams params = PriceCreateParams.builder()
                        .setCurrency(currency)
                        .setUnitAmount(amount)
                        .setProductData(PriceCreateParams.ProductData.builder().setName("AVIONIX AVIA TICKET").build())
                        .build();

        return Price.create(params);
    }
}
