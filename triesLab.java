// Blake Haddad & Andy Nguyen
// CS-570
// Tries Assignment

import java.util.Vector;
import java.io.*;
import java.util.Scanner;
import java.util.*;
import java.text.*;

class trieNode{
    public Vector<trieNode> subNodes = new Vector<trieNode>();
    public char letter;
    public boolean isEndOfWord;
    public int synonymBucket;

    trieNode(){

    }

    public String input = "";

    trieNode(char l, boolean end, int synBucket){
        this.letter = l;
        this.isEndOfWord = end;
        this.synonymBucket = synBucket;
    }

    public void setSynBucket(int bucket){
        this.synonymBucket = bucket;
    }

    public int getSynBucket(){
        return this.synonymBucket;
    }

    public Vector<trieNode> getSubNodes(){
        return this.subNodes;
    }

    public void setIsEndOfWord(boolean value){
        this.isEndOfWord = value;
    }

    public boolean getIsEndOfWord(){
        return this.isEndOfWord;
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

    public trieNode insertSubLetter(char let, boolean endOfWord, int synBucket){
        trieNode subLetterNode = searchSubNodes(let);
        if(subLetterNode == null){
            trieNode newNode = new trieNode(let, endOfWord, synBucket);
            subLetterNode = addSubNode(newNode);
        }
        return subLetterNode;
    }

    public void insertString(String s, int synBucket){
        int l = s.length();
        trieNode node = insertSubLetter(s.charAt(0), l == 1, synBucket);
        for(int i=1; i<l; i++){
            node = node.insertSubLetter(s.charAt(i),i==l-1,synBucket);
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
}

public class triesLab{
    String filename;
    public String input = "";
    public String filteredInput = "";
    public String article = "";
    public int wordCount;
    public int totalFoundWords = 0;
    public Vector<Vector<String>> companies = new Vector<Vector<String>>();
    public Vector<Integer> tallyMap = new Vector<Integer>();
    public Vector<Integer> sortedTallyMap = new Vector<Integer>();
    public Vector<String> sortedCompNames = new Vector<String>();
    public Vector<String> blackListWords = new Vector<String>();
    public trie trieObj = new trie();
    public trie blackListTrie = new trie();
    DecimalFormat df = new DecimalFormat("#.####");
    

    public static void main(String[] args){
        triesLab trieAssign = new triesLab();
        trieAssign.loadFileName();
        trieAssign.loadFile();
        trieAssign.loadArticle();
        trieAssign.filterAndCountInput();
        trieAssign.createTrie();
        trieAssign.initializeTallyMap();
        trieAssign.search();
        trieAssign.sortOutput();
        trieAssign.printResults();
    }

    public void filterAndCountInput(){
        String s = input;
        filteredInput = s.replaceAll("[^a-zA-Z0-9 ]", "");//.toLowerCase().split("\\s+");
        String words = removeBlacklistWords(filteredInput);
        wordCount = countWordsUsingSplit(words);
    }

    public int countWordsUsingSplit(String input){ 
        if (input == null || input.isEmpty()) 
        {
            return 0;
        }
        String[] words = input.split("\\s+");
        return words.length;
    }

    public String removeBlacklistWords(String message) {
        List<String> badWords = Arrays.asList( "a", "an", "and", "the", "or", "but" );
        for ( String badWord : badWords ) {
            message = message.replaceAll("(?i)\\b[^\\w -]*" + badWord + "[^\\w -]*\\b", "");
        }
        return message;
    }

    public void loadArticle(){
        try {
            String x;
            FileReader fileIn = new FileReader(filename);
            BufferedReader in = new BufferedReader(fileIn);
            x = in.readLine();
            while (x != null && !x.equals(".")) {
                input = input + x;
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

    public void loadFileName(){
        try{
            Scanner reader = new Scanner(System.in);
            System.out.print("Enter an article filename within current direcotry (please include file extension): ");
            filename = reader.nextLine(); // Scans the next token of the input.
            while(filename.isEmpty()){
                filename = reader.nextLine();
            }
        }catch(Exception e){
                System.err.print("Error loading game with exception:\n"+e);
                System.exit(0);
        }
    }
    public void loadFile(){
        String[] splitNameSyn;
        String[] splitSyns;
        String compName = "";
        String syn = "";
        String curr = "";
        try(BufferedReader br = new BufferedReader(new FileReader("company.dat"))) {
            for(String line; (line = br.readLine()) != null; ) {
                if(line.equals(".")){
                    break;
                }
                splitNameSyn = line.split("\t");
                Vector<String> companyNamesVector = new Vector<String>(Arrays.asList(splitNameSyn));
                //compName = splitNameSyn[0];
                //splitSyns = splitNameSyn[1].split(" ");
                companies.add(companyNamesVector);
                //companies.put(compName,splitSyns);
            }
        } catch (IOException i) {
            System.err.print("Error loading file with exception:\n"+i);
            i.printStackTrace();
            System.exit(0);
        }
    }


    public void createTrie(){
        for(int i = 0; i<companies.size(); i++){
            for(int y = 0; y<companies.get(i).size();y++){
                trieObj.getRoot().insertString(companies.get(i).get(y).replaceAll("[^a-zA-Z0-9 ]", ""),i);
            }
        }
        //trieObj.print();
        return;
    }

    public void initializeTallyMap(){
        int l = companies.size();
        for(int i = 0; i<l; i++){
            tallyMap.add(i, 0);
        }
    }

    public void search(){
        //String test = "The company Microsoft Inc competed with Apple today, and MicroSoft came out on top";
        int length = filteredInput.length();
        trieNode currentNode = trieObj.getRoot();
        String foundCompanyName = "";
        for(int i = 0; i < length; i++){
            currentNode = currentNode.searchSubNodes(filteredInput.charAt(i));
            if(currentNode == null || filteredInput.charAt(i) == '\n'){
                currentNode = trieObj.getRoot();
                foundCompanyName = "";
            }else if(currentNode != null){
                foundCompanyName = foundCompanyName + currentNode.getLetter();
                if(currentNode.getIsEndOfWord() == true){
                    //System.out.println("Found company name: " + foundCompanyName);
                    tallyMap.set(currentNode.getSynBucket(), tallyMap.get(currentNode.getSynBucket())+1);
                    foundCompanyName = "";
                }

            }
        }

    }

    public void printResults(){
        printTallyMap();
        System.out.println("---------------");
        System.out.println("Total: " + totalFoundWords + " | " + df.format(100*(double)totalFoundWords/(double)wordCount) + "%");
        System.out.println("Total Words: " + wordCount);
    }

    public void printTallyMap(){
        int l = sortedTallyMap.size();
        System.out.println("Company | Hit Count | Relevance");
        for(int i = 0; i<l; i++){
            if(sortedTallyMap.get(i)==0){
                return;
            }
            double relevance = 100*(double)sortedTallyMap.get(i)/(double)wordCount;
            System.out.println(sortedCompNames.get(i) + " | " + sortedTallyMap.get(i) + " | " + df.format(relevance) + "%");
            totalFoundWords = totalFoundWords + sortedTallyMap.get(i);
        }
    }

    public void sortOutput(){
        Vector<Integer> tallyMapCopy = (Vector<Integer>)tallyMap.clone();
        Vector<Vector<String>> companyNamesCopy = (Vector<Vector<String>>)companies.clone();
        while(!tallyMapCopy.isEmpty()){
            int tempMaxIndex = 0;
            for(int i = 1; i<tallyMapCopy.size(); i++){
                if(tallyMapCopy.get(i)>tallyMapCopy.get(tempMaxIndex)){
                    tempMaxIndex = i;
                }
            }
            sortedTallyMap.add(tallyMapCopy.get(tempMaxIndex));
            sortedCompNames.add(companyNamesCopy.get(tempMaxIndex).get(0));
            tallyMapCopy.remove(tempMaxIndex);
            companyNamesCopy.remove(tempMaxIndex);
        }
        // System.out.println(sortedTallyMap);
        // System.out.println(sortedCompNames);
    }

}