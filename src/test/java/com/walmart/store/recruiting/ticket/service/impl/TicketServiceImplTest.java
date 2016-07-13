package com.walmart.store.recruiting.ticket.service.impl;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import com.walmart.store.recruiting.ticket.domain.ReserveSeat;
import com.walmart.store.recruiting.ticket.domain.SeatHold;
import com.walmart.store.recruiting.ticket.domain.Venue;
import com.walmart.store.recruiting.ticket.service.TicketService;

/**
 * A few basic tests for the TicketService.
 */
public class TicketServiceImplTest {

	private static final Venue TEN_SEAT_VENUE = new Venue(0, 2, 5);
	private TicketService ticketService;

	@Before
	public void init() {
		ticketService = new TicketServiceImpl(TEN_SEAT_VENUE);
	}

	@Test
	public void testSimpleSeatHold() {
		Optional<SeatHold> hold = ticketService.findAndHoldSeats(1);
		assertTrue(hold.isPresent());
		assertNotNull(hold.get().getId());
		assertEquals(1, hold.get().getNumSeats());
		assertEquals(9, ticketService.numSeatsAvailable());

		hold = ticketService.findAndHoldSeats(5);
		assertTrue(hold.isPresent());
		assertNotNull(hold.get().getId());
		assertEquals(5, hold.get().getNumSeats());
		assertEquals(4, ticketService.numSeatsAvailable());
	}

	@Test
	public void testReserveSeats() {
		Optional<SeatHold> hold = ticketService.findAndHoldSeats(5);
		assertTrue(hold.isPresent());
		assertNotNull(hold.get().getId());
		assertEquals(5, hold.get().getNumSeats());
		assertEquals(5, ticketService.numSeatsAvailable());

		Optional<String> reservationId = ticketService.reserveSeats(hold.get().getId());
		assertTrue(reservationId.isPresent());
		assertEquals(hold.get().getId(), reservationId.get());
	}

	@Test
	public void testReserveSeatsWithInvalidHold() {
		Optional<String> reservationId = ticketService.reserveSeats("AAAA");
		assertFalse(reservationId.isPresent());
	}

	@Test
	public void testMaxSeatHold() {
		Optional<SeatHold> hold = ticketService.findAndHoldSeats(10);
		assertTrue(hold.isPresent());
		assertNotNull(hold.get().getId());
		assertEquals(10, hold.get().getNumSeats());
	}

	@Test
	public void testEmptySeatHoldReturnedWhenRequestExceedsCapacity() {
		Optional<SeatHold> hold = ticketService.findAndHoldSeats(11);
		assertTrue(!hold.isPresent());
	}

	@Test
	public void testEmptySeatHoldReturnedWhenVenueIsFull() {
		testMaxSeatHold();
		Optional<SeatHold> hold = ticketService.findAndHoldSeats(1);
		assertTrue(!hold.isPresent());
	}

	@Test
	public void testHoldRelease() {
		Optional<SeatHold> hold = ticketService.findAndHoldSeats(1);
		// indicate the seat is holded before time out
		assertEquals(9, ticketService.numSeatsAvailable());
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// indicate the seat is released after time out
		assertEquals(10, ticketService.numSeatsAvailable());

	}

	@Test
	public void testSeatHold() {
		Optional<SeatHold> hold = ticketService.findAndHoldSeats(1);
		Map<String, SeatHold> holdedSeats;

		// Testing retention of Holded seats before timeout
		holdedSeats = ticketService.getHoldedSeats();
		for (Entry<String, SeatHold> holdedSeat : holdedSeats.entrySet()) {
			assertEquals(hold.get().getId(), holdedSeat.getKey());
			assertEquals(1, holdedSeat.getValue().getSeatId());
			assertEquals(1, holdedSeat.getValue().getNumSeats());
			assertEquals(hold.get().getId(), holdedSeat.getValue().getId());
		}

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Testing release of holded seats in Maps after timeout
		holdedSeats = ticketService.getHoldedSeats();
		assertFalse(holdedSeats.containsKey(hold.get().getId()));
	}

	@Test
	public void testReservation() {

		Map<String, ReserveSeat> reservedSeats;

		// Testing reservation of 1 seat with seatId starting at 2
		Optional<SeatHold> hold1 = ticketService.findAndHoldSeats(2);
		ticketService.reserveSeats(hold1.get().getId());
		reservedSeats = ticketService.getReservedSeats();

		for (Entry<String, ReserveSeat> reserveSeat : reservedSeats.entrySet()) {
			assertEquals(hold1.get().getId(), reserveSeat.getKey());
			assertEquals(1, reserveSeat.getValue().getSeatId());
			assertEquals(2, reserveSeat.getValue().getNumSeats());
			assertEquals(hold1.get().getId(), reserveSeat.getValue().getId());
		}

		// Testing Second Reservation of 2 Seats. of reservation of first on

		Optional<SeatHold> hold2 = ticketService.findAndHoldSeats(2);
		ticketService.reserveSeats(hold2.get().getId());
		reservedSeats = ticketService.getReservedSeats();

		ReserveSeat secondReservation = reservedSeats.get(hold2.get().getId());
		assertEquals(3, secondReservation.getSeatId());
		assertEquals(2, secondReservation.getNumSeats());

	}
}
