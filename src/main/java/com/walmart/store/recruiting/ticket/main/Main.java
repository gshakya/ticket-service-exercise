package com.walmart.store.recruiting.ticket.main;

import java.util.Optional;

import com.walmart.store.recruiting.ticket.domain.SeatHold;
import com.walmart.store.recruiting.ticket.domain.Venue;
import com.walmart.store.recruiting.ticket.service.impl.TicketServiceImpl;

public class Main {
	public static void main(String[] args) {
		TicketServiceImpl tImp = new TicketServiceImpl(new Venue(1, 5, 10));
		System.out.println("Next Available Seat: "+ tImp.getNextAvailableSeat());
		System.out.println("Total Available Seat: "+ tImp.numSeatsAvailable());
		
	
		
		Optional<SeatHold> hold = tImp.findAndHoldSeats(10);
		
	
		System.out.println("Next Available Seat: "+ tImp.getNextAvailableSeat());
		System.out.println("Total Available Seat: "+ tImp.numSeatsAvailable());
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Next Available Seat: "+ tImp.getNextAvailableSeat());
		System.out.println("Total Available Seat: "+ tImp.numSeatsAvailable());
		tImp.reserveSeats(hold.get().getId());

	}
}
