
package roomreservationrecorder;

public class RoomReservationRecorder {

        
          // MANUALLY IMPLEMENTED ARRAY
    private final Room[] rooms = new Room[100];
    private int roomCount = 0;

    // MANUALLY IMPLEMENTED CIRCULAR QUEUE
    private final Booking[] bookingQueue = new Booking[100];
    private int front = 0;
    private int rear = -1;
    private int queueCount = 0;

    // ROOM RECORD
    public static class Room {
        public int roomNumber;
        public String roomType;
        public double price;
        public String status;
        public String guestName;

        public Room(int number, String type, double price) {
            this.roomNumber = number;
            this.roomType = type;
            this.price = price;
            this.status = "Available";
            this.guestName = "";
        }
    }

    // BOOKING RECORD
    public static class Booking {
        public int roomNumber;
        public String guestName;

        public Booking(int number, String guest) {
            roomNumber = number;
            guestName = guest;
        }
    }

    // ADD ROOM
    public boolean addRoom(int number, String type, double price) {

        if (number <= 0 || price <= 0 ||
            type == null || type.trim().isEmpty() ||
            roomCount == rooms.length) {
            return false;
        }

        if (searchRoom(number) != -1) {
            return false;
        }

        rooms[roomCount] = new Room(number, type, price);
        roomCount++;

        return true;
    }

    
    // EDIT ROOM
    public boolean updateRoom(int oldNumber, int newNumber,
                          String newType, double newPrice) {

    int index = searchRoom(oldNumber);

    if (index == -1 || newNumber <= 0 ||
        newPrice <= 0 || newType == null ||
        newType.trim().isEmpty()) {
        return false;
    }

    // Prevent duplicate room numbers
    int duplicateIndex = searchRoom(newNumber);

    if (duplicateIndex != -1 && duplicateIndex != index) {
        return false;
    }

    // Update pending bookings if room number changes
    if (oldNumber != newNumber) {
        for (int i = 0; i < queueCount; i++) {
            int position = (front + i) % bookingQueue.length;

            if (bookingQueue[position].roomNumber == oldNumber) {
                bookingQueue[position].roomNumber = newNumber;
            }
        }
    }

    rooms[index].roomNumber = newNumber;
    rooms[index].roomType = newType.trim();
    rooms[index].price = newPrice;

    return true;
}

// DELETE ROOM
public boolean deleteRoom(int number) {

    int index = searchRoom(number);

    if (index == -1) {
        return false;
    }

    // Do not delete occupied rooms
    if (rooms[index].status.equals("Occupied")) {
        return false;
    }

    // Do not delete rooms with pending bookings
    for (int i = 0; i < queueCount; i++) {
        int position = (front + i) % bookingQueue.length;

        if (bookingQueue[position].roomNumber == number) {
            return false;
        }
    }

    // Shift remaining rooms to the left
    for (int i = index; i < roomCount - 1; i++) {
        rooms[i] = rooms[i + 1];
    }

    rooms[roomCount - 1] = null;
    roomCount--;

    return true;
    }

    
    // MANUAL LINEAR SEARCH
    public int searchRoom(int number) {

        for (int i = 0; i < roomCount; i++) {
            if (rooms[i].roomNumber == number) {
                return i;
            }
        }

        return -1;
    }

    // GET ROOM
    public Room getRoomAt(int index) {

        if (index < 0 || index >= roomCount) {
            return null;
        }

        return rooms[index];
    }

    // GET TOTAL ROOMS
    public int getSize() {
        return roomCount;
    }

    public boolean assignRoom(int roomNumber, String guestName) {

    int index = searchRoom(roomNumber);

    if (index == -1) {
        return false;
    }

    Room room = rooms[index];

    // Room is already being used
    if (room.status.equalsIgnoreCase("Occupied")) {
        return false;
    }

    // Assign guest
    room.guestName = guestName;
    room.status = "Occupied";

    return true;
    }

    // VACATE ROOM
    public boolean vacateRoom(int roomNumber) {

    int index = searchRoom(roomNumber);

    if (index == -1) {
        return false;
    }

    Room room = rooms[index];

    // Remove guest
    room.guestName = "";
    room.status = "Available";

    return true;
    }

    // COUNT AVAILABLE ROOMS
    public int countAvailableRooms() {

        int count = 0;

        for (int i = 0; i < roomCount; i++) {
            if (rooms[i].status.equals("Available")) {
                count++;
            }
        }

        return count;
    }

    // COUNT OCCUPIED ROOMS
    public int countOccupiedRooms() {

        int count = 0;

        for (int i = 0; i < roomCount; i++) {
            if (rooms[i].status.equals("Occupied")) {
                count++;
            }
        }

        return count;
    }

    // ADD BOOKING TO QUEUE
    public boolean enqueueBooking(int number, String guest) {

        int index = searchRoom(number);

        if (index == -1 ||
            guest == null ||
            guest.trim().isEmpty() ||
            queueCount == bookingQueue.length ||
            !rooms[index].status.equals("Available")) {
            return false;
        }

        // Prevent duplicate pending bookings for same room
        for (int i = 0; i < queueCount; i++) {
            int position = (front + i) % bookingQueue.length;

            if (bookingQueue[position].roomNumber == number) {
                return false;
            }
        }

        rear = (rear + 1) % bookingQueue.length;

        bookingQueue[rear] =
                new Booking(number, guest.trim());

        queueCount++;

        return true;
    }

    // PROCESS NEXT BOOKING - FIFO
    public Booking dequeueBooking() {

        if (queueCount == 0) {
            return null;
        }

        Booking next = bookingQueue[front];

        front = (front + 1) % bookingQueue.length;
        queueCount--;

        int index = searchRoom(next.roomNumber);

        if (index != -1 &&
            rooms[index].status.equals("Available")) {

            assignRoom(next.roomNumber, next.guestName);
            return next;
        }

        return next;
    }

    // VIEW NEXT BOOKING
    public Booking peekBooking() {

        if (queueCount == 0) {
            return null;
        }

        return bookingQueue[front];
    }

    // QUEUE COUNT
    public int getQueueCount() {
        return queueCount;
    }

    // MAIN METHOD
    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(() -> {
            HOME home = new HOME();
            home.setVisible(true);
        });
    }
    public Booking getBookingAt(int position) {
    if (position < 0 || position >= queueCount) {
        return null;
    }

    int actualPosition = (front + position) % bookingQueue.length;
    return bookingQueue[actualPosition];
    }
    
    
}
