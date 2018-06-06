import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.Set;
import javax.swing.JOptionPane;

public class ContactsManager {
	
	private ArrayList<IActionListener> listeners = new ArrayList<IActionListener>();
	
	private FileListIterator<IContact> iterator;
	private IContact current = new Contact();
	
	public ContactsManager(String fileName) throws IOException {
		this.iterator = new FileListIterator<>(fileName);
		if (iterator.hasNext()) {
			current = iterator.next();
		}
		lastContact();
		Contact.setIdContact(Integer.parseInt(current.getUiData()[0]) + 1);
	}
	
	public void proccessEvent(String command, IContact contact) {
		for (IActionListener listener : listeners) {
			listener.performedFromModel(new ActionEvent(this, -1, command), current);
		}
	}

	public void registerListener(IActionListener listener) {
		this.listeners.add(listener);
	}
	
	public void addContact(String firstName, String lastName, String phoneNumber) {
		Contact con = new Contact(firstName, lastName, phoneNumber);
		iterator.add(con);
		current = iterator.next();
		if (!iterator.hasPrevious()) {
			proccessEvent(IActionListener.CREATE, current);
			return;
		}
		current = iterator.previous();
		proccessEvent(IActionListener.CREATE, current);
	}
	
	public void updateContact(String firstName, String lastName, String phoneNumber) {
		Contact con = new Contact(firstName, lastName, phoneNumber);
		iterator.set(con);
		current = con;
		proccessEvent(IActionListener.UPDATE, current);
	}
	
	public void previousContact() {
		if (!iterator.hasPrevious() && !iterator.hasNext()) {
			System.out.println("No contacts");
			proccessEvent(IActionListener.PREVIOUS, current);
			return;
		}
		if (!iterator.hasPrevious()) {
			System.out.println("This is the first contact");
			current = iterator.next();
		}
		current = iterator.previous();
		proccessEvent(IActionListener.PREVIOUS, current);
	}
	
	public IContact firstContact() {
		if (!iterator.hasPrevious() && !iterator.hasNext()) {
			System.out.println("No contacts");
			proccessEvent(IActionListener.FIRST, current);
			return current;
		}
		while (iterator.hasPrevious()) {
			current = iterator.previous();
		}
		proccessEvent(IActionListener.FIRST, current);
		return current;
	}
	
	public void nextContact() {
		if (!iterator.hasPrevious() && !iterator.hasNext()) {
			System.out.println("No contacts");
			proccessEvent(IActionListener.NEXT, current);
			return;
		}
		if (!iterator.hasNext()) {
			System.out.println("This is the last contact");
			current = iterator.previous();
		}
		current = iterator.next();
		proccessEvent(IActionListener.NEXT, current);
	}
	
	public void lastContact() {
		if (!iterator.hasPrevious() && !iterator.hasNext()) {
			System.out.println("No contacts");
			proccessEvent(IActionListener.LAST, current);
			return;
		}
		while (iterator.hasNext()) {
			current = iterator.next();
		}
		proccessEvent(IActionListener.LAST, current);
	}
	
	public void export(String format) {
		try {
			if (!iterator.hasPrevious() && !iterator.hasNext()) {
				System.out.println("No contacts");
				return;
			}
			current = iterator.next();
			String nameFile = Integer.toString(((Contact)current).getId()) + "." + format;
			File file = new File(nameFile);
			current.export(format, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadFile(String format, String fileName) {
		Contact con = new Contact();
		String fullFileName = fileName + "." + format;
		File file = new File(fullFileName);
		try {
			if (!file.exists()) {
				throw new Exception("the file not exists");
			}
			con.loadFile(format, file);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
		proccessEvent(IActionListener.LOAD_FILE, con);
	}
	
	public void sortField(String sortBy, String sortOrder) {
		
		Comparator<String> comparator = new Comparator<String>() {
			
			@Override
			public int compare(String o1, String o2) {
				int order = 0;
				switch (sortOrder) {
				case "asc":
					order = 1;
					break;
				case "desc":
					order = -1;
					break;
				}
				int lengthDiff = o1.length() - o2.length();
				int compareStringRes = o1.compareTo(o2);
				if (lengthDiff == 0) {
					return compareStringRes * order;
				}
				return lengthDiff * order;
			}
		};
		
		String key = null;
		Map<String, IContact> map = new TreeMap<String, IContact>(comparator);
		IContact con = firstContact();
		while (con != null) {
			switch (sortBy) {
			case "FIRST_NAME_FIELD":
				key = con.getUiData()[1];
				break;
			case "LAST_NAME_FIELD":
				key = con.getUiData()[2];
				break;
			case "PHONE_NUMBER_FIELD":
				key = con.getUiData()[3];
				break;
			}
			map.put(key, con);
			if (iterator.hasNext()) {
				con = iterator.next();
			} else {
				con = null;
			}
		}
		firstContact();
		while (iterator.nextIndex() > 0) {
			iterator.remove();
		}
		for (Entry<String, IContact> entry : map.entrySet()) {
			iterator.add(entry.getValue());
		}
		current = iterator.next();
	}
	
	public void sortCount(String sortBy, String sortOrder) {
		String key = null;
		Map<String, Vector<IContact>> map = new HashMap<String, Vector<IContact>>();
		IContact con = firstContact();
		while (con != null) {
			switch (sortBy) {
			case "FIRST_NAME_FIELD":
				key = con.getUiData()[1];
				break;
			case "LAST_NAME_FIELD":
				key = con.getUiData()[2];
				break;
			case "PHONE_NUMBER_FIELD":
				key = con.getUiData()[3];
				break;
			}
			if (!map.containsKey(key)) {
				map.put(key, new Vector<IContact>());
			}
			map.get(key).add(con);
			if (iterator.hasNext()) {
				con = iterator.next();
			} else {
				con = null;
			}
		}
		firstContact();
		while (iterator.nextIndex() > 0) {
			iterator.remove();
		}
		
		Comparator<Entry<String, Vector<IContact>>> comparator = new Comparator<Entry<String, Vector<IContact>>>() {
			
			@Override
			public int compare(Entry<String, Vector<IContact>> o1, Entry<String, Vector<IContact>> o2) {
				int order = 0;
				switch (sortOrder) {
				case "asc":
					order = 1;
					break;
				case "desc":
					order = -1;
					break;
				}
				return (o1.getValue().size() - o2.getValue().size()) * order;
			}
		};
		
		Set<Entry<String, Vector<IContact>>> set = new TreeSet<Entry<String, Vector<IContact>>>(comparator);
		for (Entry<String, Vector<IContact>> entry : map.entrySet()) {
			set.add(entry);
		}
		for (Entry<String, Vector<IContact>> entry : set) {
			iterator.add(entry.getValue().get(0));
		}
		current = iterator.next();
	}
	
	public void reverse() {
		Stack<IContact> stack = new Stack<IContact>();
		IContact con = firstContact();
		while (con != null) {
			stack.push(con);
			if (iterator.hasNext()) {
				con = iterator.next();
			} else {
				con = null;
			}
		}
		firstContact();
		while (iterator.nextIndex() > 0) {
			iterator.remove();
		}
		while (!stack.empty()) {
			iterator.add(stack.pop());
		}
		current = iterator.next();
	}

	public IContact getCurrent() {
		return current;
	}

}
