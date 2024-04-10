package com.scaler.bookmyshow.dtos;


// To simply things we can directly return booking object, to simply things on the client side, in reality we would be returning booking id;
//In parking lot we were returning object, but in here we are returning like this for a change


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BookMovieResponseDto {
    private int amount;
    private Long bookingId;
    private ResponseStatus responseStatus;

}
