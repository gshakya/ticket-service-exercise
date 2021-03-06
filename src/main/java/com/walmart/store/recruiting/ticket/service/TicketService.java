package com.walmart.store.recruiting.ticket.service;

import java.util.Map;
import java.util.Optional;

import com.walmart.store.recruiting.ticket.domain.ReserveSeat;
import com.walmart.store.recruiting.ticket.domain.SeatHold;

/**
 * An interface for querying and reserving seats from a single static venue.
 */
public interface TicketService {

    /**
     * The number of seats in the venue that are neither held nor reserved
     *
     * @return the number of tickets available
     */
    int numSeatsAvailable();

    /**
     * Find and hold the best available seats for a customer
     *
     * @param numSeats the number of seats to find and hold
     * @return a SeatHold object identifying the specific seats and related information
     */
    Optional<SeatHold> findAndHoldSeats(int numSeats);

    /**
     * Commit seats held for a specific customer
     *
     * @param seatHoldId the seat hold identifier
     * @return a reservation confirmation code, if the reservation has not expired.
     */
    Optional<String> reserveSeats(String seatHoldId);
    
    Map<String,ReserveSeat> getReservedSeats();
    
    Map<String, SeatHold> getHoldedSeats();
    

}