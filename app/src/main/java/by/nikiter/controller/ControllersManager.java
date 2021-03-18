package by.nikiter.controller;

public class ControllersManager {

    private static volatile ControllersManager instance = null;

    private MainWindowController mainWindowController = null;
    private AddProductWindowController addProductWindowController = null;
    private AddRawWindowController addRawWindowController = null;

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
}