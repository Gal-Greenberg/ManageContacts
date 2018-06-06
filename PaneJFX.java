import java.util.ArrayList;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.geometry.Insets;

public class PaneJFX extends Pane implements PaneInterface {
	
	private final static int DRAW_OFFSET = 10;
	
	private ArrayList<IActionListener> listeners;
	
	private Timeline colorAnimation;
	private TextField textFieldFirst;
	private TextField textFieldLast;
	private TextField textFieldPhone;
	private Button createButton;
	private Button updateButton;
	
	private IContact currentContact;
	
	private Button previousButton;
	private Button firstButton;
	private Button editButton;
	private Button nextButton;
	private Button lastButton;
	private Label[] labels = new Label[Contact.MEMBERS_NUM];
	
	private ComboBox<String> fileTypes;
	private Button exportButton;
	private TextField textFieldPath;
	private Button loadFile;
	
	private ComboBox<String> sort;
	private ComboBox<String> sortBy;
	private ComboBox<String> sortOrder;
	private Button sortButton;
	private ComboBox<String> showOrder;
	private Button showButton;
	
	private Timeline animation;
	private boolean isAscending;
	private CurrentColor viewState;
	
	public PaneJFX(ArrayList<IActionListener> listeners) {
		this.listeners = listeners;
		this.viewState = CurrentColor.START;
		setColorAnimation();
		
		Pane pNorth = north();
		Pane pCenter = center();
		Pane pSouth = south();
		Pane pDownSouth = downSouth();
		
		GridPane pane = new GridPane();
		pane.setPadding(new Insets(0, DRAW_OFFSET, DRAW_OFFSET, DRAW_OFFSET));
		pane.setHgap(DRAW_OFFSET);
		pane.setVgap(DRAW_OFFSET);
		
		pane.add(pNorth, 0, 1);
		pane.add(pCenter, 0, 2);
		pane.add(pSouth, 0, 3);
		pane.add(pDownSouth, 0, 4);
		
		getChildren().add(pane);
	}
	
