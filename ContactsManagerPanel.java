import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;

public class ContactsManagerPanel extends JPanel implements PaneInterface {
	
	private final static long serialVersionUID = 1L;
	
	private final static int WIDTH = 750;
	private final static int HEIGHT = 450;
	private final static int DRAW_OFFSET = 10;
	
	private ArrayList<IActionListener> listeners;
	
	private JTextField textFieldFirst;
	private JTextField textFieldLast;
	private JTextField textFieldPhone;
	private JButton createButton;
	private JButton updateButton;
	
	private IContact currentContact;
	
	private JButton previousButton;
	private JButton firstButton;
	private JButton editButton;
	private JButton nextButton;
	private JButton lastButton;
	private JLabel[] labels = new JLabel[Contact.MEMBERS_NUM];
	
	private JComboBox<String> fileTypes;
	private JButton exportButton;
	private JTextField textFieldPath;
	private JButton loadFile;
	
	private JComboBox<String> sort;
	private JComboBox<String> sortBy;
	private JComboBox<String> sortOrder;
	private JButton sortButton;
	private JComboBox<String> showOrder;
	private JButton showButton;
	
	private Timer timer;
	private boolean isAscending;
	
	public ContactsManagerPanel(ArrayList<IActionListener> listeners) {
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		SpringLayout theLayout = new SpringLayout();
		setLayout(theLayout);
		
		this.listeners = listeners;
		
		JPanel pNorth = north();
		JPanel pCenter = center();
		JPanel pSouth = south();
		JPanel pDownSouth = downSouth();
		
		add(pNorth);
		add(pCenter);
		add(pSouth);
		add(pDownSouth);
		
		theLayout.putConstraint(SpringLayout.WEST, pNorth, DRAW_OFFSET, SpringLayout.WEST, this);
		theLayout.putConstraint(SpringLayout.EAST, pNorth, -DRAW_OFFSET, SpringLayout.EAST, this);
		theLayout.putConstraint(SpringLayout.NORTH, pNorth, DRAW_OFFSET, SpringLayout.NORTH, this);
		
		theLayout.putConstraint(SpringLayout.WEST, pCenter, DRAW_OFFSET, SpringLayout.WEST, this);
		theLayout.putConstraint(SpringLayout.EAST, pCenter, -DRAW_OFFSET, SpringLayout.EAST, this);
		theLayout.putConstraint(SpringLayout.NORTH, pCenter, DRAW_OFFSET, SpringLayout.SOUTH, pNorth);
		
		theLayout.putConstraint(SpringLayout.WEST, pSouth, DRAW_OFFSET, SpringLayout.WEST, this);
		theLayout.putConstraint(SpringLayout.EAST, pSouth, -DRAW_OFFSET, SpringLayout.EAST, this);
		theLayout.putConstraint(SpringLayout.NORTH, pSouth, DRAW_OFFSET, SpringLayout.SOUTH, pCenter);
		
		theLayout.putConstraint(SpringLayout.WEST, pDownSouth, DRAW_OFFSET, SpringLayout.WEST, this);
		theLayout.putConstraint(SpringLayout.EAST, pDownSouth, -DRAW_OFFSET, SpringLayout.EAST, this);
		theLayout.putConstraint(SpringLayout.SOUTH, pDownSouth, -DRAW_OFFSET, SpringLayout.SOUTH, this);
		theLayout.putConstraint(SpringLayout.NORTH, pDownSouth, DRAW_OFFSET, SpringLayout.SOUTH, pSouth);
	}
	
