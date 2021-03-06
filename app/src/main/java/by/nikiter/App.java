package by.nikiter;

import by.nikiter.controller.ControllersManager;
import by.nikiter.controller.MainWindowController;
import by.nikiter.util.PropManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Hello world!
 *
 */
public class App extends Application {

    //todo: make default locale
    private static volatile Locale locale = Locale.forLanguageTag("ru");

    public static void main( String[] args ) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle(PropManager.getLabel("app_name"));
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(400);

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/view/MainWindow.fxml"),
                ResourceBundle.getBundle("labels")
        );

        Parent root = loader.load();
        ((MainWindowController)loader.getController()).setStage(primaryStage);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static Locale getLocale() {
        return locale;
    }

    public static void setLocale(Locale locale) {
        App.locale = locale;
    }
}
