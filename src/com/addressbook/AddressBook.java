package com.addressbook;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import com.addressbook.io.FixedLengthStringIO;

public class AddressBook extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// specify the fields saved
	final static int NAME_SIZE = 32;
	final static int STREET_SIZE = 32;
	final static int CITY_SIZE = 20;
	final static int STATE_SIZE = 2;
	final static int ZIP_SIZE = 5;
	final static int RECORD_SIZE = 
			NAME_SIZE + STREET_SIZE + CITY_SIZE + STATE_SIZE + ZIP_SIZE;
	
	// random access file
	private RandomAccessFile raf;
	
	private JTextField name = new JTextField(NAME_SIZE);
	private JTextField street = new JTextField(STREET_SIZE);
	private JTextField city = new JTextField(CITY_SIZE);
	private JTextField state = new JTextField(STATE_SIZE);
	private JTextField zip = new JTextField(ZIP_SIZE);
	
	private JButton add = new JButton("Add");
	private JButton first = new JButton("First");
	private JButton next = new JButton("Next");
	private JButton previous = new JButton("Previous");
	private JButton last = new JButton("Last");
	
	// constructor
	public AddressBook() {
		// open address data
		try {
			raf = new RandomAccessFile("address.dat", "rw");
		} catch (IOException ioexc) {
			JOptionPane.showMessageDialog(null, ioexc.getMessage());
			System.exit(0);
		}
		
		// add panels
		JPanel p1 = new JPanel();
		p1.setLayout(new GridLayout(3,1));
		p1.add(new JLabel("Name"));
		p1.add(new JLabel("Street"));
		p1.add(new JLabel("City"));
		
		// panel for state
		JPanel jpState = new JPanel();
		jpState.setLayout(new BorderLayout());
		jpState.add(new JLabel("State"), BorderLayout.WEST);
		jpState.add(state, BorderLayout.CENTER);
		
		// panel for zip
		JPanel jpZip = new JPanel();
		jpZip.setLayout(new BorderLayout());
		jpZip.add(new JLabel("Zip"), BorderLayout.WEST);
		jpZip.add(zip, BorderLayout.CENTER);
		
		// panel for state and zip
		JPanel p2 = new JPanel();
		p2.setLayout(new BorderLayout());
		p2.add(jpState, BorderLayout.WEST);
		p2.add(jpZip, BorderLayout.CENTER);
		
		// panel for holding p2 and city
		JPanel p3 = new JPanel();
		p3.setLayout(new BorderLayout());
		p3.add(city, BorderLayout.CENTER);
		p3.add(p2, BorderLayout.EAST);
		
		// panel for holding name, street, and p3
		JPanel p4 = new JPanel();
		p4.setLayout(new GridLayout(3, 1));
		p4.add(name);
		p4.add(street);
		p4.add(p3);
		
		// panel for p1 and p4
		JPanel jpAddress = new JPanel(new BorderLayout());
		jpAddress.add(p1, BorderLayout.WEST);
		jpAddress.add(p4, BorderLayout.CENTER);
		
		// set the panel with a line border
		jpAddress.setBorder(new BevelBorder(BevelBorder.RAISED));
		
		// add buttons to panel
		JPanel jpButton = new JPanel();
		jpButton.add(add);
		jpButton.add(first);
		jpButton.add(next);
		jpButton.add(previous);
		jpButton.add(last);
		
		// add jpAddress and jpButton to the frame (since your extending the JFrame class)
		add(jpAddress, BorderLayout.CENTER);
		add(jpButton, BorderLayout.SOUTH);
		
		/* ACTION LISTENERS */
		
		// add button
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				writeAddress();
			}
		});
		// first button
		first.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (raf.length() > 0) {
						readAddress(0);
					}
				} catch (IOException ioe) {
					JOptionPane.showMessageDialog(null, ioe.getMessage());
				}
			}
		});
		// next button
		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					long currentPos = raf.getFilePointer();
					if (currentPos < raf.length()) {
						readAddress(currentPos);
					}
				} catch (IOException ioe) {
					JOptionPane.showMessageDialog(null, ioe.getMessage());
				}
			}
		});
		// previous button
		previous.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					long currentPos = raf.getFilePointer();
					if (currentPos - 2 * RECORD_SIZE > 0) {
						readAddress(currentPos - 2 * 2 * RECORD_SIZE);
					} else {
						readAddress(0);
					}
				} catch (IOException ioe) {
					JOptionPane.showMessageDialog(null, ioe.getMessage());
				}
			}
		});
		// last button
		last.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					long lastPos = raf.length();
					if (lastPos > 0) {
						readAddress(lastPos - 2 * RECORD_SIZE);
					}
				} catch (IOException ioe) {
					JOptionPane.showMessageDialog(null,  ioe.getMessage());
				}
			}
		});
		
		// display the first record if it exists
		try {
			if (raf.length() > 0) {
				readAddress(0);
			}
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(null, ioe.getMessage());
		}
		
	}
	
	// save a record
	public void writeAddress() {
		try {
			raf.seek(raf.length());
			FixedLengthStringIO.writeFixedLengthString(name.getText(),
					NAME_SIZE,
					raf);
			FixedLengthStringIO.writeFixedLengthString(street.getText(),
					STREET_SIZE,
					raf);
			FixedLengthStringIO.writeFixedLengthString(city.getText(),
					CITY_SIZE,
					raf);
			FixedLengthStringIO.writeFixedLengthString(state.getText(),
					STATE_SIZE,
					raf);
			FixedLengthStringIO.writeFixedLengthString(zip.getText(),
					ZIP_SIZE,
					raf);
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(null, ioe.getMessage());
		}
	}
	
	// read a record
	public void readAddress(long position) {
		try {
			raf.seek(position);
			String nameText = FixedLengthStringIO.readFixedLengthString(
					NAME_SIZE, raf);
			String streetText = FixedLengthStringIO.readFixedLengthString(
					STREET_SIZE, raf);
			String cityText = FixedLengthStringIO.readFixedLengthString(
					CITY_SIZE, raf);
			String stateText = FixedLengthStringIO.readFixedLengthString(
					STATE_SIZE, raf);
			String zipText = FixedLengthStringIO.readFixedLengthString(
					ZIP_SIZE, raf);
		
			
			name.setText(nameText);
			street.setText(streetText);
			city.setText(cityText);
			state.setText(stateText);
			zip.setText(zipText);
			
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(null, ioe.getMessage());
		}
	}
	
	/* MAIN METHOD */
	public static void main(String[] args) {
		AddressBook frame = new AddressBook();
		frame.pack();
		frame.setTitle("Address Book");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

}