	public JPanel north() {
		JPanel panel = new JPanel(new GridLayout(4, 2, 10, 5));
		
		JLabel first = new JLabel("First name:");
		textFieldFirst = new JTextField(Contact.SIZE_STRING);
		
		JLabel last = new JLabel("Last name:");
		textFieldLast = new JTextField(Contact.SIZE_STRING);
		
		JLabel phone = new JLabel("Phone number:");
		textFieldPhone = new JTextField(Contact.SIZE_STRING);
		
		createButton = new JButton(IActionListener.CREATE);
		createButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				for (IActionListener actionListener : listeners) {
					actionListener.create(textFieldFirst.getText(), textFieldLast.getText(), textFieldPhone.getText());
				}
			}
		});
		
		updateButton = new JButton(IActionListener.UPDATE);
		updateButton.setEnabled(false);
		updateButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				for (IActionListener actionListener : listeners) {
					actionListener.update(textFieldFirst.getText(), textFieldLast.getText(), textFieldPhone.getText());
				}
			}
		});
		
		panel.add(first);
		panel.add(textFieldFirst);
		panel.add(last);
		panel.add(textFieldLast);
		panel.add(phone);
		panel.add(textFieldPhone);
		panel.add(createButton);
		panel.add(updateButton);
		
		return panel;
	}
	
	public JPanel center() {
		JPanel panel = new JPanel(new GridLayout(0, 3, 10, 5));
		
		JPanel pLeft = leftCenter();
		JPanel pMiddle = middleCenter();
		JPanel pRight = rightCenter();
		
		panel.add(pLeft);
		panel.add(pMiddle);
		panel.add(pRight);
		
		return panel;
	}
	
	public JPanel leftCenter() {
		JPanel panel = new JPanel(new GridLayout(2, 0, 10, 5));
		
		previousButton = new JButton(IActionListener.PREVIOUS);
		previousButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				for (IActionListener actionListener : listeners) {
					actionListener.previous();
				}
			}
		});
		
		firstButton = new JButton(IActionListener.FIRST);
		firstButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				for (IActionListener actionListener : listeners) {
					actionListener.first();
				}
			}
		});
		
		panel.add(previousButton);
		panel.add(firstButton);
		
		return panel;
	}
	
	public JPanel middleCenter() {
		JPanel panel = new JPanel(new GridLayout(4, 2, 10, 5));
		
		JLabel first = new JLabel("First name:");
		JLabel last = new JLabel("Last name:");
		JLabel phone = new JLabel("Phone number:");
		
		for (int i = 0; i < Contact.MEMBERS_NUM; i++) {
			labels[i] = new JLabel();
		}
		
		editButton = new JButton("Edit contact");
		editButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] currentContactInfo = currentContact.getUiData();
				updateButton.setEnabled(true);
				fillTextFiled(currentContactInfo[1], currentContactInfo[2], currentContactInfo[3]);
			}
		});
		
		panel.add(first);
		panel.add(labels[0]);
		panel.add(last);
		panel.add(labels[1]);
		panel.add(phone);
		panel.add(labels[2]);
		panel.add(editButton);
		
		return panel;
	}
	
	public JPanel rightCenter() {
		JPanel panel = new JPanel(new GridLayout(2, 0, 10, 5));
		
		nextButton = new JButton(IActionListener.NEXT);
		nextButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				for (IActionListener actionListener : listeners) {
					actionListener.next();
				}
			}
		});
		
		lastButton = new JButton(IActionListener.LAST);
		lastButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				for (IActionListener actionListener : listeners) {
					actionListener.last();
				}
			}
		});
		
		panel.add(nextButton);
		panel.add(lastButton);
		
		return panel;
	}
	
	public JPanel south() {
		JPanel panel = new JPanel(new GridLayout(0, 3, 10, 5));
		
		exportButton = new JButton(IActionListener.EXPORT);
		exportButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				for (IActionListener actionListener : listeners) {
					actionListener.export(IView.FILE_TYPES[fileTypes.getSelectedIndex()]);
				}
			}
		});
		
		JPanel pLeft = leftSouth();
		JPanel pRight = rightSouth();
		
		panel.add(pLeft);
		panel.add(exportButton);
		panel.add(pRight);
		
		return panel;
	}
	
	public JPanel leftSouth() {
		JPanel panel = new JPanel(new GridLayout(3, 0, 10, 5));
		
		fileTypes = new JComboBox<String>(IView.FILE_TYPES);
		panel.add(new JLabel());
		panel.add(fileTypes);
		
		return panel;
	}
	
	public JPanel rightSouth() {
		JPanel panel = new JPanel(new GridLayout(3, 0, 10, 5));
		
		JLabel filePath = new JLabel("file path:");
		textFieldPath = new JTextField();
		
		loadFile = new JButton(IActionListener.LOAD_FILE);
		loadFile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				for (IActionListener actionListener : listeners) {
					actionListener.loadFile(IView.FILE_TYPES[fileTypes.getSelectedIndex()], textFieldPath.getText());
				}
			}
		});
		
		panel.add(filePath);
		panel.add(textFieldPath);
		panel.add(loadFile);
		
		return panel;
	}
	
	public JPanel downSouth() {
		JPanel panel = new JPanel(new GridLayout(2, 0, 10, 5));
		
		JPanel firstRow = new JPanel(new GridLayout(0, 4, 10, 5));
		JPanel secondRow = new JPanel(new GridLayout(0, 2, 10, 5));
		
		sort = new JComboBox<String>(IView.SORT);
		sortBy = new JComboBox<String>(IView.FIELD);
		sortOrder = new JComboBox<String>(IView.ORDER);
		
		sortButton = new JButton(IActionListener.SORT);
		sortButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				for (IActionListener actionListener : listeners) {
					actionListener.sort(IView.SORT[sort.getSelectedIndex()], IView.FIELD[sortBy.getSelectedIndex()],
							IView.ORDER[sortOrder.getSelectedIndex()]);
				}
			}
		});
		
		firstRow.add(sort);
		firstRow.add(sortBy);
		firstRow.add(sortOrder);
		firstRow.add(sortButton);
		
		showOrder = new JComboBox<String>(IView.ORDER);
		
		showButton = new JButton(IActionListener.SHOW);
		showButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				for (IActionListener actionListener : listeners) {
					actionListener.show(IView.ORDER[showOrder.getSelectedIndex()]);
				}
			}
		});
		
		secondRow.add(showOrder);
		secondRow.add(showButton);
		
		panel.add(firstRow);
		panel.add(secondRow);
		
		return panel;
	}
	
	@Override
	public void startTicking() {
		timer = new Timer();
		timer.schedule(new ArcsTimer(), 1000, 1000);
	}

	class ArcsTimer extends TimerTask {
		@Override
		public void run() {
			String prevContactId = currentContact.getUiData()[0];
			if (isAscending) {
				nextButton.doClick();
			} else {
				previousButton.doClick();
			}
			if (currentContact.getUiData()[0].equals(prevContactId)) {
				timer.cancel();
			}
			viewContact();
		}
	}
	
	@Override
	public void viewContact() {
		for (int i = 0; i < Contact.MEMBERS_NUM; i++) {
			labels[i].setText(currentContact.getUiData()[i + 1]);
		}
	}
	
	@Override
	public void fillTextFiled(String first, String last, String phone) {
		textFieldFirst.setText(first);
		textFieldLast.setText(last);
		textFieldPhone.setText(phone);
	}

	@Override
	public void setListeners(ArrayList<IActionListener> listeners) {
		this.listeners = listeners;
	}

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	@Override
	public String getTextFieldFirst() {
		return textFieldFirst.getText();
	}
	
	@Override
	public String getTextFieldLast() {
		return textFieldLast.getText();
	}

	@Override
	public String getTextFieldPhone() {
		return textFieldPhone.getText();
	}

	@Override
	public String getTextFieldPath() {
		return textFieldPath.getText();
	}

