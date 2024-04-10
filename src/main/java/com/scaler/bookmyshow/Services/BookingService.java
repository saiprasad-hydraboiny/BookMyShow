package com.scaler.bookmyshow.Services;


import com.scaler.bookmyshow.Models.*;
import com.scaler.bookmyshow.repositories.BookingRepository;
import com.scaler.bookmyshow.repositories.ShowRepository;
import com.scaler.bookmyshow.repositories.ShowSeatRepository;
import com.scaler.bookmyshow.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

//Rather than allocating a seat, updating its status, locking the seats and waiting for payment to happen in a single transaction, in the soft lock approach we are using the transaction just to update the seats status column.
//Connect to a database and start transaction use @Transactional annotation

@Service
public class BookingService {

    private UserRepository userRepository;
    private ShowRepository showRepository;
    private ShowSeatRepository showSeatRepository;
    private PricingService pricingService;
    private BookingRepository bookingRepository;

    @Autowired
    public BookingService(UserRepository userRepository, ShowRepository showRepository, ShowSeatRepository showSeatRepository, PricingService pricingService, BookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.showRepository = showRepository;
        this.showSeatRepository = showSeatRepository;
        this.pricingService = pricingService;
        this.bookingRepository = bookingRepository;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Booking bookMovie(Long userId, List<Long> seatIds, Long showId)
    {
        //<----Start Lock From Here For Today----->
        //1.Get the user from the userId
        //2.Get the show from the showId
        //<------Take A lock----------->
        //3.Get the show seats from the seatIds
        //4.check if the seats are available
        //5. If Yes, Make the status as Locked or Booking in Progress
        //<------Release the Lock------->
        //6.Save updated show seats in DB and end the lock
        //7.<------End the lock from here For Today-->

        Optional<User> userOptional=userRepository.findById(userId);
        if(userOptional.isEmpty())
        {
            throw new RuntimeException();
        }
        User bookedBy=userOptional.get();

        Optional<Show>showOptional=showRepository.findById(showId);
        if(showOptional.isEmpty())
        {
            throw new RuntimeException();
        }
        Show bookedShow=showOptional.get();

        List<ShowSeat>showSeats=showSeatRepository.findAllById(seatIds);

        for(ShowSeat showSeat:showSeats)
        {
            if(!((showSeat.getShowSeatStatus().equals(ShowSeatStatus.AVAILABLE)) ||((showSeat.getShowSeatStatus().equals(ShowSeatStatus.BLOCKED)) && (Duration.between(showSeat.getBlockedAt().toInstant(),new Date().toInstant()).toMinutes()>15)))){
                throw new RuntimeException();
            }

        }

        List<ShowSeat>savedShowSeats=new ArrayList<>();

        for(ShowSeat showSeat:showSeats)
        {
            showSeat.setShowSeatStatus(ShowSeatStatus.BLOCKED);
            showSeat.setBlockedAt(new Date());
            savedShowSeats.add(showSeatRepository.save(showSeat));
        }

        Booking booking=new Booking();
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setShowSeats(savedShowSeats);
        booking.setUser(bookedBy);
        booking.setBookedAt(new Date());
        booking.setShow(bookedShow);
        //Amount
        booking.setAmount(pricingService.calculatePrice(savedShowSeats,bookedShow));

        Booking savedBooking=bookingRepository.save(booking);

        return savedBooking;

    }

}
