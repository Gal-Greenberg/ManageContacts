import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class Controller implements IActionListener {

	private ContactsManager model;
	private ArrayList<IView<?>> views;
	
	public Controller(ContactsManager manager) {
		this.model = manager;
		model.registerListener(this);
		this.views = new ArrayList<>();
	}
	
	public void addView(IView<?> frame) {
		this.views.add(frame);
		for (IView<?> view : views) {
			view.registerListener(this);
			view.initView();
		}
	}
	
	@Override
	public void create(String firstName, String lastName, String phoneNumber) {
		model.addContact(firstName, lastName, phoneNumber);
	}
	
	@Override
	public void update(String firstName, String lastName, String phoneNumber) {
		model.updateContact(firstName, lastName, phoneNumber);
	}
	
	@Override
	public void previous() {
		model.previousContact();
	}
	
	@Override
	public void first() {
		model.firstContact();
	}
	
	@Override
	public void next() {
		model.nextContact();
	}
	
	@Override
	public void last() {
		model.lastContact();
	}
	
	@Override
	public void export(String formatExport) {
		model.export(formatExport);
	}
	
	@Override
	public void loadFile(String formatLoad, String fileName) {
		model.loadFile(formatLoad, fileName);
	}
	
	@Override
	public void sort(String sort, String sortBy, String sortOrder) {
		switch (sort) {
		case "sort-field":
			model.sortField(sortBy, sortOrder);
			break;
		case "sort-count":
			model.sortCount(sortBy, sortOrder);
			break;
		case "reverse":
			model.reverse();
			break;
		}
		model.firstContact();
	}
	
	@Override
	public void show(String sortOrder) {
		switch (sortOrder) {
		case "asc":
			model.firstContact();
			break;
		case "desc":
			model.lastContact();
			break;
		}
		performedFromModel(new ActionEvent(this, -1, IActionListener.SHOW), model.getCurrent());
	}
	
	@Override
	public void changeCurrentContact(IContact contact, IView<?> view) {
		view.getMainPanel().setCurrentContact(contact);
		view.getMainPanel().viewContact();
		view.getMainPanel().fillTextFiled("", "", "");
	}
	
	@Override
	public void performedFromModel(ActionEvent event, IContact contact) {
		for (IView<?> view : views) {
			switch (event.getActionCommand()) {
			case IActionListener.CREATE:
			case IActionListener.PREVIOUS:
			case IActionListener.FIRST:
			case IActionListener.NEXT:
			case IActionListener.LAST:
				changeCurrentContact(contact, view);
				break;
			case IActionListener.UPDATE:
				changeCurrentContact(contact, view);
				view.getMainPanel().setUpdateButtonEnable(false);
				break;
			case IActionListener.LOAD_FILE:
				String[] read = {"", "", ""};
				if (contact != null)
					read = contact.getUiData();
				view.getMainPanel().fillTextFiled(read[1], read[2], read[3]);
				break;
			case IActionListener.SHOW:
				switch (view.getMainPanel().getShowOrder()) {
				case "asc":
					view.getMainPanel().setAscending(true);
					break;
				case "desc":
					view.getMainPanel().setAscending(false);
					break;
				}
				view.getMainPanel().startTicking();
				break;
			}
		}
	}

}
