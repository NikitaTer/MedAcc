package by.nikiter.controller;

public class ControllersManager {

    private static volatile ControllersManager instance = null;

    private MainWindowController mainWindowController = null;
    private AddProductWindowController addProductWindowController = null;
    private AddRawWindowController addRawWindowController = null;
    private EditRawWindowController editRawWindowController = null;
    private EditProductWindowController editProductWindowController = null;
    private NewRawWindowController newRawWindowController = null;

    private ControllersManager() {

    }

    public static ControllersManager getInstance() {
        if (instance != null) {
            return instance;
        }

        synchronized (ControllersManager.class) {
            if (instance == null) {
                instance = new ControllersManager();
            }

            return instance;
        }
    }

    public MainWindowController getMainWindowController() {
        return mainWindowController;
    }

    public void setMainWindowController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }

    public AddProductWindowController getAddProductWindowController() {
        return addProductWindowController;
    }

    public void setAddProductWindowController(AddProductWindowController addProductWindowController) {
        this.addProductWindowController = addProductWindowController;
    }

    public AddRawWindowController getAddRawWindowController() {
        return addRawWindowController;
    }

    public void setAddRawWindowController(AddRawWindowController addRawWindowController) {
        this.addRawWindowController = addRawWindowController;
    }

    public EditRawWindowController getEditRawWindowController() {
        return editRawWindowController;
    }

    public void setEditRawWindowController(EditRawWindowController editRawWindowController) {
        this.editRawWindowController = editRawWindowController;
    }

    public EditProductWindowController getEditProductWindowController() {
        return editProductWindowController;
    }

    public void setEditProductWindowController(EditProductWindowController editProductWindowController) {
        this.editProductWindowController = editProductWindowController;
    }

    public NewRawWindowController getNewRawWindowController() {
        return newRawWindowController;
    }

    public void setNewRawWindowController(NewRawWindowController newRawWindowController) {
        this.newRawWindowController = newRawWindowController;
    }
}