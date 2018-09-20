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
public class Node {

    private String name;
    private final Node parent;
    private final List<Node> children;

    public Node(String name, Node parent)
    {
        this.name = name;
        this.parent = parent;
        this.children = new LinkedList<>();
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Node getParent()
    {
        return parent;
    }

    public List<Node> getChildren()
    {
        return children;
    }

    public Node addChild(String name)
    {
        Node result = null;

        // search name
        for (Node node : children)
        {
            if (node.getName().equals(name))
            {
                result = node;
                break;
            }
        }

        // if not found, add name
        if (result == null)
        {
            result = new Node(name, this);
            children.add(result);
        }

        return result;
    }

    @Override
    public String toString()
    {
        return name;
    }

    public String subtreeToString()
    {
        StringBuilder sb = new StringBuilder();
        subtreeToStringRecursively(this, 0, sb);
        return sb.toString();
    }

    private void subtreeToStringRecursively(Node start, int level, StringBuilder sb)
    {
        // add indent
        for (int i = 0; i < level; ++i)
        {
            sb.append(' ');
        }

        // add node name
        sb.append(start.getName());

        // add child names recursively
        level++;
        for (Node child : start.getChildren())
        {
            sb.append('\n');
            subtreeToStringRecursively(child, level, sb);
        }
    }
}
