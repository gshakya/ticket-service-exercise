package com.walmart.store.recruiting.ticket.domain;

public class ReserveSeat {
	
	private String id;
    private int seatId;
    private int numSeats;

    
    public ReserveSeat(SeatHold s){
    	this.id= s.getId();
    	this.seatId = s.getSeatId();
    	this.numSeats = s.getNumSeats();
    }


	public String getId() {
		return id;
	}


	public int getSeatId() {
		return seatId;
	}


	public int getNumSeats() {
		return numSeats;
	}

    
}
