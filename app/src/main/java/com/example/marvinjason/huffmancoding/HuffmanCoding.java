package com.example.marvinjason.huffmancoding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HuffmanCoding
{
    private final LinkedHashMap map;
    private final List<Node> nodes;
    private final List<String> dictionary;
    private final String string;
    private StringBuilder path;
    private int compressedSize;
    private int uncompressedSize;
    private String compressedString;

    public HuffmanCoding(String str)
    {
        map = new LinkedHashMap();
        nodes = new ArrayList();
        dictionary = new ArrayList();
        string = str;

        for (char c: str.toCharArray())
        {
            if (map.containsKey(c))
            {
                map.put(c, ((int) map.get(c)) + 1);
            }
            else
            {
                map.put(c, 1);
            }
        }

        Iterator iterator = map.entrySet().iterator();

        while (iterator.hasNext())
        {
            Map.Entry entry = (Map.Entry) iterator.next();
            nodes.add(new Node((int) entry.getValue(), (char) entry.getKey()));
        }

        buildTree();
    }

    private void buildTree()
    {
        while (nodes.size() != 1)
        {
            sort();

            Node node = new Node(nodes.get(0).getData() + nodes.get(1).getData());
            node.setWidth(nodes.get(0).getWidth() + nodes.get(1).getWidth() + 60);
            node.setHeight(Math.max(nodes.get(0).getHeight(), nodes.get(1).getHeight()) + 80);
            node.setMiddle(nodes.get(0).getWidth());
            node.setLeftChild(nodes.get(0));
            node.setRightChild(nodes.get(1));
            nodes.remove(0);
            nodes.remove(0);
            nodes.add(node);
        }
    }

    private void sort()
    {
        Collections.sort(nodes, new Comparator<Node>(){
            @Override
            public int compare(Node x, Node y)
            {
                //return Integer.compare(x.getData(), y.getData());
                return Integer.valueOf(x.getData()).compareTo(Integer.valueOf(y.getData()));
            }
        });
    }

    public void compress()
    {
        StringBuilder str = new StringBuilder();

        for (char c: string.toCharArray())
        {
            path = new StringBuilder();
            compress(c, nodes.get(0));
            str.append(path);
            //System.out.println(c + " - " + path);
            String temp = c + " - " + path;
            dictionary.add(temp);
        }

        uncompressedSize = string.length() * 8;
        compressedSize = str.length();
        compressedString = str.toString();
    }

    private boolean compress(char c, Node node)
    {
        if (node.getCharacter() == c)
        {
            return true;
        }

        if (node.getLeftChild() != null)
        {
            path.append('0');

            if (compress(c, node.getLeftChild()))
            {
                return true;
            }

            path.deleteCharAt(path.length() - 1);
        }

        if (node.getRightChild() != null)
        {
            path.append('1');

            if (compress(c, node.getRightChild()))
            {
                return true;
            }

            path.deleteCharAt(path.length() - 1);
        }

        return false;
    }

    public int getCompressedSize()
    {
        return compressedSize;
    }

    public int getUncompressedSize()
    {
        return uncompressedSize;
    }

    public String getCompressedString()
    {
        return compressedString;
    }

    public List<String> getDictionary()
    {
        return dictionary;
    }

    public Node getRoot() { return nodes.get(0); }
}

class Node
{
    private final int data;
    private Node leftChild;
    private Node rightChild;
    private char character;
    private int width;
    private int height;
    private int middle;

    Node(int data)
    {
        this.data = data;
        this.leftChild = null;
        this.rightChild = null;
        this.width = 60;
        this.height = 80;
        this.middle = 30;
    }

    Node(int data, char character)
    {
        this.data = data;
        this.character = character;
        this.leftChild = null;
        this.rightChild = null;
        this.width = 60;
        this.height = 80;
        this.middle = 30;
    }

    public int getData()
    {
        return this.data;
    }

    public Node getLeftChild()
    {
        return this.leftChild;
    }

    public Node getRightChild()
    {
        return this.rightChild;
    }

    public char getCharacter()
    {
        return this.character;
    }

    public int getWidth()
    {
        return this.width;
    }

    public int getHeight()
    {
        return this.height;
    }

    public int getMiddle()
    {
        return this.middle;
    }

    public void setLeftChild(Node node)
    {
        this.leftChild = node;
    }

    public void setRightChild(Node node)
    {
        this.rightChild = node;
    }

    public void setWidth(int width) { this.width = width; }

    public void setHeight(int height) { this.height = height; }

    public void setMiddle(int middle) { this.middle = middle; }
}