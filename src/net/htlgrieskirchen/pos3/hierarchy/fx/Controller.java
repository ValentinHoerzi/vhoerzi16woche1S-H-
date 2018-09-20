/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.htlgrieskirchen.pos3.hierarchy.fx;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import net.htlgrieskirchen.pos3.hierarchy.model.Model;
import net.htlgrieskirchen.pos3.hierarchy.model.Node;

/**
 * FXML Controller class
 *
 * @author Valentin
 */
public class Controller implements Initializable {

    //<editor-fold desc="Internationalizing Items">
    @FXML
    private MenuItem menuItemOpen, menuItemExit, menuItemSave, menuItemSaveAs, menuItemGerman, menuItemEnglish;
    private String save, noSave, cancel, warning, error, headertext1, headertext2, context1, context2, context3;
    public String errorModel, headertext3, context4;
    @FXML
    private Label labelTextSelectedElement,labelTextChangedElement;
    @FXML
    private Menu menuFile, menuLanguage;

    //</editor-fold>
    @FXML
    private Button button;
    @FXML
    private TreeView<Node> treeview;
    @FXML
    private TextField textfield;
    private FileChooser fileChooser;
    private Model model;
    private File file;
    private TreeItem<Node> selectedItem;
    @FXML
    private Label labelSetSelectedItem;
    private boolean unsaved, openedOnce, labelSetSelectedItemIsUnset;
    private ResourceBundle bundle;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        model = new Model();
        unsaved = true;
        openedOnce = false;
        button.setDisable(true);
        fileChooser = new FileChooser();
        labelSetSelectedItem.setText(" ");
        labelSetSelectedItemIsUnset = true;
        bundle = ResourceBundle.getBundle("net.htlgrieskirchen.pos3.hierarchy.fx.MessageBundle", Locale.getDefault());
        fileChooser.setTitle("Filechooser");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Datei (*.csv)", "*.csv"));
        save = "Save";
        noSave = "Don't Save";
        cancel = "Cancel";
        error = "ERROR";
        warning = "WARNING";
        headertext1 = "Save Changes?";
        context1 = "Do you want to Save your changes before opening another document?";
        context2 = "Do you want to Save your changes before exiting the programm?";
        headertext2 = "No Item selected";
        context3 = "Clicking the \"Change\" button without selecting an item is perhibited";
        errorModel = error;
        headertext3 = "File is empty or damaged";
        context4 = "It seems like the file you selected was empty or damaged";
    }

    @FXML
    private void handleMenuOpen(ActionEvent event)
    {
        button.setDisable(false);
        if (unsaved && openedOnce)
        {
            String answer = throwAlert(AlertType.CONFIRMATION, warning, headertext1, context1);
            if (answer.equals(cancel))
            {
                return;
            } else if (answer.equals(save))
            {
                handleMenuSave(event);
            }
        }

        fileChooser.setTitle("Select document to load");
        file = fileChooser.showOpenDialog(Main.getStage());
        if (file != null)
        {
            model.readCsv(file);
        } else
        {
            //TODO: Throw exception
        }
        treeview.setRoot(nodeToTreeItem(model.getTree().getRoot()));
        treeview.refresh();
        file = null;
        openedOnce = true;
    }

    @FXML
    private void handleTreeviewClicked(MouseEvent event)
    {
        selectedItem = treeview.getSelectionModel().getSelectedItem();
        labelSetSelectedItem.setText(String.valueOf(selectedItem.getValue()));
        unsaved = true;
        labelSetSelectedItemIsUnset = false;
    }

    @FXML
    private void handleButtonAction(ActionEvent event)
    {
        String text = textfield.getText();

        if (!labelSetSelectedItemIsUnset)
        {
            Node node = null;
            node = new Node(text, selectedItem.getValue().getParent());
            selectedItem.setValue(node);
            unsaved = true;
        } else
        {
            throwAlert(AlertType.ERROR, error, headertext2, context3);
        }
        labelSetSelectedItem.setText(" ");
        textfield.setText(" ");
        labelSetSelectedItemIsUnset = true;
    }

    @FXML
    private void handleMenuSave(ActionEvent event)
    {
        if (file != null)
        {
            try {
                model.writeCsv(file);
            } catch (IOException ex) {
                throwAlert(Alert.AlertType.ERROR, Main.getController().errorModel, Main.getController().headertext3, Main.getController().context4);
            }
        } else
        {
            handleMenuSaveAs(event);
        }
        unsaved = false;
    }

    @FXML
    private void handleMenuSaveAs(ActionEvent event)
    {
        fileChooser.setTitle("Select document to save file");
        file = fileChooser.showOpenDialog(Main.getStage());
        if (file == null)
        {
            return;
        }
        try {
            model.writeCsv(file);
        } catch (IOException ex) {
             throwAlert(Alert.AlertType.ERROR, Main.getController().errorModel, Main.getController().headertext3, Main.getController().context4);
        }
        unsaved = false;
    }

    @FXML
    private void handleMenuExit(ActionEvent event)
    {
        if (unsaved && openedOnce)
        {
            String answer = throwAlert(AlertType.CONFIRMATION, warning, headertext1, context2);
            if (answer.equals(save))
            {
                handleMenuSave(event);
            } else if (answer.equals(cancel))
            {
                Platform.exit();
                System.exit(0);
            }
        }

        Platform.exit();
        System.exit(0);
    }

    public String throwAlert(AlertType alertType, String title, String headerText, String context)
    {
        String ret = null;
        boolean isConf = false;
        Alert a = new Alert(alertType, context);
        a.setTitle(title);
        a.setHeaderText(headerText);

        if (alertType.equals(AlertType.CONFIRMATION))
        {
            isConf = true;
        }
        if (alertType.equals(AlertType.CONFIRMATION) && context.equals(context1))
        {
            a.getButtonTypes().setAll(new ButtonType(save, ButtonBar.ButtonData.OTHER), new ButtonType(noSave, ButtonBar.ButtonData.OTHER), new ButtonType(cancel, ButtonBar.ButtonData.CANCEL_CLOSE));
        }
        if (alertType.equals(AlertType.CONFIRMATION) && context.equals(context2))
        {
            a.getButtonTypes().setAll(new ButtonType(save, ButtonBar.ButtonData.YES), new ButtonType(cancel, ButtonBar.ButtonData.CANCEL_CLOSE));
        }
        Optional<ButtonType> showAndWait = a.showAndWait();
        if (isConf)
        {
            ret = showAndWait.get().getText();
        }
        return ret;
    }

    private static TreeItem<Node> nodeToTreeItem(Node node)
    {
        TreeItem<Node> result = new TreeItem<>(node);
        List<Node> children = node.getChildren();
        if (!children.isEmpty())
        {
            for (Node child : children)
            {
                result.getChildren().add(nodeToTreeItem(child));
            }
        }
        return result;
    }

    private void changeLanguage(Locale locale)
    {
        bundle = ResourceBundle.getBundle("net.htlgrieskirchen.pos3.hierarchy.fx.MessageBundle",locale);
        menuItemOpen.setText(bundle.getString("menuItemOpen"));
        menuItemExit.setText(bundle.getString("menuItemExit"));
        menuItemSave.setText(bundle.getString("menuItemSave"));
        menuItemSaveAs.setText(bundle.getString("menuItemSaveAs"));
        menuItemGerman.setText(bundle.getString("menuItemGerman"));
        menuItemEnglish.setText(bundle.getString("menuItemEnglish"));
        menuFile.setText(bundle.getString("menuFile"));
        menuLanguage.setText(bundle.getString("menuLanguage"));
        save = bundle.getString("save");
        noSave = bundle.getString("noSave");
        cancel = bundle.getString("cancel");
        warning = bundle.getString("warning");
        error = bundle.getString("error");
        errorModel = bundle.getString("errorModel");
        button.setText(bundle.getString("buttonChange"));
        headertext1 = bundle.getString("headertext1");
        headertext2 = bundle.getString("headertext2");
        headertext3 = bundle.getString("headertext3");
        context1 = bundle.getString("context1");
        context2 = bundle.getString("context2");
        context3 = bundle.getString("context3");
        context4 = bundle.getString("context4");
        labelTextChangedElement.setText(bundle.getString("labelTextChangedElement"));
        labelTextSelectedElement.setText(bundle.getString("labelTextSelectedElement"));
   }

    @FXML
    private void onMenuItemGerman(ActionEvent event)
    {
        changeLanguage(Locale.GERMAN);
    }

    @FXML
    private void onMenuItemEnglish(ActionEvent event)
    {
        changeLanguage(Locale.ENGLISH);
    }
}
