package by.nikiter.controller;

import by.nikiter.model.Currency;
import by.nikiter.model.Repo;
import by.nikiter.model.entity.Employee;
import by.nikiter.model.entity.Salary;
import by.nikiter.util.PropManager;
import by.nikiter.util.Regexp;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddEmployeeWindowController implements Initializable {

    private Stage stage;

    @FXML
    private TextField employeeNameField;

    @FXML
    private TextField employeePostField;

    @FXML
    private TextField salaryField;

    @FXML
    private ComboBox<Currency> currencyBox;

    @FXML
    private Label errorLabel;

    @FXML
    private Button addButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ControllersManager.getInstance().setAddEmployeeWindowController(this);

        currencyBox.setItems(FXCollections.observableArrayList(Currency.values()));
        currencyBox.setValue(Currency.BYN);

        addButton.setOnAction(event -> {
            if (addEmployee()) {
                ControllersManager.getInstance().getMainWindowController().updateCurrentTable();
                stage.close();
            }
        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private boolean addEmployee() {
        String name = employeeNameField.getText().trim().replaceAll(Regexp.DOUBLE_SPACE,"");
        String post = employeePostField.getText().trim().replaceAll(Regexp.DOUBLE_SPACE,"");
        String salary = salaryField.getText().trim();

        if (name.isEmpty() || post.isEmpty()) {
            errorLabel.setText(PropManager.getLabel("add_emp.error.empty_text"));
            errorLabel.setVisible(true);
            return false;
        }

        for (Employee emp : Repo.getInstance().getCurrentProduct().getEmployees()) {
            if (emp.getName().equals(name) && emp.getPost().equals(post)) {
                errorLabel.setText(PropManager.getLabel("add_emp.error.dup_emp"));
                errorLabel.setVisible(true);
                return false;
            }
        }

        if (!salary.matches(Regexp.DOUBLE)) {
            errorLabel.setText(PropManager.getLabel("add_emp.error.wrong_format_salary"));
            errorLabel.setVisible(true);
            return false;
        } else if (!salary.contains(".")) {
            salary = salary + ".0";
        }

        Employee employee = new Employee(name,post,new Salary(Double.parseDouble(salary),currencyBox.getValue()));
        Repo.getInstance().addEmployeeToCurrent(employee);

        return true;
    }
}
