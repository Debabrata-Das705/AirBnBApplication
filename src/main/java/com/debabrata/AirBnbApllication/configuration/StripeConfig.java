package com.debabrata.AirBnbApllication.configuration;

import com.stripe.Stripe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {

   public StripeConfig(@Value("${stripe.secretkey}") String stripeSecretKey){
       Stripe.apiKey=stripeSecretKey;
   }


}
