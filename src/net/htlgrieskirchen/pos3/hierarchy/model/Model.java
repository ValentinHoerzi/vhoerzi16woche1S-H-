/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.htlgrieskirchen.pos3.hierarchy.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import net.htlgrieskirchen.pos3.hierarchy.fx.Main;

/**
 *
 * @author Valentin
 */
public class Model {

    private final String SEPERATOR = ";";
    private String firstRow = "";

    private Tree tree;

    public Tree getTree() {
        return tree;
    }

    public void readCsv(File file) {
        String line = null;
        List<String> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            firstRow = br.readLine();
            while ((line = br.readLine()) != null) {
                list.add(line);
            }

        } catch (IOException e) {
            Main.getController().throwAlert(Alert.AlertType.ERROR, Main.getController().errorModel, Main.getController().headertext3, Main.getController().context4);
        }

        tree = new Tree();
        
        list.stream().map((stringLine) -> stringLine.split(SEPERATOR)).forEachOrdered((splits)
                -> {
            Node node = tree.getRoot();
            for (int i = 0; i < splits.length; ++i) {
                node = node.addChild(splits[i]);
            }
        });
    }

    public void writeCsv(File file) throws IOException {

        if (tree == null) {
            return;
        }

        if (file != null) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                List<Node> nodes = new LinkedList<>();
                nodes = tree.getLeafs();

                bw.write(firstRow);
                bw.newLine();
                for (Node node : nodes) {
                    Node tempNode = node.getParent();
                    String rg = node.getName();

                    while (tempNode.getParent() != null) {
                        rg = tempNode.getName() + ";" + rg;
                        tempNode = tempNode.getParent();
                    }

                    bw.write(rg);
                    bw.newLine();
                }
            }
        }else{
            Main.getController().throwAlert(Alert.AlertType.ERROR, Main.getController().errorModel, Main.getController().headertext3, Main.getController().context4);
        }
    }
}
