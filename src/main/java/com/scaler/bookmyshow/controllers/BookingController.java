package com.scaler.bookmyshow.controllers;

import com.scaler.bookmyshow.Models.Booking;
import com.scaler.bookmyshow.Services.BookingService;
import com.scaler.bookmyshow.dtos.BookMovieRequestDto;
import com.scaler.bookmyshow.dtos.BookMovieResponseDto;
import com.scaler.bookmyshow.dtos.ResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;


//where ever we want to tell the spring boot to create object of that class you should annotate with @component

@Controller
public class BookingController {
    private BookingService bookingService;

    //My spring automatically knows these dependencies the constructor is taking,it has to be provided from the ones
    //that spring has created
    @Autowired
    public BookingController(BookingService bookingService){
        this.bookingService=bookingService;
    }

    public BookMovieResponseDto bookMovie(BookMovieRequestDto bookMovieRequestDto)
    {
        BookMovieResponseDto bookMovieResponseDto=new BookMovieResponseDto();
        Booking booking;

        try{

            booking=bookingService.bookMovie(bookMovieRequestDto.getUserId(),bookMovieRequestDto.getShowSeatIds(), bookMovieRequestDto.getShowId());
            bookMovieResponseDto.setResponseStatus(ResponseStatus.SUCCESS);
            bookMovieResponseDto.setBookingId(booking.getId());
            bookMovieResponseDto.setAmount(booking.getAmount());

        }catch(Exception ex){
            bookMovieResponseDto.setResponseStatus(ResponseStatus.FAILURE);
        }
        return bookMovieResponseDto;


    }

}
