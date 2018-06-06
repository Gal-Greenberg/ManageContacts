import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ListIterator;

public class FileListIterator<T extends IContact> implements ListIterator<T> {
	
	private long currentPosition;
	private RandomAccessFile raf;
	
	public FileListIterator(String fileName) throws IOException {
		raf = new RandomAccessFile(fileName, "rw");
		currentPosition = 0;
	}
	
	@SuppressWarnings("unchecked")
	private T initContactWorkaround() {
		return (T) new Contact();
	}

	@Override
	public void add(T con) {
		try {
			raf.seek(raf.length());
			con.writeObject(raf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean hasNext() {
		try {
			return currentPosition < raf.length();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean hasPrevious() {
		T con = initContactWorkaround();
		return currentPosition > con.getObjectSize();
	}

	@Override
	public T next() {
		if (!hasNext()) {
			throw new IndexOutOfBoundsException();
		}
		T con = initContactWorkaround();
		try {
			con.readObject(currentPosition, raf);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		currentPosition += con.getObjectSize();
		return con;
	}

	@Override
	public int nextIndex() {
		T con = initContactWorkaround();
		return (int)(currentPosition / con.getObjectSize());
	}

	@Override
	public T previous() {
		if (!hasPrevious()) {
			throw new IndexOutOfBoundsException();
		}
		T con = initContactWorkaround();
		currentPosition -= con.getObjectSize();
		long previousPosition = currentPosition - con.getObjectSize();
		try {
			con.readObject(previousPosition, raf);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return con;
	}

	@Override
	public int previousIndex() {
		T con = initContactWorkaround();
		return (int)(currentPosition / con.getObjectSize()) - 1;
	}

	@Override
	public void remove() {
		T con = initContactWorkaround();
		// i first thing read the one i want to remove
		long removePosition = currentPosition;
		try {
			while (raf.length() - con.getObjectSize() >= raf.getFilePointer()) {
				con.readObject(removePosition, raf);
				raf.seek(removePosition - con.getObjectSize());
				con.writeObject(raf);
				removePosition += con.getObjectSize();
				raf.seek(removePosition);
			}
			raf.setLength(raf.getFilePointer() - con.getObjectSize());
			if (currentPosition > raf.length()) {
				currentPosition = raf.length();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void set(T con) {
		try {
			raf.seek(currentPosition - con.getObjectSize());
			con.writeObject(raf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
