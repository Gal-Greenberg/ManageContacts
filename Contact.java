import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.Scanner;

public class Contact implements Cloneable, IContact, Serializable {
	
	private static final long serialVersionUID = 1L;
	final static int SIZE_STRING = 32;
	final static int SIZE_INT = 4;
	final static int MEMBERS_NUM = 3;
	final static int RECORD_SIZE = SIZE_STRING*MEMBERS_NUM + SIZE_INT;
	private final static int START_ID = 1;
	private static int idContact = START_ID;
	
	private int id;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	
	public Contact() {
		this.firstName = new String("");
		this.lastName = new String("");
		this.phoneNumber = new String("");
	}
	
	public Contact(String firstName, String lastName, String phoneNumber) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.id = idContact++;
	}
	
	@Override
	protected Contact clone() throws CloneNotSupportedException {
		return (Contact)super.clone();
	}
	
	public void copyFrom(Contact that) {
		this.id = that.id;
		this.firstName = that.firstName;
		this.lastName = that.lastName;
		this.phoneNumber = that.phoneNumber;
	}

	@Override
	public void writeObject(RandomAccessFile raf) throws IOException {
		raf.writeInt(id);
		FixedLengthStringIO.writeFixedLengthString(firstName, SIZE_STRING, raf);
		FixedLengthStringIO.writeFixedLengthString(lastName, SIZE_STRING, raf);
		FixedLengthStringIO.writeFixedLengthString(phoneNumber, SIZE_STRING, raf);
	}
	
	public void writeObjectToTxt(File file) throws IOException {
		PrintWriter pw = new PrintWriter(file);
		pw.print(id + " " + firstName + " " + lastName + " " + phoneNumber);
		pw.close();
	}
	
	public void writeObjectToByte(File file) throws IOException {
		DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(file));
		dataOutputStream.writeInt(id);
		dataOutputStream.writeUTF(firstName);
		dataOutputStream.writeUTF(lastName);
		dataOutputStream.writeUTF(phoneNumber);
		dataOutputStream.close();
	}

	@Override
	public void export(String format, File file) throws IOException {
		switch (format) {
		case "txt":
			writeObjectToTxt(file);
			break;
		case "obj.dat":
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
			objectOutputStream.writeObject(this);
			objectOutputStream.close();
			break;
		case "byte.dat":
			writeObjectToByte(file);
			break;
		}
	}
	
	@Override
	public void readObject(long position, RandomAccessFile raf) throws IOException {
		raf.seek(position);
		setId(raf.readInt());
		setFirstName(FixedLengthStringIO.readFixedLengthString(Contact.SIZE_STRING, raf).trim());
		setLastName(FixedLengthStringIO.readFixedLengthString(Contact.SIZE_STRING, raf).trim());
		setPhoneNumber(FixedLengthStringIO.readFixedLengthString(Contact.SIZE_STRING, raf).trim());
	}
	
	public void readObjectFromTxt(File file) throws IOException {
		Scanner s = new Scanner(file);
		setId(s.nextInt());
		setFirstName(s.next());
		setLastName(s.next());
		setPhoneNumber(s.next());
		s.close();
	}
	
	public void readObjectFromByte(File file) throws IOException {
		DataInputStream dataInputStream = new DataInputStream(new FileInputStream(file));
		setId(dataInputStream.readInt());
		setFirstName(dataInputStream.readUTF());
		setLastName(dataInputStream.readUTF());
		setPhoneNumber(dataInputStream.readUTF());
		dataInputStream.close();
	}
	
	@Override
	public void loadFile(String format, File file) throws IOException, ClassNotFoundException {
		switch (format) {
		case "txt": 
			readObjectFromTxt(file);
			break;
		case "obj.dat":
			ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
			copyFrom((Contact)objectInputStream.readObject());
			objectInputStream.close();
			break;
		case "byte.dat":
			readObjectFromByte(file);
			break;
		}
	}

	@Override
	public String[] getUiData() {
		String[] arrString = {"" + id, firstName, lastName, phoneNumber};
		return arrString;
	}

	@Override
	public int getObjectSize() {
		return 2 * Contact.RECORD_SIZE - Contact.SIZE_INT;
	}
	
	public int getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public static void setIdContact(int idContact) {
		Contact.idContact = idContact;
	}
	
}
