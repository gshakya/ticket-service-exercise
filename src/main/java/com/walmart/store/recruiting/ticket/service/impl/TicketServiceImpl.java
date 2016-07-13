package com.walmart.store.recruiting.ticket.service.impl;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.walmart.store.recruiting.ticket.domain.ReserveSeat;
import com.walmart.store.recruiting.ticket.domain.SeatHold;
import com.walmart.store.recruiting.ticket.domain.Venue;
import com.walmart.store.recruiting.ticket.service.TicketService;

/**
 * A ticket service implementation.
 */
public class TicketServiceImpl implements TicketService {

	private int seatsAvailable;
	private int seatsReserved;
	private final int HOLD_TIME_OUT = 3000;

	// Assuming each seat is given an numerical number as Id which is in
	// ascending order from first to last in the venue
	private int nextAvailableSeatId;

	// converted to ConcurrentHashMap for thread Safety
	private Map<String, SeatHold> seatHoldMap = new ConcurrentHashMap<>();
	private Map<String, ReserveSeat> reservedSeatMap = new ConcurrentHashMap<>();

	public TicketServiceImpl(Venue venue) {
		seatsAvailable = venue.getMaxSeats();
		nextAvailableSeatId = 1;
	}

	@Override
	public int numSeatsAvailable() {
		return seatsAvailable;
	}

	public int numSeatsReserved() {
		return this.seatsReserved;
	}

	public int getNextAvailableSeatId() {
		return nextAvailableSeatId;
	}

	@Override
	public synchronized Optional<SeatHold> findAndHoldSeats(int numSeats) {
		// made the method synchronized so that only one thread can access the
		// method at one time
		Optional<SeatHold> optionalSeatHold = Optional.empty();

		if (seatsAvailable >= numSeats) {
			String holdId = generateId();
			SeatHold seatHold = new SeatHold(holdId, nextAvailableSeatId, numSeats);
			optionalSeatHold = Optional.of(seatHold);
			seatHoldMap.put(holdId, seatHold);
			seatsAvailable -= numSeats;
			nextAvailableSeatId += numSeats;
			HoldTimer h = new HoldTimer(holdId);
			h.start();

		}

		return optionalSeatHold;
	}

	@Override
	public Optional<String> reserveSeats(String seatHoldId) {
		Optional<String> optionalReservation = Optional.empty();
		;
		SeatHold seatHold = seatHoldMap.get(seatHoldId);
		if (seatHold != null) {
			seatsReserved += seatHold.getNumSeats();
			optionalReservation = Optional.of(seatHold.getId());

			// Create Reserved Seat object
			ReserveSeat reserveSeat = new ReserveSeat(seatHold);

			
			
			// -----------Exisiting Bug--------
			// if a user holds some no of seat and within the hold timeout
			// period if any another user holds and reserves some seat
			// the holded seats by earlier user would be released but other
			// users won't be able be reserve the same seat.
			
			// Place the holded Seat from holdmap to Reserve Map
			reservedSeatMap.put(seatHoldId, reserveSeat);
			seatHoldMap.remove(seatHoldId);

		} else {
			// Indicates the hold id is not available
			System.out.println("Log:--- Hold Id not found or timed out");
			
		}

		return optionalReservation;
	}

	private String generateId() {
		return UUID.randomUUID().toString();
	}

	// Inner Class created for hold timing

	class HoldTimer extends Thread {
		private String holdId;

		HoldTimer(String holdId) {
			this.holdId = holdId;
		}

		public void run() {

			try {
				Thread.sleep(HOLD_TIME_OUT); // 3 second wait
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
			if (seatHoldMap.containsKey(holdId)) {
				// Remove only if the holdId
				// is in map, will be remove
				// if the holded seats are
				// reserved
				removeHoldSeats(seatHoldMap.get(holdId).getNumSeats());
				seatHoldMap.remove(holdId);
			}

		}

		public synchronized void removeHoldSeats(int seats) { //
			seatsAvailable += seats;
			nextAvailableSeatId -= seats;

		}
	}
}
