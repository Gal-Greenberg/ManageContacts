import java.util.ArrayList;
import javafx.scene.Scene;
import javafx.stage.Stage;

@SuppressWarnings("rawtypes")
public class ContactsManagerJFX implements IView {
	
	private ArrayList<IActionListener> listeners = new ArrayList<IActionListener>();
	private PaneJFX mainPanel;
	
	public ContactsManagerJFX(Stage primaryStage) {
		mainPanel = new PaneJFX(listeners);
		Scene scene = new Scene(mainPanel, 435, 430);
	    primaryStage.setTitle("Contacts manager jfx");
	    primaryStage.setScene(scene);
	    primaryStage.show();
	    primaryStage.setAlwaysOnTop(true);
	}

	@Override
	public void initView() {
		
	}

	@Override
	public void registerListener(IActionListener listener) {
		this.listeners.add(listener);
		if (mainPanel != null) {
			mainPanel.setListeners(listeners);
		}
	}

	@Override
	public PaneInterface getMainPanel() {
		return mainPanel;
	}

}
