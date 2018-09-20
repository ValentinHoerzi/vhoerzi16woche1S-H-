package net.htlgrieskirchen.pos3.hierarchy.fx;

import org.junit.Test;
import static org.junit.Assert.*;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import net.htlgrieskirchen.pos3.hierarchy.model.Model;
import net.htlgrieskirchen.pos3.hierarchy.model.Node;

import java.io.File;
import net.htlgrieskirchen.pos3.util.JavaFXUtil;

public class ControllerTest {

    @Test
    public void testControllerVariables() {
        System.out.println("Controller Object Variables");

        JavaFXUtil.getField("button", Button.class);
        JavaFXUtil.getField("treeview", TreeView.class);
        JavaFXUtil.getField("textfield", TextField.class);
        JavaFXUtil.getField("fileChooser", FileChooser.class);
        JavaFXUtil.getField("model", Model.class);
        JavaFXUtil.getField("file", File.class);
        JavaFXUtil.getField("selectedItem", TreeItem.class);
        JavaFXUtil.getField("unsaved", Boolean.class);
    }

    @Test
    public void testControllerMethods() {
        System.out.println("Controller Object Methods");

        JavaFXUtil.getMethod("handleTreeviewClicked", MouseEvent.class);
        JavaFXUtil.getActionEventHandlerMethod("handleButtonAction");
        JavaFXUtil.getActionEventHandlerMethod("handleMenuOpen");
        JavaFXUtil.getActionEventHandlerMethod("handleMenuSave");
        JavaFXUtil.getActionEventHandlerMethod("handleMenuSaveAs");
        JavaFXUtil.getActionEventHandlerMethod("handleMenuExit");
    }

    @Test
    public void testTextBoxInitialization() {
        System.out.println("Textbox initialization");

        TextField testTextField = JavaFXUtil.getField("textfield", TextField.class);

        assertTrue("text field must be empty at begin", testTextField.getCharacters().toString().isEmpty());
    }

    @Test
    public void testTreeViewLoad() {
        JavaFXUtil.processEvent("handleMenuOpen");
        TreeView<Node> testTreeView = JavaFXUtil.getField("treeview", TreeView.class);

        TreeItem<Node> rootNode = testTreeView.getRoot();
        assertEquals(rootNode.getChildren().size(), 4);

    }
}
