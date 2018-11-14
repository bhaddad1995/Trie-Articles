// Blake Haddad & Andy Nguyen
// CS-570
// Tries Assignment

import java.util.Vector;
import java.io.*;
import java.util.Scanner;
import java.util.*;

class trieNode{
    public Vector<trieNode> subNodes = new Vector<trieNode>();
    public char letter;

    trieNode(){

    }

    public String input = "";

    trieNode(char l){
        this.letter = l;
    }

    public Vector<trieNode> getSubNodes(){
        return this.subNodes;
    }

    public void setLetter(char letter){
        this.letter = letter;
    }

    public char getLetter(){
        return this.letter;
    }

    public trieNode addSubNode(trieNode node){
        int l = subNodes.size();
        int index = 0;
        if(l == 0){
            subNodes.add(node);
        }else{
            for(int i=0; i<l; i++){
                if((int) subNodes.get(i).getLetter() > (int) node.getLetter()){
                    if(i == 0){
                        subNodes.insertElementAt(node, 0);
                        index = 0;
                        break;
                    }else if(i == l-1){
                        subNodes.add(node);
                        index = l;
                        break;
                    }else{
                        subNodes.insertElementAt(node, i);
                        index = i;
                        break;
                    }
                }else if(i == l-1){
                    subNodes.add(node);
                    index = l;
                }
            }
        }
        return subNodes.get(index);
    }


    public void loadFile(){
        HashMap<String, String[]> companies = new HashMap<>();
        String[] splitNameSyn;
        String[] splitSyns;
        String compName = "";
        String syn = "";
        String curr = "";
        try(BufferedReader br = new BufferedReader(new FileReader("/Users/Andy/IdeaProjects/triesLab/src/companies.dat"))) {
            for(String line; (line = br.readLine()) != null; ) {
                splitNameSyn = line.split("    ");
                compName = splitNameSyn[0];
                splitSyns = splitNameSyn[1].split(" ");
                companies.put(compName,splitSyns);
            }
        } catch (IOException i) {
            System.err.print("Error loading file with exception:\n"+i);
            i.printStackTrace();
            System.exit(0);
        }
    }


    public trieNode searchSubNodes(char let){
        int numSubNodes = subNodes.size();
        for(int i=0; i<numSubNodes; i++){
            if(subNodes.get(i).getLetter() == let){
                return subNodes.get(i);
            }
            if((int) subNodes.get(i).getLetter() > (int) let){
                return null;
            }
        }
        return null;
    }

    public trieNode insertSubLetter(char let){
        trieNode subLetterNode = searchSubNodes(let);
        if(subLetterNode == null){
            trieNode newNode = new trieNode(let);
            subLetterNode = addSubNode(newNode);
        }
        return subLetterNode;
    }

    public void insertString(String s){
        int l = s.length();
        trieNode node = insertSubLetter(s.charAt(0));
        for(int i=1; i<l; i++){
            node = node.insertSubLetter(s.charAt(i));
        }
    }

    public void printTrieLevels(){
        int numSubNodes = subNodes.size();
        if(numSubNodes == 0){
            System.out.println(getLetter());
        }else{
            for(int i = 0; i<numSubNodes; i++){
                System.out.println(getLetter());
                trieNode node = subNodes.get(i);
                node.printTrieLevels();
            }
        }
        return;
    }

}



class trie{

    public trieNode root = new trieNode();

    public trieNode getRoot(){
        return this.root;
    }

    public void print(){
        root.printTrieLevels();
    }

    // public void printLeafNodes(treeNode root)
    // {
    //     // if node is null, return
    //     if (root == null){
    //         return;
    //     }

    //     // if node is leaf node, print its data
    //     if (root.getSubNodes().size() == 0){
    //         System.out.println("Leaf node " + root.getLetter() + " with weight of " + root.getValue() + "and huff code " + huffCode);
    //         return;
    //     }

    //     // if left child exists, check for leaf
    //     // recursively
    //     if (root.getLeftChild() != null){
    //         huffCode = huffCode + "0";
    //         printLeafNodes(root.getLeftChild());
    //         huffCode = huffCode.substring(0,huffCode.length()-1);
    //     }

    //     // if right child exists, check for leaf
    //     // recursively
    //     if (root.getRightChild() != null){
    //         huffCode = huffCode + "1";
    //         printLeafNodes(root.getRightChild());
    //         huffCode = huffCode.substring(0,huffCode.length()-1);
    //     }
    // }


}

class triesLab{

    public String input = "";
    public static void main(String[] args){
        triesLab trieObj = new triesLab();
        //trieObj.createTrie();
        trieNode trie = new trieNode();
        trie.loadFile();



    }


   /* public void loadFile(){
        try {
            String x;
            FileReader fileIn = new FileReader("company.dat");
            BufferedReader in = new BufferedReader(fileIn);
            x = in.readLine();
            while (x != null) {
                input = input + formatInput(x);
                x = in.readLine();
            }
            in.close();
            fileIn.close();

        } catch (IOException i) {
            System.err.print("Error loading game with exception:\n"+i);
            i.printStackTrace();
            System.exit(0);
        }
    }

    public String formatInput(String s){
        String formattedInput = "";
        for(int i = 0; i < s.length(); i++){
            if((int) s.charAt(i) == 9){
                formattedInput = formattedInput + s.charAt(i);
            }else{
                continue;
            }
        }
        return formattedInput;
    }*/


    public void createTrie(){
        trie trieObj = new trie();
        trieObj.getRoot().insertString("abx");
        trieObj.getRoot().insertString("abde");
        trieObj.getRoot().insertString("xyz");
        trieObj.print();
        return;
    }


}