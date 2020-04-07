package controller_view;

import client.Client;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Controller for JavaFX application
 * @author Michal Szeler
 * @version 3.0
 */
public class Controller {

    /**
     * Field contains an instance of Client class for connecting with server
     */
    private Client client;
    /**
     * Field contains an instance of BorderPane from MainWindow.fxml
     */
    @FXML
    private BorderPane borderPane;
    /**
     * Field contains an instance of Button from MainWindow.fxml that generates pair of Keys after being pressed
     */
    @FXML
    private Button generateKeysButton;
    /**
     * Field contains an instance of Button from MainWindow.fxml that processes input after being pressed
     */
    @FXML
    private Button processButton;
    /**
     * Field contains an instance of TextArea from MainWindow.fxml that holds unprocessed text
     */
    @FXML
    private TextArea unprocessedTextArea;
    /**
     * Field contains an instance of TextField from MainWindow.fxml that holds private key
     */
    @FXML
    private TextField privateKeyTextField;
    /**
     * Field contains an instance of TextField from MainWindow.fxml that holds public key
     */
    @FXML
    private TextField publicKeyTextField;
    /**
     * Field contains an instance of TextArea from MainWindow.fxml that holds processed text
     */
    @FXML
    private TextArea processedTextArea;
    /**
     * Field contains an instance of ToggleGroup containing radio buttons from MainWindow.fxml
     */
    @FXML
    private ToggleGroup optionToggleGroup;

    /**
     * Method initializes algorithm field by creating new instance of Client class
     */
    public final void initialize(){
        String propertiesName = "client.properties";
        try{
            client = new Client(propertiesName);
        } catch (IOException e){
            processedTextArea.setText("Couldn't establish connection to server");
            generateKeysButton.setDisable(true);
            processButton.setDisable(true);
        }
    }

    /**
     * Event handler for when generateKeysButton is clicked
     * If generator wasn't initialized appropriately, a message is shown in processedTextArea
     * Even if Client class is unable to generate keys, encryption and decryption still can be done
     */
    @FXML
    public final void generateKeysButtonClicked() {
        try{
            String[] keyPair = client.getKeyPair();
            privateKeyTextField.setText(keyPair[0]);
            publicKeyTextField.setText(keyPair[1]);
        } catch(IOException e){
            if(e.getMessage().equals("Keys generator is not initialized")){
                generateKeysButton.setDisable(true);
                processedTextArea.setText(e.getMessage() + "\n" +
                        "Generate keys button has been disabled\n" +
                        "You can still encrypt and decrypt text by manually adding encryption keys");
            }
            else{
                processedTextArea.setText(e.getMessage());
            }
        }
    }

    /**
     * Event handler for when processButton is clicked
     * If an exception occurs while processing the data, an adequate message is shown in processedTextArea
     */
    @FXML
    public void processButtonClicked(){
        if(!unprocessedTextArea.getText().isEmpty()){
            RadioButton selectedRadioButton = (RadioButton) optionToggleGroup.getSelectedToggle();
            String selectedRadioButtonString = selectedRadioButton.getText();
            if(selectedRadioButtonString.equals("Encrypt text")){
                String publicKeyString = publicKeyTextField.getText();
                if(!publicKeyString.isEmpty()){
                    processedTextArea.setText(client.encryptMessage(unprocessedTextArea.getText(),publicKeyString));
                }
                else {
                    processedTextArea.setText("Public key text field is empty");
                }
            }
            else {
                String privateKeyString = privateKeyTextField.getText();
                if(!privateKeyString.isEmpty()){
                    processedTextArea.setText(client.decryptMessage(unprocessedTextArea.getText(), privateKeyString));
                }
                else {
                    processedTextArea.setText("Private key text field is empty");
                }
            }
        }
        else{
            processedTextArea.setText("Incorrect input in insert text field");
        }
    }

    /**
     * Method that shows new Open Dialog window, which allows to open a file with .txt extension
     */
    @FXML
    public void showOpenTextFileDialog(){
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open file with text");
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text","*.txt"));
        File file = chooser.showOpenDialog(borderPane.getScene().getWindow());
        try{
            if(file!=null){
               unprocessedTextArea.setText(new String(Files.readAllBytes(Paths.get(file.getPath()))));
            }
        }
        catch (IOException e){
            unprocessedTextArea.setText("Incorrect data in file");
            e.printStackTrace();
        }
    }

    /**
     * Method that shows new Save Dialog window, which allows to save the result of the program to a file with .txt extension
     */
    @FXML
    public void showSaveProcessedTextDialog(){
        if(processedTextArea.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No text has been processed");
            alert.showAndWait();
        }
        else{
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Save processed text");
            chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text","*.txt"));
            File file = chooser.showSaveDialog(borderPane.getScene().getWindow());
            try{
                processedTextArea = new TextArea(new String(Files.readAllBytes(Paths.get(file.getPath()))));
                Files.writeString(Paths.get(file.getPath()), processedTextArea.toString());
            }
            catch (IOException e){
                processedTextArea.setText("Unable to save to file");
                e.printStackTrace();
            }
        }
    }
}
