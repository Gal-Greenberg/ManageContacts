import java.awt.event.ActionEvent;

public interface IActionListener {
	
	final static String CREATE = "Create";
	final static String UPDATE = "Update";
	final static String PREVIOUS = "<";
	final static String FIRST = "First";
	final static String NEXT = ">";
	final static String EXPORT = "Export";
	final static String LAST = "Last";
	final static String LOAD_FILE = "load file";
	final static String SORT = "SORT";
	final static String SHOW = "SHOW";

	void create(String firstName, String lastName, String phoneNumber);
	
	void update(String firstName, String lastName, String phoneNumber);
	
	void previous();
	
	void first();
	
	void next();
	
	void last();
	
	void export(String formatExport);
	
	void loadFile(String formatLoad, String fileName);
	
	void sort(String sort, String sortBy, String sortOrder);
	
	void show(String sortOrder);
	
	void changeCurrentContact(IContact contact, IView<?> view);
	
	void performedFromModel(ActionEvent event, IContact contact);

}
