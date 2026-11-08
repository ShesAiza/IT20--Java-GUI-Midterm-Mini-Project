/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package roomreservationrecorder;

/**
 *
 * @author Aiza Mae
 */
public class HOME extends javax.swing.JFrame {
    
    // SHARED BACKEND
    private final RoomReservationRecorder tracker =
        new RoomReservationRecorder();

    // Records window reference
    private RECORDTAB recordsWindow = null;

    /**
     * Creates new form OPERATOR
     */
   public HOME() {
    initComponents();

    setLocationRelativeTo(null);
    setTitle("Hotel Room Reservation Tracker");

    // Room types
    jComboBox1.setModel(
        new javax.swing.DefaultComboBoxModel<>(
            new String[]{"Single", "Double", "Triple", "Suites", "Deluxe", "VIP"}
        )
    );

    // Default price
    jTextField2.setText("1200");

    // Automatically update room price
    jComboBox1.addActionListener(e -> updateRoomPrice());

    // Connect buttons
    jButton1.addActionListener(e -> addRoom());
    jButton3.addActionListener(e -> bookRoom());
    jButton4.addActionListener(e -> nextBooking());
    jButton6.addActionListener(e -> assignOrVacate());
    jButton7.addActionListener(e -> searchRoom());

    updateQueueStatus();
    }
   
    // UPDATE PRICE BASED ON ROOM TYPE
    private void updateRoomPrice() {

    String type = (String) jComboBox1.getSelectedItem();

    if ("Single".equals(type)) {
        jTextField2.setText("1200");
    } else if ("Double".equals(type)) {
        jTextField2.setText("1800");
    }else if ("Triple".equals(type)) {
        jTextField2.setText("3500");
    } else if ("Suites".equals(type)) {
        jTextField2.setText("5000");
    }else if ("Deluxe".equals(type)) {
        jTextField2.setText("7000");
    }else if ("VIP".equals(type)) {
        jTextField2.setText("10000");
    }
    }
    
    // UPDATE STATUS LABEL
    private void updateStatus(String message) {
    jLabel7.setText(message);
    }

    // UPDATE BOOKING QUEUE LABEL
    private void updateQueueStatus() {

    RoomReservationRecorder.Booking next =
            tracker.peekBooking();

    if (next == null) {
        jLabel8.setText(
            "Booking Queue: 0 Guests | Next: None"
        );
    } else {
        jLabel8.setText(
            "Booking Queue: " + tracker.getQueueCount()
            + " Guests | Next: " + next.guestName
        );
    }
    }
    
    // REFRESH RECORDS TABLE IF OPEN
    private void refreshRecords() {

    if (recordsWindow != null &&
        recordsWindow.isDisplayable()) {

        recordsWindow.refreshTable();
    }
    }
    
    // ADD ROOM
    private void addRoom() {

    try {
        String numberText = jTextField1.getText().trim();
        String priceText = jTextField2.getText().trim();

        if (numberText.isEmpty() || priceText.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(
                this, "Please enter room number and price."
            );
            return;
        }

        int number = Integer.parseInt(numberText);
        double price = Double.parseDouble(priceText);

        String type = (String) jComboBox1.getSelectedItem();

        if (number <= 0 || price <= 0) {
            javax.swing.JOptionPane.showMessageDialog(
                this,
                "Room number and price must be greater than zero."
            );
            return;
        }

        if (tracker.addRoom(number, type, price)) {

            javax.swing.JOptionPane.showMessageDialog(
                this, "Room " + number + " added successfully!"
            );

            updateStatus("Room added successfully.");
            refreshRecords();
            clearFields();

        } else {

            javax.swing.JOptionPane.showMessageDialog(
                this,
                "Room already exists or storage is full."
            );

            updateStatus("Failed to add room.");
        }

    } catch (NumberFormatException e) {

        javax.swing.JOptionPane.showMessageDialog(
            this,
            "Room number and price must be valid numbers."
        );
    }
    }
    
    // BOOK ROOM - ADD TO FIFO QUEUE
    private void bookRoom() {
    try {
        String numberText = jTextField1.getText().trim();
        String guest = jTextField3.getText().trim();

        if (numberText.isEmpty() || guest.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(
                this, "Enter a room number and guest name."
            );
            return;
        }

        int number = Integer.parseInt(numberText);
        int index = tracker.searchRoom(number);

        if (index == -1) {
            javax.swing.JOptionPane.showMessageDialog(
                this, "Room number does not exist. Add the room first."
            );
            return;
        }

        RoomReservationRecorder.Room room = tracker.getRoomAt(index);

        if (!room.status.equals("Available")) {
            javax.swing.JOptionPane.showMessageDialog(
                this, "This room is already occupied."
            );
            return;
        }

        if (tracker.enqueueBooking(number, guest)) {
            javax.swing.JOptionPane.showMessageDialog(
                this, "Booking successfully added!\n"
                + "Guest: " + guest
                + "\nRoom: " + number
            );

            updateStatus("Booking added successfully.");
            updateQueueStatus();
            refreshRecords();
        } else {
            javax.swing.JOptionPane.showMessageDialog(
                this, "Booking failed. This room may already have a pending booking."
            );
            updateStatus("Booking failed.");
        }

    } catch (NumberFormatException e) {
        javax.swing.JOptionPane.showMessageDialog(
            this, "Enter a valid room number."
        );
    }
    }
    
