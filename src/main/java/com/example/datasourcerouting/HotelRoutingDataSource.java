package com.example.datasourcerouting;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;


public class HotelRoutingDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return HotelContextHolder.getDatabase();
    }

}
