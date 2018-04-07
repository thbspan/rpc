package com.github.thbspan.rpc.service;

public class SearchPriceImpl implements ISearchPrice {
    @Override
    public String getPrice(String name) {
        return String.format("price of [%s] is $100.00", name);
    }
}