    // PROCESS NEXT BOOKING - FIFO
    private void nextBooking() {

    RoomReservationRecorder.Booking booking =
            tracker.dequeueBooking();

    if (booking == null) {

        javax.swing.JOptionPane.showMessageDialog(
            this, "There are no pending bookings."
        );

        updateStatus("Booking queue is empty.");

    } else {

        int index = tracker.searchRoom(booking.roomNumber);

        if (index != -1) {

            RoomReservationRecorder.Room room =
                    tracker.getRoomAt(index);

            if (room.status.equals("Occupied") &&
                room.guestName.equals(booking.guestName)) {

                javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Next booking processed!\n"
                    + "Guest: " + booking.guestName
                    + "\nRoom: " + booking.roomNumber
                );

                updateStatus("Booking processed successfully.");

            } else {

                javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Booking removed, but room "
                    + booking.roomNumber
                    + " could not be assigned."
                );

                updateStatus("Booking could not be assigned.");
            }
        }

    }

    updateQueueStatus();
    refreshRecords();
    }

    
    // ROOM AVAILABILITY
    private void showAvailability() {

    int total = tracker.getSize();
    int available = tracker.countAvailableRooms();
    int occupied = tracker.countOccupiedRooms();

    javax.swing.JOptionPane.showMessageDialog(
        this,
        "ROOM AVAILABILITY\n\n"
        + "Total Rooms: " + total
        + "\nAvailable Rooms: " + available
        + "\nOccupied Rooms: " + occupied,
        "Room Availability",
        javax.swing.JOptionPane.INFORMATION_MESSAGE
    );

    updateStatus("Room availability displayed.");
    }

    // ASSIGN OR VACATE
    private void assignOrVacate() {

    try {

        String input = jTextField1.getText().trim();

        if (input.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(
                this, "Enter a room number first."
            );
            return;
        }

        int number = Integer.parseInt(input);

        String[] options = {
            "Assign Room",
            "Vacate Room"
        };

        int choice = javax.swing.JOptionPane.showOptionDialog(
            this,
            "Choose an operation for Room " + number,
            "Assign / Vacate",
            javax.swing.JOptionPane.DEFAULT_OPTION,
            javax.swing.JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );

        if (choice == 0) {

            String guest = jTextField3.getText().trim();

            if (guest.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(
                    this, "Enter a guest name first."
                );
                return;
            }

            if (tracker.assignRoom(number, guest)) {

                javax.swing.JOptionPane.showMessageDialog(
                    this, "Room assigned to " + guest
                );

                updateStatus("Room assigned.");
                refreshRecords();

            } else {

                javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Room not found or already occupied."
                );
            }

        } else if (choice == 1) {

            if (tracker.vacateRoom(number)) {

                javax.swing.JOptionPane.showMessageDialog(
                    this, "Room " + number + " vacated successfully."
                );

                updateStatus("Room vacated.");
                refreshRecords();

            } else {

                javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Room not found or already available."
                );
            }
        }

    } catch (NumberFormatException e) {

        javax.swing.JOptionPane.showMessageDialog(
            this, "Enter a valid room number."
        );
    }
    }
    
    // SEARCH ROOM - MANUAL LINEAR SEARCH
    // SEARCH ROOM BY ROOM NUMBER OR GUEST NAME
    private void searchRoom() {

    String input = javax.swing.JOptionPane.showInputDialog(
        this,
        "Enter Room Number or Guest Name:",
        "Search Room",
        javax.swing.JOptionPane.QUESTION_MESSAGE
    );

    // If Cancel is clicked
    if (input == null) {
        return;
    }

    input = input.trim();

    if (input.isEmpty()) {
        javax.swing.JOptionPane.showMessageDialog(
            this, "Please enter a room number or guest name."
        );
        return;
    }

    int foundIndex = -1;

    // First, try searching by room number
    try {
        int roomNumber = Integer.parseInt(input);
        foundIndex = tracker.searchRoom(roomNumber);

    } catch (NumberFormatException e) {

        // If input is not a number, search by guest name
        for (int i = 0; i < tracker.getSize(); i++) {

            RoomReservationRecorder.Room room =
                tracker.getRoomAt(i);

            if (room.guestName != null &&
                room.guestName.toLowerCase().contains(
                    input.toLowerCase()
                )) {

                foundIndex = i;
                break;
            }
        }
    }

    // If no record was found
    if (foundIndex == -1) {

        javax.swing.JOptionPane.showMessageDialog(
            this,
            "No room record found for: " + input,
            "Search Result",
            javax.swing.JOptionPane.INFORMATION_MESSAGE
        );

        updateStatus("No matching room found.");
        return;
    }

    // Open the Records window
    if (recordsWindow == null ||
        !recordsWindow.isDisplayable()) {

        recordsWindow = new RECORDTAB(tracker);
    }

    // Display only the matching room
    recordsWindow.showOnlyRoom(foundIndex);

    recordsWindow.setVisible(true);
    recordsWindow.setLocationRelativeTo(this);
    recordsWindow.toFront();

    updateStatus("Room record found.");
    }   

    // CLEAR INPUT FIELDS
    private void clearFields() {

    jTextField1.setText("");
    jComboBox1.setSelectedIndex(0);
    jTextField2.setText("1200");
    jTextField3.setText("");

    jTextField1.requestFocus();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton9 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jTextField3 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();

        jButton9.setText("jButton9");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 102, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 0, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Status:");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("?");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 434, 320, -1));

        jPanel3.setBackground(new java.awt.Color(255, 0, 204));
        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Room Info & Operations");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 320, -1));

        jTextField1.setBackground(new java.awt.Color(255, 255, 255));
        jTextField1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 80, 117, 30));

        jTextField2.setBackground(new java.awt.Color(255, 255, 255));
        jTextField2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 176, 117, 30));

        jComboBox1.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 128, 117, 30));

        jTextField3.setBackground(new java.awt.Color(255, 255, 255));
        jTextField3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 224, 117, 30));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Room Number:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 84, 107, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Room Type:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 132, -1, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Room Price:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 180, -1, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Guest Name:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 228, -1, -1));

        jButton1.setBackground(new java.awt.Color(255, 0, 204));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 0, 0));
        jButton1.setText("ADD ROOM");
        jButton1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(44, 266, 105, 31));

        jButton2.setBackground(new java.awt.Color(255, 0, 204));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(0, 0, 0));
        jButton2.setText("CLEAR");
        jButton2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton2.addActionListener(this::jButton2ActionPerformed);
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(161, 266, 105, 31));

        jButton3.setBackground(new java.awt.Color(255, 0, 204));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(0, 0, 0));
        jButton3.setText("BOOK ROOMS");
        jButton3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(44, 303, 105, 31));

        jButton4.setBackground(new java.awt.Color(255, 0, 204));
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(0, 0, 0));
        jButton4.setText("NEXT BOOKING");
        jButton4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(161, 303, 105, 31));

        jButton5.setBackground(new java.awt.Color(255, 0, 204));
        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton5.setForeground(new java.awt.Color(0, 0, 0));
        jButton5.setText("ROOM AVAIL");
        jButton5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton5.addActionListener(this::jButton5ActionPerformed);
        jPanel1.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(44, 340, 105, 31));

        jButton6.setBackground(new java.awt.Color(255, 0, 204));
        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton6.setForeground(new java.awt.Color(0, 0, 0));
        jButton6.setText("ASSIGN/VACATE");
        jButton6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(161, 340, 105, 31));

        jButton7.setBackground(new java.awt.Color(255, 0, 204));
        jButton7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton7.setForeground(new java.awt.Color(0, 0, 0));
        jButton7.setText("SEARCH ROOM");
        jButton7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(44, 377, 105, 31));

        jButton8.setBackground(new java.awt.Color(255, 0, 204));
        jButton8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton8.setForeground(new java.awt.Color(0, 0, 0));
        jButton8.setText("VIEW RECORDS");
        jButton8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton8.addActionListener(this::jButton8ActionPerformed);
        jPanel1.add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(161, 377, 105, 31));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("Booking Queue: 0 Guest | Next: ?");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 414, -1, -1));

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/roomreservationrecorder/Home background.jpg"))); // NOI18N
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 320, 460));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        clearFields();
    updateStatus("Fields cleared.");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        showAvailability();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
       if (recordsWindow == null ||
        !recordsWindow.isDisplayable()) {

        recordsWindow = new RECORDTAB(tracker);
    }

    recordsWindow.refreshTable();
    recordsWindow.setVisible(true);
    recordsWindow.setLocationRelativeTo(this);
    }//GEN-LAST:event_jButton8ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
    javax.swing.SwingUtilities.invokeLater(() -> {
        new HOME().setVisible(true);
    });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}
