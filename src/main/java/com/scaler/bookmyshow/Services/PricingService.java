package com.scaler.bookmyshow.Services;

import com.scaler.bookmyshow.Models.Show;
import com.scaler.bookmyshow.Models.ShowSeat;
import com.scaler.bookmyshow.Models.ShowSeatType;
import com.scaler.bookmyshow.repositories.ShowSeatRepository;
import com.scaler.bookmyshow.repositories.ShowSeatTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PricingService {
    private ShowSeatTypeRepository showSeatTypeRepository;

    public int calculatePrice(List<ShowSeat>showSeats, Show show)
    {
        List<ShowSeatType>showSeatTypes=showSeatTypeRepository.findAllByShow(show);

        int amount=0;
        for(ShowSeat showSeat:showSeats)
        {
            for(ShowSeatType showSeatType:showSeatTypes)
            {
                if(showSeat.getSeat().getSeatType().equals(showSeatType.getSeatType()))
                {
                    amount+=showSeatType.getPrice();
                }
            }

        }
        return amount;


    }
}
