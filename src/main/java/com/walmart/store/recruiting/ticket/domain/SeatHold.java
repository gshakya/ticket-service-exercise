package com.walmart.store.recruiting.ticket.domain;

/**
 * This POJO contains the data relevant to a successful seat hold request,
 * including the seat hold id which may be used later to permanently reserve the
 * seats.
 */
public class SeatHold {

	private String id;
	private int seatId; // seat Id added for tracking the seat
	private int numSeats;

	/**
	 * Constructor.
	 *
	 * @param id
	 *            the unique hold identifier
	 * @param numSeats
	 *            the number of seats that were held.
	 */
	public SeatHold(String id, int seatId, int numSeats) {
		this.id = id;
		this.seatId = seatId;

		this.numSeats = numSeats;
	}

	/**
	 * @return the seat hold (reservation) id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the number of seats that are being held
	 */
	public int getNumSeats() {
		return numSeats;
	}

		
	public int getSeatId() {
		return seatId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		SeatHold seatHold = (SeatHold) o;

		return id.equals(seatHold.id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

}
