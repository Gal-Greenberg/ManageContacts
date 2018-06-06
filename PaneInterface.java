import java.util.ArrayList;

import javafx.scene.paint.Color;

public interface PaneInterface {
	
	void startTicking();
	
	void viewContact();
	
	void fillTextFiled(String first, String last, String phone);
	
	void setListeners(ArrayList<IActionListener> listeners);
	
	String getTextFieldFirst();
	
	String getTextFieldLast();
	
	String getTextFieldPhone();
	
	String getTextFieldPath();
	
	String getSortOrder(); //
	
	String getShowOrder(); //
	
	IContact getCurrentContact();
	
	void setCurrentContact(IContact currentContact);
	
	void setUpdateButtonEnable(boolean bool);
	
	void setAscending(boolean isAscending);
	
	enum CurrentColor {
		
		START(Color.BLACK), CREATE_OR_UPDATE(Color.BLUE), NEXT_OR_LAST(Color.GREEN), PREV_OR_FIRST(Color.RED);

		private Color c;

		CurrentColor(Color color) {
			this.c = color;
		}

		public Color getCurrentColor() {
			return c;
		}

	}

}
