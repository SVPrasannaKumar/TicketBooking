package com.movie.ticketbooking;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class TicketBooking {

    Connection connection;
    Statement statement;
    ResultSet resultSet;
    JFrame frame;
    String seatsAvailable[];

    public TicketBooking() throws SQLException {
        frame = new JFrame("Ticket Booking");
        final JLabel titleLabel = new JLabel();
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setSize(350, 100);
        JButton theatreButton = new JButton("Show Threatres");
        theatreButton.setBounds(200, 100, 150, 20);
        String[] movieNames = getConnection();
        final JComboBox movieComboBox = new JComboBox(movieNames);
        movieComboBox.setBounds(50, 100, 120, 20);
        final JComboBox theatreComboBox = new JComboBox();
        theatreComboBox.setBounds(50, 150, 100, 20);
        titleLabel.setText("SELECT MOVIE TO DISPLAY THEATRES");
        JButton timeButton = new JButton("Show Timings");
        timeButton.setBounds(200, 150, 120, 20);
        final JComboBox timeComboBox = new JComboBox();
        timeComboBox.setBounds(50, 200, 100, 20);
        timeComboBox.setVisible(false);
        JButton confirmButton = new JButton("Confirm");
        confirmButton.setBounds(200, 200, 90, 20);
        confirmButton.setVisible(false);
        JButton bookButton = new JButton("Book");
        bookButton.setBounds(200, 250, 90, 20);
        bookButton.setVisible(false);
        JLabel mobileLabel = new JLabel("Mobile Number");
        mobileLabel.setBounds(50, 280, 120, 20);
        mobileLabel.setVisible(false);
        frame.add(mobileLabel);
        JTextField mobileTextField = new JTextField();
        mobileTextField.setBounds(50, 300, 120, 20);
        mobileTextField.setVisible(false);
        JLabel seatLabel = new JLabel("Number of seats");
        seatLabel.setBounds(50, 230, 120, 20);
        seatLabel.setVisible(false);
        frame.add(seatLabel);
        JTextField seatTextField = new JTextField();
        seatTextField.setBounds(50, 250, 120, 20);
        seatTextField.setVisible(false);
        frame.add(seatTextField);
        frame.add(mobileTextField);
        frame.add(confirmButton);
        frame.add(bookButton);
        frame.add(timeComboBox);
        frame.add(movieComboBox);
        frame.add(titleLabel);
        frame.add(theatreButton);
        frame.setLayout(null);
        frame.setSize(512, 490);
        frame.setVisible(true);
        theatreComboBox.setVisible(false);
        frame.add(theatreComboBox);
        timeButton.setVisible(false);
        frame.add(timeButton);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        theatreButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {

                try {
                    getTheatre(movieComboBox.getItemAt(movieComboBox.getSelectedIndex()).toString());
                } catch (SQLException ex) {
                    Logger.getLogger(TicketBooking.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            private void getTheatre(String name) throws SQLException {
                String s[] = new String[11];
                String host = "jdbc:derby://localhost:1527/ticket";
                String uName = "tick";
                String uPass = "tick";
                connection = DriverManager.getConnection(host, uName, uPass);
                statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                String SQL = "select NAME from movie A JOIN theatre B on A.MOVIE_ID=B.MOVIE_ID where A.MOVIE_NAME='" + name + "'";
                resultSet = statement.executeQuery(SQL);
                int i = 0;
                while (resultSet.next()) {
                    s[i++] = resultSet.getString("NAME");
                }
                connection.close();
                DefaultComboBoxModel model = new DefaultComboBoxModel(s);
                theatreComboBox.setModel(model);
                theatreComboBox.setVisible(true);
            }

        });
        theatreComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeButton.setVisible(true);

            }
        });
        timeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    bookShow(movieComboBox.getItemAt(movieComboBox.getSelectedIndex()).toString());
                } catch (SQLException ex) {
                    Logger.getLogger(TicketBooking.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            private void bookShow(String name) throws SQLException {
                String s[] = new String[11];
                String host = "jdbc:derby://localhost:1527/ticket";
                String uName = "tick";
                String uPass = "tick";
                connection = DriverManager.getConnection(host, uName, uPass);
                statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                String SQL = "select SHOWTIME from movie A JOIN theatre B on A.MOVIE_ID=B.MOVIE_ID where A.MOVIE_NAME='" + name.trim() + "'";
                resultSet = statement.executeQuery(SQL);
                int i = 0;
                while (resultSet.next()) {
                    String time = resultSet.getString("SHOWTIME");
                    String times[] = time.split(",");
                    for (String t : times) {
                        s[i++] = t;
                    }
                }
                connection.close();
                DefaultComboBoxModel model = new DefaultComboBoxModel(s);
                timeComboBox.setModel(model);
                timeComboBox.setVisible(true);
                seatLabel.setVisible(true);
                seatTextField.setVisible(true);
                confirmButton.setVisible(true);
            }
        });
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    StringBuilder sb = new StringBuilder();
                    String host = "jdbc:derby://localhost:1527/ticket";
                    String uName = "tick";
                    String uPass = "tick";
                    connection = DriverManager.getConnection(host, uName, uPass);
                    statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    String SQL = "select seat_no from seats where theatre_name='" + theatreComboBox.getItemAt(theatreComboBox.getSelectedIndex()).toString() + "' and showtime='" + timeComboBox.getItemAt(timeComboBox.getSelectedIndex()).toString() + "' and availability='YES' and movie_name='" + movieComboBox.getItemAt(movieComboBox.getSelectedIndex()).toString() + "'";
                    resultSet = statement.executeQuery(SQL);
                    while (resultSet.next()) {
                        sb.append(resultSet.getString("seat_no")).append(",");

                    }
                    connection.close();
                    seatsAvailable = null;
                    if (sb.length() != 0) {
                        seatsAvailable = sb.toString().split(",");
                        int seatno = Integer.parseInt(seatTextField.getText());
                        if (seatno <= 0) {
                            JOptionPane.showMessageDialog(frame, "Invalid seats", "Error", JOptionPane.ERROR_MESSAGE);
                        } else if (seatno <= seatsAvailable.length) {
                            mobileLabel.setVisible(true);
                            mobileTextField.setVisible(true);
                            bookButton.setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(frame, "Specified seats are not available", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "Specified seats are not available", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(TicketBooking.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int mobileNo = Integer.parseInt(mobileTextField.getText());
                    int seat = Integer.parseInt(seatTextField.getText());
                    String host = "jdbc:derby://localhost:1527/ticket";
                    String uName = "tick";
                    String uPass = "tick";
                    connection = DriverManager.getConnection(host, uName, uPass);
                    statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    String SQL = "INSERT INTO BOOKING_DETAILS VALUES('" + movieComboBox.getItemAt(movieComboBox.getSelectedIndex()).toString() + "','" + theatreComboBox.getItemAt(theatreComboBox.getSelectedIndex()).toString() + "','" + timeComboBox.getItemAt(timeComboBox.getSelectedIndex()).toString() + "'," + seat + "," + mobileNo + ")";
                    statement.executeUpdate(SQL);
                    String s = "";
                    for (int i = 0; i < seat; i++) {
                        String UpdateSeatSQL = "UPDATE SEATS SET AVAILABILITY='NO' WHERE SEAT_NO='" + seatsAvailable[i] + "' AND THEATRE_NAME='" + theatreComboBox.getItemAt(theatreComboBox.getSelectedIndex()).toString() + "' AND SHOWTIME='" + timeComboBox.getItemAt(timeComboBox.getSelectedIndex()).toString() + "' AND MOVIE_NAME='" + movieComboBox.getItemAt(movieComboBox.getSelectedIndex()).toString() + "'";
                        statement.executeUpdate(UpdateSeatSQL);
                        s += seatsAvailable[i] + " ";
                    }
                    connection.close();
                    JOptionPane.showMessageDialog(frame, "BOOKING SUCCESS.. SEATS ARE " + s, "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException ex) {
                    Logger.getLogger(TicketBooking.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }

    private String[] getConnection() throws SQLException {
        String s[] = new String[11];
        String host = "jdbc:derby://localhost:1527/ticket";
        String uName = "tick";
        String uPass = "tick";
        connection = DriverManager.getConnection(host, uName, uPass);
        statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        String SQL = "SELECT * FROM MOVIE";
        resultSet = statement.executeQuery(SQL);
        int i = 0;
        while (resultSet.next()) {
            s[i++] = resultSet.getString("MOVIE_NAME");
        }
        connection.close();
        return s;
    }

    public static void main(String[] args) {
        try {
            TicketBooking demo = new TicketBooking();
        } catch (SQLException ex) {
            Logger.getLogger(TicketBooking.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
