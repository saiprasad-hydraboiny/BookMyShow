package com.scaler.bookmyshow.repositories;


import com.scaler.bookmyshow.Models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//we're using JPA to interact with the DB
//JPA aks for object the repository will be managing,also key
//Don't have a class have a interface
//extends JPA repository


@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {

    @Override
    Optional<Booking> findById(Long aLong);

    @Override
    Booking save(Booking booking);
}
