/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.htlgrieskirchen.pos3.hierarchy.model;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Valentin
 */
public class Tree {

    private final Node root;

    public Tree() {
        root = new Node("Root", null);
    }
    
    public Node getRoot(){
        return root;
    }
    
    public List<Node> getLeafs() {
        List<Node> result = new LinkedList<>();
        
        collectLeafsRecursively(root, result);
        
        return result;
    }
    
    private void collectLeafsRecursively(Node start, List<Node> leafs) {
        List<Node> children = start.getChildren();
        if (children.isEmpty()) {
            leafs.add(start);
        } else {
            children.forEach((child) -> collectLeafsRecursively(child, leafs));
        }
    }
    
    @Override
    public String toString() {
        return root != null ? root.subtreeToString() : null;
    }   
}