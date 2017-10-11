package com.addressbook.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import javax.swing.JOptionPane;

public class FixedLengthStringIO {
	
	public static String readFixedLengthString(int size, DataInput in) {
		char[] chars = new char[size];
		try {
			for (int i = 0; i < size; i++) {
				chars[i] = in.readChar();
			}
			
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(null, ioe.getMessage());
		}
		return new String(chars);
	}
	
	public static void writeFixedLengthString(String s, int size, DataOutput out) {
		char[] chars = new char[size];
		try {
			// fill an array with the characters
			s.getChars(0, Math.min(s.length(), size), chars, 0);
			// fill in blank spaces
			for (int i = Math.min(s.length(), size); i < chars.length; i++) {
				chars[i] = ' ';
			}
			// create a new string and pad with blank spaces
			out.writeChars(new String(chars));
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(null, ioe.getMessage());
		}
	}

}
