package com.walmart.store.recruiting.ticket.service.impl;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.walmart.store.recruiting.ticket.domain.SeatHold;
import com.walmart.store.recruiting.ticket.domain.Venue;
import com.walmart.store.recruiting.ticket.service.TicketService;

/**
 * A ticket service implementation.
 */
public class TicketServiceImpl implements TicketService {

	private int seatsAvailable;
	private int seatsReserved;

	private int nextAvailableSeat;

	// converted to ConcurrentHashMap for thread Safety
	private Map<String, SeatHold> seatHoldMap = new ConcurrentHashMap<>();

	public TicketServiceImpl(Venue venue) {
		seatsAvailable = venue.getMaxSeats();
		nextAvailableSeat = venue.getNextAvailableSeat();
	}

	@Override
	public int numSeatsAvailable() {
		return seatsAvailable;
	}

	public int numSeatsReserved() {
		return this.seatsReserved;
	}

	public int getNextAvailableSeat() {
		return nextAvailableSeat;
	}

	@Override
	public synchronized Optional<SeatHold> findAndHoldSeats(int numSeats) {
		// made the method synchronized so that only one thread can access the
		// method at one time
		Optional<SeatHold> optionalSeatHold = Optional.empty();

		if (seatsAvailable >= numSeats) {
			String holdId = generateId();
			SeatHold seatHold = new SeatHold(holdId, numSeats);
			optionalSeatHold = Optional.of(seatHold);
			seatHoldMap.put(holdId, seatHold);
			seatsAvailable -= numSeats;
			nextAvailableSeat += numSeats;
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
			seatHoldMap.remove(seatHoldId);

		}

		return optionalReservation;
	}

	private String generateId() {
		return UUID.randomUUID().toString();
	}

	// Inner Class created for holding timer

	class HoldTimer extends Thread {
		private String holdId;

		HoldTimer(String holdId) {
			this.holdId = holdId;
		}

		public void run() {

			try {
				Thread.sleep(3000); // 3 second wait
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
			removeHoldSeats(seatHoldMap.get(holdId).getNumSeats());
			seatHoldMap.remove(holdId);
		}

		public synchronized void removeHoldSeats(int seats) { //
			seatsAvailable += seats;
			nextAvailableSeat -= seats;

		}
	}

}
