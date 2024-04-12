package com.transactionsapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Service
public class CurrencyService {

    @Value("${application.currency.api}")
    private String apiUrl;
    @Value("${application.currency.api.key}")
    private String apiKey;

    public Map<String, Double> getExchangeRates() throws Exception {
        Map<String, Double> exchangeRates = new HashMap<>();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(apiUrl + apiKey);
        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = ((CloseableHttpResponse) response).getEntity();

        if (entity != null) {
            InputStream inputStream = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(result.toString());
            JsonNode ratesNode = rootNode.path("rates");
            ratesNode.fields().forEachRemaining(entry -> {
                String currency = entry.getKey();
                double rate = entry.getValue().asDouble();
                exchangeRates.put(currency, rate);
            });
        }
        return exchangeRates;
    }

    public Double convertCurrency(double amount, String targetCurrency) throws Exception {
        Map<String, Double> exchangeRates = getExchangeRates();
        double ilsRate = exchangeRates.get("ILS");
        double targetCurrencyRate = exchangeRates.get(targetCurrency);
        return amount / ilsRate * targetCurrencyRate;
    }
}