//	public JComboBox<String> getFileTypes() {
//		return fileTypes;
//	}
//
//	public JComboBox<String> getSort() {
//		return sort;
//	}
//
//	public JComboBox<String> getSortBy() {
//		return sortBy;
//	}

	@Override
	public String getSortOrder() {
		return IView.ORDER[sortOrder.getSelectedIndex()];
	}

	@Override
	public String getShowOrder() {
		return IView.ORDER[showOrder.getSelectedIndex()];
	}

	@Override
	public IContact getCurrentContact() {
		return currentContact;
	}

	@Override
	public void setCurrentContact(IContact currentContact) {
		this.currentContact = currentContact;
	}
	
	@Override
	public void setUpdateButtonEnable(boolean bool) {
		updateButton.setEnabled(bool);
	}
	
	public void setAscending(boolean isAscending) {
		this.isAscending = isAscending;
	}
	
	public JButton getUpdateButton() {
		return updateButton;
	}
	
//	public JButton getCreateButton() {
//		return createButton;
//	}
//	
//	public JButton getPreviousButton() {
//		return previousButton;
//	}
//	
//	public JButton getFirstButton() {
//		return firstButton;
//	}
//	
//	public JButton getEditButton() {
//		return editButton;
//	}
//	
//	public JButton getNextButton() {
//		return nextButton;
//	}
//	
//	public JButton getLastButton() {
//		return lastButton;
//	}
//	
//	public JButton getExportButton() {
//		return exportButton;
//	}
//	
//	public JButton getLoadFile() {
//		return loadFile;
//	}
//	
//	public JButton getSortButton() {
//		return sortButton;
//	}
//	
//	public JButton getShowButton() {
//		return showButton;
//	}
	
}
