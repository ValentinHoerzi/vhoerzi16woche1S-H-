package net.htlgrieskirchen.pos3.hierarchy.model;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class ModelTest {

    public static final String TMP_CSV = "tmp.csv";

    @Test
    public void testCorrectReadCsv() throws IOException {
        List<String> lines = Arrays.asList(
                "HEADING",
                "A",
                "A",
                "B",
                "B");

        Model testModel = getTreeModel(lines);

        Tree testTree = testModel.getTree();

        Node rootNode = testTree.getRoot();

        assertEquals(rootNode.getName(), "Root");
        assertEquals(rootNode.getChildren().size(), 2);

        Node aNode = findChildren(rootNode.getChildren(), "A");
        Node bNode = findChildren(rootNode.getChildren(), "B");

        assertNotNull("node A not available", aNode);
        assertNotNull("node B not available", bNode);
    }

    @Test()
    public void testCorrectReadCsvChild() throws IOException {
        List<String> lines = Arrays.asList(
                "HEADING",
                "A;C",
                "B;C");

        Model testModel = getTreeModel(lines);

        Tree testTree = testModel.getTree();

        Node rootNode = testTree.getRoot();

        assertEquals(rootNode.getName(), "Root");
        assertEquals(rootNode.getChildren().size(), 2);

        Node aNode = findChildren(rootNode.getChildren(), "A");
        Node bNode = findChildren(rootNode.getChildren(), "B");

        assertNotNull("node A not available", aNode);
        assertNotNull("node B not available", bNode);

        Node c1Node = findChildren(aNode.getChildren(), "C");
        Node c2Node = findChildren(aNode.getChildren(), "C");

        assertNotNull("node C in A not available", c1Node);
        assertNotNull("node C in B not available", c2Node);
    }

    @Test()
    public void testCorrectReadCsvOneLine() throws IOException {
        List<String> lines = Arrays.asList("");

        Model testModel = getTreeModel(lines);

        assertEquals(testModel.getTree().getRoot().getName(), "Root");
        assertEquals(testModel.getTree().getRoot().getChildren().size(), 0);
    }

    @Test()
    public void testCorrectReadCsvEmpty() throws IOException {
        File testFile = new File(TMP_CSV);
        testFile.createNewFile();

        Model testModel = new Model();
        testModel.readCsv(testFile);

        testFile.delete();

        assertEquals(testModel.getTree().getRoot().getName(), "Root");
        assertEquals(testModel.getTree().getRoot().getChildren().size(), 0);
    }

    @Test()
    public void testCorrectReadCsvException() {
        Model testModel = new Model();

        boolean thrown = false;
        try {
            File testFile = new File("not there");
            testFile.delete();

            testModel.readCsv(new File("not there"));
        } catch (Exception e) {
            thrown = true;
        }
        
        assertTrue(thrown);
        
//        assertThrows(IOException.class, () -> {
//            File testFile = new File("not there");
//            testFile.delete();
//
//            testModel.readCsv(new File("not there"));
//        });
    }

    private Model getTreeModel(List<String> lines) throws IOException {
        try (PrintWriter testFile = new PrintWriter(TMP_CSV)) {
            lines.forEach(line -> testFile.println(line));
        }

        File testFile = new File(TMP_CSV);

        Model testModel = new Model();

        testModel.readCsv(testFile);

        testFile.delete();

        return testModel;
    }

    private Node findChildren(List<Node> nodeList, String name) {
        for (Node cNode: nodeList) {
            if(cNode.getName().equals(name)) {
                return cNode;
            }
        }

        return null;
    }

    @Test
    public void testWriteCsv() throws IOException {
        List<String> lines = Arrays.asList("Wirtschaftssektor;Branche;Gruppe;Sparte",
                "Rohstoffe;Land- & Forstwirtschaft;Landwirtschaft;Pflanzen",
                "Rohstoffe;Land- & Forstwirtschaft;Landwirtschaft;Tierhaltung",
                "Rohstoffe;Bergbau;Sand, Ton & Steine;Sonstige",
                "Industrie;Produktion;Ernährung;Pflanzliche Lebensmittel");

        Model testModel = getTreeModel(lines);

        File testFile = new File(TMP_CSV);

        assertFalse(testFile.exists());

        testModel.writeCsv(testFile);

        assertTrue(testFile.exists());

        int lineIndex = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(testFile))) {
            String cLine;
            for(; (cLine = br.readLine()) != null; lineIndex++) {
                assertEquals(cLine, lines.get(lineIndex));
            }
        }

        assertEquals(lineIndex, lines.size());
    }

    @Test
    public void testWriteCsvEmptyTree() throws IOException {
        Model testModel = new Model();
        File testFile = new File(TMP_CSV);

        testModel.writeCsv(testFile);

        assertFalse(testFile.exists());
    }

    @Test()
    public void testCorrectWriteCsvException() throws IOException {
        List<String> lines = Arrays.asList("h",
                "Rohstoffe;Land- & Forstwirtschaft;Landwirtschaft;Pflanzen",
                "Rohstoffe;Land- & Forstwirtschaft;Landwirtschaft;Tierhaltung",
                "Rohstoffe;Bergbau;Sand, Ton & Steine;Sonstige",
                "Industrie;Produktion;Ernährung;Pflanzliche Lebensmittel");

        Model testModel = getTreeModel(lines);

        boolean thrown = false;
        try {
            File testFile = new File("not there");
            testFile.createNewFile();
            testFile.setReadOnly();
            testModel.writeCsv(testFile);
            testFile.delete();
        } catch (IOException e) {
            thrown = true;
        }
        
        assertTrue(thrown);
        
//        assertThrows(IOException.class, () -> {
//            File testFile = new File("not there");
//            testFile.createNewFile();
//            testFile.setReadOnly();
//            testModel.writeCsv(testFile);
//            testFile.delete();
//        });
    }
}
