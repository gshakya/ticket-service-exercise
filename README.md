# ticket-service-exercise
Modified by: Gunjan Shakya
Email: gunjan.shakya@outlook.com
Phone: 641-919-3469

1. **Seat holds expire.**  After some period of time, held seats that are not reserved are returned to the pool of available seats.
	-> Created a Inner Class HoldTimer to handle the timeout. 
	-> Timeout set as 3 Seconds	
	-> Added a new variable (nextAvailableSeatId) to indicate the next available Seats
	-> The available Seats and nextAvailableSeatId will increase or decrease depending upon the holded seats and Reserved Seat
	-> Known bug 
		-> if a user holds some no of seat and within the hold timeout period if any another user holds and reserves some seat
		   the holded seats by earlier user would be released but other users won't be able be reserve the same seat, and will also 
		   changes the nextAvailableSeat to already reserved seat.
		-> Probable solution: while releasing the holded seats recheck the reserved seat map and reassign the SeatId if the changes.
	
2. **Seats are assigned together.** Seats and rows are numbered. Seats are held and reserved in blocks. 
	-> Assumption: Each Venue has seat orders as follows
				1-- 2--3 --4 --5 --6 --7 --8
				9--10--11--12--13--14--15--16
	-> To represent the seats in the reservation. SeatId and no of seats reserved are added
	-> To display reserved seats Interface is extend to return reservedSeats (i.e. map of ReserveSeat)
	-> reservedSeats has details of ReservationID , SeatId and no of Seats Reserved.


Implement a simple ticket service that facilitates the discovery, temporary hold, and final reservation of seats within a high-demand performance venue.

Assume that the venue has a stage and one level of seating, as such:

````
        ----------[[  STAGE  ]]----------
        ---------------------------------
        sssssssssssssssssssssssssssssssss
        sssssssssssssssssssssssssssssssss
        sssssssssssssssssssssssssssssssss
        sssssssssssssssssssssssssssssssss
        sssssssssssssssssssssssssssssssss
        sssssssssssssssssssssssssssssssss
        sssssssssssssssssssssssssssssssss
        sssssssssssssssssssssssssssssssss
        sssssssssssssssssssssssssssssssss
````


The following API can be used to find and reserve seats:

````
public interface TicketService {

/**
 * The number of seats in the requested level that are neither held nor reserved
 */
  int numSeatsAvailable();

/**
 * Find and hold the best available seats for a customer
 * 
 * @param numSeats the number of seats to find and hold
 * @return a SeatHold object identifying the found seats and related information 
 */
  SeatHold findAndHoldSeats(int numSeats);

/**
 * Complete reservation of held seats
 * 
 * @param seatHoldId the seat hold identifier
 * @return a reservation confirmation code 
 */  
  String reserveSeats(int seatHoldId);
}

````

##Instructions
We've created a simple and highly-naive implemenation of the ticket service.
Your assignment is to improve the implementation by adding the following features:

1. **Seat holds expire.**  After some period of time, held seats that are not reserved are returned to the pool of available seats.
2. **Seats are assigned together.** Seats and rows are numbered. Seats are held and reserved in blocks. 

## Notes
* We would like to see a design that can scale to support multiple concurrent users. 
* *Simple is better*. For example, a lazy seat expiration model may be a good alternative to a background thread or timer.
* We understand that tradeoffs must be made to complete the exercise within the alloted time window. Do your best to document any simplifying assumptions and design considerations as you work through the problem.
