# IT20 Midterm: Hotel Room Reservation Tracker

## Overview

The **Hotel Room Reservation Tracker** is a Java desktop application designed to manage hotel rooms, guest reservations, room assignments, and booking records. The system uses a Java Swing GUI and manually implemented data structures to manage room records and pending bookings.

## Features

- **Add Room:** Adds a new room with a room number, room type, and price.
- **Edit Room:** Updates the room number, room type, and room price of an existing room.
- **Delete Room:** Removes an available room that does not have a pending booking.
- **Assign Room:** Assigns an available room to a guest.
- **Vacate Room:** Changes an occupied room back to available status.
- **Book Room:** Adds a guest's reservation to the booking queue.
- **Next Booking:** Processes the next pending booking using **FIFO (First In, First Out)**.
- **Room Availability:** Displays the total number of rooms, available rooms, and occupied rooms.
- **Search Room:** Searches for a room by room number or guest name using a manual linear search.
- **View Records:** Displays room and booking records in tables.
- **Filter Records:** Filters rooms by room type and availability status.
- **Edit and Delete Records:** Allows selected room records to be edited or deleted from the records table.
- **Booking Queue:** Displays the current number of pending bookings and the next guest in line.
- **Input Validation:** Prevents invalid room numbers, prices, guest names, duplicate rooms, and invalid booking operations.

The system stores room records using a manually implemented array and manages pending bookings using a manually implemented circular queue.

## Setup

1. Clone the repository:

```bash
git clone https://github.com/ShesAiza/IT20--Java-GUI-Midterm-Mini-Project.git
```

2. Open the project in **Apache NetBeans**.

3. Locate the `RoomReservationRecorder` project/package.

4. Run the Java project. The application starts the `HOME` GUI window.

5. Use the main window to:
   - Add rooms
   - Book rooms
   - Process the next booking
   - Check room availability
   - Assign or vacate rooms
   - Search rooms
   - View room
