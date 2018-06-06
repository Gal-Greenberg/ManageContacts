import java.util.ArrayList;

import javax.swing.JFrame;

@SuppressWarnings("rawtypes")
public class ContactsManagerFrame extends JFrame implements IView {
	
	private static final long serialVersionUID = 1L;
	
	private ContactsManagerPanel mainPanel;
	private ArrayList<IActionListener> listeners = new ArrayList<IActionListener>();
	
	@Override
	public void initView() {
		setTitle("Contacts manager");
		mainPanel = new ContactsManagerPanel(listeners);
		add(mainPanel);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setAlwaysOnTop(true);
		setResizable(false);
		pack();
		for (IActionListener listener : listeners) {
			listener.first();
		}
		setVisible(true);
	}
	
	@Override
	public void registerListener(IActionListener listener) {
		this.listeners.add(listener);
		if (mainPanel != null)
			mainPanel.setListeners(listeners);
	}
	
	@Override
	public PaneInterface getMainPanel() {
		return mainPanel;
	}
	
}
