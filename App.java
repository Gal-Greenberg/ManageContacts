import javafx.stage.Stage;

/*
 * gal zilca
 * 205787161
 * 
 */

public class App extends javafx.application.Application {

	public static void main(String[] args) {
		try {
			launch(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void start(Stage primaryStage) throws Exception {
		ContactsManager model = new ContactsManager("contacts.dat");
		Controller controller = new Controller(model);
		IView<?> firstView = new ContactsManagerFrame();
		IView<?> secondView = new ContactsManagerJFX(primaryStage);
		
		controller.addView(firstView);
		controller.addView(secondView);
	}
	
}
