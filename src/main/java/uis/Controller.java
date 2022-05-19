package uis;


/**
 * This interface aims to specify a controller's methods
 *
 * @author Miren, Leire and Amanda
 * @version 1
 */
public interface Controller {
    /**
     * Method that sets a window as the main window
     * @param main MainGUI - Current window
     */
    void setMainApp(MainGUI main);
    /**
     * Method to initialize the information in the UI
     */
    void initializeInformation();
}
