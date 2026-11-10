/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package roomreservationrecorder;

/**
 *
 * @author Aiza Mae
 */
public class RECORDTAB extends javax.swing.JFrame {
    
    private final RoomReservationRecorder tracker;
    private javax.swing.table.DefaultTableModel tableModel;
    private javax.swing.table.DefaultTableModel bookingTableModel;
    
 
   public RECORDTAB(RoomReservationRecorder tracker) {
     initComponents();

    this.tracker = tracker;

    setTitle("Room Reservation Records");
    setLocationRelativeTo(null);
    setDefaultCloseOperation(
        javax.swing.WindowConstants.DISPOSE_ON_CLOSE
    );

    
    
    tableModel = new javax.swing.table.DefaultTableModel(
        new Object[][]{},
        new String[]{
            "Room Number",
            "Room Type",
            "Room Price",
            "Status",
            "Guest Name"
        }
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    jTable1.setModel(tableModel);

    bookingTableModel =
        new javax.swing.table.DefaultTableModel(
            new Object[][]{},
            new String[]{
                "Queue No.",
                "Room Number",
                "Guest Name"
            }
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    
    
    // ROOM TYPE FILTER
    jComboBox2.setModel(
    new javax.swing.DefaultComboBoxModel<>(
        new String[]{
            "All Room Types",
            "Single",
            "Double",
            "Triple",
            "Suites",
            "Deluxe",
            "VIP"
        }
    )
    );

   // ROOM AVAILABILITY FILTER
    jComboBox1.setModel(
    new javax.swing.DefaultComboBoxModel<>(
        new String[]{
            "All Status",
            "Available",
            "Occupied"
            }
        )
    );

    // FILTER ACTION LISTENERS
    jComboBox1.addActionListener(e -> refreshTable());
    jComboBox2.addActionListener(e -> refreshTable());	

    // RESET FILTER BUTTON
    jButton4.addActionListener(e -> {
    jComboBox1.setSelectedIndex(0);
    jComboBox2.setSelectedIndex(0);
    
    refreshTable();
    });

    jTable2.setModel(bookingTableModel);

    refreshTable();
    }
   
   
    // GET DEFAULT PRICE BASED ON ROOM TYPE
    private double getRoomPrice(String type) {

    if ("Single".equals(type)) {
        return 1200;
    } else if ("Double".equals(type)) {
        return 1800;
    } else if ("Triple".equals(type)) {
        return 3500;
    } else if ("Suites".equals(type)) {
        return 5000;
    } else if ("Deluxe".equals(type)) {
        return 7000;
    } else if ("VIP".equals(type)) {
        return 10000;
    }

    return 1200;
}
   
    // EDIT SELECTED ROOM
    private void editSelectedRoom() {

    int selectedRow = jTable1.getSelectedRow();

    if (selectedRow == -1) {
        javax.swing.JOptionPane.showMessageDialog(
            this,
            "Please select a room to edit."
        );
        return;
    }

    int roomNumber = (int) tableModel.getValueAt(selectedRow, 0);

    int index = tracker.searchRoom(roomNumber);

    if (index == -1) {
        return;
    }

    RoomReservationRecorder.Room room =
        tracker.getRoomAt(index);

    javax.swing.JTextField numberField =
        new javax.swing.JTextField(
            String.valueOf(room.roomNumber)
        );

    String[] types = {
        "Single",
        "Double",
        "Triple",
        "Suites",
        "Deluxe",
        "VIP"
    };

    javax.swing.JComboBox<String> typeBox =
        new javax.swing.JComboBox<>(types);

    typeBox.setSelectedItem(room.roomType);

    // Set price based on current room type
    javax.swing.JTextField priceField =
        new javax.swing.JTextField(
            String.valueOf(getRoomPrice(room.roomType))
        );

    // Automatically change price when room type changes
    typeBox.addActionListener(e -> {

        String selectedType =
            (String) typeBox.getSelectedItem();

        priceField.setText(
            String.valueOf(getRoomPrice(selectedType))
        );
    });

    javax.swing.JPanel panel = new javax.swing.JPanel(
        new java.awt.GridLayout(0, 2, 5, 5)
    );

    panel.add(new javax.swing.JLabel("Room Number:"));
    panel.add(numberField);

    panel.add(new javax.swing.JLabel("Room Type:"));
    panel.add(typeBox);

    panel.add(new javax.swing.JLabel("Room Price:"));
    panel.add(priceField);

    int choice = javax.swing.JOptionPane.showConfirmDialog(
        this,
        panel,
        "Edit Room " + roomNumber,
        javax.swing.JOptionPane.OK_CANCEL_OPTION
    );

    if (choice == javax.swing.JOptionPane.OK_OPTION) {

        try {

            int newNumber =
                Integer.parseInt(
                    numberField.getText().trim()
                );

            double newPrice =
                Double.parseDouble(
                    priceField.getText().trim()
                );

            String newType =
                (String) typeBox.getSelectedItem();

            if (newNumber <= 0 || newPrice <= 0) {

                javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Room number and price must be greater than zero."
                );

                return;
            }

            if (tracker.updateRoom(
                roomNumber,
                newNumber,
                newType,
                newPrice
            )) {

                refreshTable();

                javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Room updated successfully!"
                );

            } else {

                javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Update failed. Check the room number or duplicate records."
                );
            }

        } catch (NumberFormatException e) {

            javax.swing.JOptionPane.showMessageDialog(
                this,
                "Please enter valid numbers."
            );
        }
    }
    } 
  
    // DELETE SELECTED ROOM
    private void deleteSelectedRoom() {

    int selectedRow = jTable1.getSelectedRow();

    if (selectedRow == -1) {
        javax.swing.JOptionPane.showMessageDialog(
            this, "Please select a room to delete."
        );
        return;
    }

    int roomNumber = (int) tableModel.getValueAt(selectedRow, 0);

    int confirm = javax.swing.JOptionPane.showConfirmDialog(
        this,
        "Are you sure you want to delete Room " + roomNumber + "?",
        "Confirm Delete",
        javax.swing.JOptionPane.YES_NO_OPTION,
        javax.swing.JOptionPane.WARNING_MESSAGE
    );

    if (confirm == javax.swing.JOptionPane.YES_OPTION) {

        if (tracker.deleteRoom(roomNumber)) {

            refreshTable();

            javax.swing.JOptionPane.showMessageDialog(
                this, "Room deleted successfully!"
            );

        } else {

            javax.swing.JOptionPane.showMessageDialog(
                this,
                "Cannot delete this room.\n"
                + "It may be occupied or have a pending booking."
            );
        }
    }
    }

   
   public void refreshTable() {

    tableModel.setRowCount(0);

    java.util.ArrayList<RoomReservationRecorder.Room> sortedRooms =
        new java.util.ArrayList<>();

    // GET SELECTED FILTERS
    String selectedType =
        (String) jComboBox2.getSelectedItem();

    String selectedStatus =
        (String) jComboBox1.getSelectedItem();

    // CHECK EVERY ROOM
    for (int i = 0; i < tracker.getSize(); i++) {

        RoomReservationRecorder.Room room =
            tracker.getRoomAt(i);

        boolean typeMatch =
            selectedType.equals("All Room Types") ||
            room.roomType.equalsIgnoreCase(selectedType);

        boolean statusMatch =
            selectedStatus.equals("All Status") ||
            room.status.equalsIgnoreCase(selectedStatus);

        // ADD ONLY MATCHING ROOMS
        if (typeMatch && statusMatch) {
            sortedRooms.add(room);
        }
    }

    // SORT ROOM NUMBERS
    sortedRooms.sort(
        java.util.Comparator.comparingInt(
            room -> room.roomNumber
        )
    );

    // DISPLAY FILTERED ROOMS
    for (RoomReservationRecorder.Room room : sortedRooms) {

        tableModel.addRow(new Object[]{
            room.roomNumber,
            room.roomType,
            "PHP " + room.price,
            room.status,
            room.guestName
        });
    }

    // REFRESH BOOKING RECORDS
    refreshBookingTable();

    // DISPLAY FILTERED TOTAL
    jLabel3.setText(
        "Showing: " + sortedRooms.size()
        + " / " + tracker.getSize() + " Rooms"
    );
    }
   
    public void refreshBookingTable() {

    bookingTableModel.setRowCount(0);

    for (int i = 0; i < tracker.getQueueCount(); i++) {

        RoomReservationRecorder.Booking booking =
            tracker.getBookingAt(i);

        if (booking != null) {

            bookingTableModel.addRow(new Object[]{
                i + 1,
                booking.roomNumber,
                booking.guestName
            });
        }
    }
    
    
    
    // DISPLAY TOTAL BOOKING RECORDS
    jLabel7.setText(
        "Total Booking Records: " + tracker.getQueueCount()
    );
    }
    
    // HIGHLIGHT SEARCHED ROOM WITHOUT HIDING OTHER RECORDS
    public void highlightRoom(int index) {

    refreshTable();

    if (index < 0 || index >= tracker.getSize()) {
        return;
    }

    RoomReservationRecorder.Room searchedRoom =
        tracker.getRoomAt(index);

    for (int i = 0; i < jTable1.getRowCount(); i++) {

        int roomNumber = (int) jTable1.getValueAt(i, 0);

        if (roomNumber == searchedRoom.roomNumber) {

            jTable1.setRowSelectionInterval(i, i);

            jTable1.scrollRectToVisible(
                jTable1.getCellRect(i, 0, true)
            );

            break;
        }
    }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jButton4 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("RECORDS TABLE");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 598, -1));

        jPanel3.setBackground(new java.awt.Color(153, 0, 153));
        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("?");

        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("?");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(8, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addContainerGap())
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 687, 598, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Search By Room Number:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(32, 53, 152, -1));

        jTextField1.setBackground(new java.awt.Color(255, 255, 255));
        jTextField1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 52, 71, -1));

        jButton1.setBackground(new java.awt.Color(153, 0, 153));
        jButton1.setForeground(new java.awt.Color(0, 0, 0));
        jButton1.setText("Search");
        jButton1.addActionListener(this::jButton1ActionPerformed);
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 50, -1, -1));

        jButton2.setBackground(new java.awt.Color(0, 0, 0));
        jButton2.setForeground(new java.awt.Color(0, 255, 0));
        jButton2.setText("EDIT ROOM");
        jButton2.addActionListener(this::jButton2ActionPerformed);
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 50, -1, -1));

        jButton3.setBackground(new java.awt.Color(0, 0, 0));
        jButton3.setForeground(new java.awt.Color(255, 0, 0));
        jButton3.setText("DELETE ROOM");
        jButton3.addActionListener(this::jButton3ActionPerformed);
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 50, 120, -1));

        jComboBox1.setBackground(new java.awt.Color(255, 0, 255));
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel1.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 80, -1, -1));

        jComboBox2.setBackground(new java.awt.Color(255, 0, 255));
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel1.add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 80, -1, -1));

        jButton4.setBackground(new java.awt.Color(0, 0, 0));
        jButton4.setFont(new java.awt.Font("Segoe UI", 0, 8)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Reset");
        jPanel1.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 80, 50, 20));

        jScrollPane1.setBackground(new java.awt.Color(255, 0, 204));
        jScrollPane1.setForeground(new java.awt.Color(0, 0, 0));

        jTable1.setBackground(new java.awt.Color(255, 51, 255));
        jTable1.setForeground(new java.awt.Color(0, 0, 0));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable1.setSelectionBackground(new java.awt.Color(153, 0, 153));
        jTable1.setSelectionForeground(new java.awt.Color(0, 0, 0));
        jScrollPane1.setViewportView(jTable1);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 108, 586, 291));

        jScrollPane2.setBackground(new java.awt.Color(255, 0, 204));
        jScrollPane2.setForeground(new java.awt.Color(0, 0, 0));

        jTable2.setAutoCreateRowSorter(true);
        jTable2.setBackground(new java.awt.Color(255, 51, 255));
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable2.setSelectionBackground(new java.awt.Color(153, 0, 153));
        jTable2.setSelectionForeground(new java.awt.Color(0, 0, 0));
        jScrollPane2.setViewportView(jTable2);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 436, 586, 239));

        jLabel5.setBackground(new java.awt.Color(153, 0, 153));
        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(153, 0, 153));
        jLabel5.setText("ROOM RECORDS");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(153, 0, 153));
        jLabel6.setText("BOOKING RECORDS");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 410, -1, -1));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/roomreservationrecorder/tab.jpg"))); // NOI18N
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(-2, -4, 600, 750));

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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

    String input = jTextField1.getText().trim();

    if (input.isEmpty()) {
        javax.swing.JOptionPane.showMessageDialog(
            this,
            "Please enter a room number."
        );
        return;
    }

    try {

        int roomNumber = Integer.parseInt(input);

        // MANUAL LINEAR SEARCH
        int index = tracker.searchRoom(roomNumber);

        if (index == -1) {

            jLabel3.setText(
                "Room " + roomNumber + " NOT FOUND!"
            );

            javax.swing.JOptionPane.showMessageDialog(
                this,
                "Room " + roomNumber + " was not found."
            );

        } else {

            // Restore all records
            refreshTable();

            // Select the matching record
            jTable1.setRowSelectionInterval(index, index);

            jTable1.scrollRectToVisible(
                jTable1.getCellRect(index, 0, true)
            );

           jLabel3.setText("Total Records: " + tracker.getSize());
        }

    } catch (NumberFormatException e) {

        javax.swing.JOptionPane.showMessageDialog(
            this,
            "Room number must contain numbers only."
        );
    }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
         editSelectedRoom();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        deleteSelectedRoom();
    }//GEN-LAST:event_jButton3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        //</editor-fold>

        /* Create and display the form */
     java.awt.EventQueue.invokeLater(() -> {
    RoomReservationRecorder tracker = new RoomReservationRecorder();
    RECORDTAB records = new RECORDTAB(tracker);
    records.setVisible(true);
    });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
