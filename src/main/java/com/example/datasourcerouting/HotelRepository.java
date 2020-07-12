package com.example.datasourcerouting;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HotelRepository extends CrudRepository<Hotel, Integer> {
    List<Hotel> findByName(String name);
    Hotel findById(int id);
}