	private void setColorAnimation() {
		colorAnimation = new Timeline(new KeyFrame(Duration.millis(1000), new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				setTextColor(viewState.getCurrentColor());
			}
		}));
		
		colorAnimation.setCycleCount(Timeline.INDEFINITE);
		colorAnimation.play();
	}
	
	private void setTextColor(Color color) {
		for (int i = 0; i < Contact.MEMBERS_NUM; i++) {
			labels[i].setTextFill(color);
		}
	}
	
	public Pane north() {
		GridPane pane = new GridPane();
		pane.setHgap(DRAW_OFFSET*9);
		pane.setVgap(DRAW_OFFSET);
		
		Label first = new Label("First name:");
		textFieldFirst = new TextField();
		
		Label last = new Label("Last name:");
		textFieldLast = new TextField();

		Label phone = new Label("Phone number:");
		textFieldPhone = new TextField();
		
		createButton = new Button(IActionListener.CREATE);
		createButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				for (IActionListener actionListener : listeners) {
					actionListener.create(textFieldFirst.getText(), textFieldLast.getText(), textFieldPhone.getText());
				}
			}
		});
		
		updateButton = new Button(IActionListener.UPDATE);
		updateButton.setDisable(true);
		updateButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				for (IActionListener actionListener : listeners) {
					actionListener.update(textFieldFirst.getText(), textFieldLast.getText(), textFieldPhone.getText());
				}
			}
		});
		
		pane.add(first, 0, 0);
		pane.add(textFieldFirst, 2, 0);
		
		pane.add(last, 0, 1);
		pane.add(textFieldLast, 2, 1);
		
		pane.add(phone, 0, 2);
		pane.add(textFieldPhone, 2, 2);
		
		pane.add(createButton, 0, 3);
		pane.add(updateButton, 2, 3);
		
		return pane;
	}
	
	public Pane center() {
		GridPane pane = new GridPane();
		pane.setHgap(DRAW_OFFSET*8);
		pane.setVgap(DRAW_OFFSET);
		
		Pane pLeft = leftCenter();
		Pane pMiddle = middleCenter();
		Pane pRight = rightCenter();
		
		pane.add(pLeft, 0, 0);
		pane.add(pMiddle, 1, 0);
		pane.add(pRight, 2, 0);
		
		return pane;
	}
	
	public Pane leftCenter() {
		GridPane pane = new GridPane();
		pane.setHgap(DRAW_OFFSET);
		pane.setVgap(DRAW_OFFSET);
		
		previousButton = new Button(IActionListener.PREVIOUS);
		previousButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				for (IActionListener actionListener : listeners) {
					actionListener.previous();
				}
			}
		});
		
		firstButton = new Button(IActionListener.FIRST);
		firstButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				for (IActionListener actionListener : listeners) {
					actionListener.first();
				}
			}
		});
		
		pane.add(previousButton, 0, 0);
		pane.add(firstButton, 0, 1);
		
		return pane;
	}
	
	public Pane middleCenter() {
		GridPane pane = new GridPane();
		pane.setHgap(DRAW_OFFSET*9);
		pane.setVgap(DRAW_OFFSET);
		
		Label first = new Label("First name:");
		Label last = new Label("Last name:");
		Label phone = new Label("Phone number:");
		
		for (int i = 0; i < Contact.MEMBERS_NUM; i++) {
			labels[i] = new Label();
		}
		
		editButton = new Button("Edit contact");
		editButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				String[] currentContactInfo = currentContact.getUiData();
				updateButton.setDisable(true);
				fillTextFiled(currentContactInfo[1], currentContactInfo[2], currentContactInfo[3]);
			}
		});
		
		pane.add(first, 0, 0);
		pane.add(labels[0], 1, 0);
		pane.add(last, 0, 1);
		pane.add(labels[1], 1, 1);
		pane.add(phone, 0, 2);
		pane.add(labels[2], 1, 2);
		pane.add(editButton, 0, 3);
		
		return pane;
	}

	public Pane rightCenter() {
		GridPane pane = new GridPane();
		pane.setHgap(DRAW_OFFSET);
		pane.setVgap(DRAW_OFFSET);
		
		nextButton = new Button(IActionListener.NEXT);
		nextButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				for (IActionListener actionListener : listeners) {
					actionListener.next();
				}
			}
		});
		
		lastButton = new Button(IActionListener.LAST);
		lastButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				for (IActionListener actionListener : listeners) {
					actionListener.last();
				}
			}
		});
		
		pane.add(nextButton, 0, 0);
		pane.add(lastButton, 0, 1);
		
		return pane;
	}
	
	public Pane south() {
		GridPane pane = new GridPane();
		pane.setHgap(DRAW_OFFSET*6);
		pane.setVgap(DRAW_OFFSET);
		
		Pane pRight = rightSouth();
		
		exportButton = new Button(IActionListener.EXPORT);
		exportButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				for (IActionListener actionListener : listeners) {
					actionListener.export(fileTypes.getValue());
				}
			}
		});
		
		ObservableList<String> items = FXCollections.observableArrayList(IView.FILE_TYPES);
		fileTypes = new ComboBox<>(items);
		fileTypes.setValue(IView.FILE_TYPES[0]);
		
		pane.add(fileTypes, 0, 0);
		pane.add(exportButton, 1, 0);
		pane.add(pRight, 2, 0);
		
		return pane;
	}
	
	public Pane rightSouth() {
		GridPane pane = new GridPane();
		pane.setHgap(DRAW_OFFSET);
		pane.setVgap(DRAW_OFFSET);
		
		Label filePath = new Label("file path:");
		textFieldPath = new TextField();
		
		loadFile = new Button(IActionListener.LOAD_FILE);
		loadFile.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				for (IActionListener actionListener : listeners) {
					actionListener.loadFile(fileTypes.getValue(), textFieldPath.getText());
				}
			}
		});
		
		pane.add(filePath, 0, 0);
		pane.add(textFieldPath, 0, 1);
		pane.add(loadFile, 0, 2);
		
		return pane;
	}
	
	public Pane downSouth() {
		GridPane pane = new GridPane();
		pane.setHgap(DRAW_OFFSET*0.5);
		pane.setVgap(DRAW_OFFSET);
		
		ObservableList<String> sortValues = FXCollections.observableArrayList(IView.SORT);
		sort = new ComboBox<String>(sortValues);
		sort.setValue(IView.SORT[0]);
		
		ObservableList<String> sortByValues = FXCollections.observableArrayList(IView.FIELD);
		sortBy = new ComboBox<String>(sortByValues);
		sortBy.setValue(IView.FIELD[0]);
		
		ObservableList<String> sortOrderValues = FXCollections.observableArrayList(IView.ORDER);
		sortOrder = new ComboBox<String>(sortOrderValues);
		sortOrder.setValue(IView.ORDER[0]);
		
		sortButton = new Button(IActionListener.SORT);
		sortButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				for (IActionListener actionListener : listeners) {
					actionListener.sort(sort.getValue(), sortBy.getValue(),	sortOrder.getValue());
				}
			}
		});
		
		ObservableList<String> showOrderValues = FXCollections.observableArrayList(IView.ORDER);
		showOrder = new ComboBox<String>(showOrderValues);
		showOrder.setValue(IView.ORDER[0]);
		
		showButton = new Button(IActionListener.SHOW);
		showButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				for (IActionListener actionListener : listeners) {
					actionListener.show(showOrder.getValue());
				}
			}
		});
		
		animation = new Timeline(new KeyFrame(Duration.millis(1000), new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				String prevContactId = currentContact.getUiData()[0];
				if (isAscending) {
					nextButton.fire();
				} else {
					previousButton.fire();
				}
				if (currentContact.getUiData()[0].equals(prevContactId)) {
					animation.pause();
				}
				viewContact();
			}
		}));
		
		pane.add(sort, 0, 0);
		pane.add(sortBy, 1, 0);
		pane.add(sortOrder, 2, 0);
		pane.add(sortButton, 3, 0);
		pane.add(showOrder, 1, 1);
		pane.add(showButton, 2, 1);
		
		return pane;
	}
	
	@Override
	public void startTicking() {
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.play();
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

//	public Timer getTimer() {
//		return timer;
//	}

//	public void setTimer(Timer timer) {
//		this.timer = timer;
//	}
	
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

//	public ComboBox<String> getFileTypes() {
//		return fileTypes;
//	}
//
//	public ComboBox<String> getSort() {
//		return sort;
//	}
//
//	public ComboBox<String> getSortBy() {
//		return sortBy;
//	}
	
	@Override
	public String getSortOrder() {
		return sortOrder.getValue();
	}
	
	@Override
	public String getShowOrder() {
		return showOrder.getValue();
	}

	@Override
	public IContact getCurrentContact() {
		return currentContact;
	}

	@Override
	public void setCurrentContact(IContact currentContact) {
		this.currentContact = currentContact;
	}
	
	public void setUpdateButtonEnable(boolean bool) {
		updateButton.setDisable(!bool);
	}
	
	@Override
	public void setAscending(boolean isAscending) {
		this.isAscending = isAscending;
	}
//	
//	public Button getUpdateButton() {
//		return updateButton;
//	}
//	
//	public Button getCreateButton() {
//		return createButton;
//	}
//	
//	public Button getPreviousButton() {
//		return previousButton;
//	}
//	
//	public Button getFirstButton() {
//		return firstButton;
//	}
//	
//	public Button getEditButton() {
//		return editButton;
//	}
//	
//	public Button getNextButton() {
//		return nextButton;
//	}
//	
//	public Button getLastButton() {
//		return lastButton;
//	}
//	
//	public Button getExportButton() {
//		return exportButton;
//	}
//	
//	public Button getLoadFile() {
//		return loadFile;
//	}
//	
//	public Button getSortButton() {
//		return sortButton;
//	}
//	
//	public Button getShowButton() {
//		return showButton;
//	}
	
}
