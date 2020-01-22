import java.io.*;
import java.util.*;

public class Spellchecker {

	// this method checks a word against a dictionary file
	public static boolean Check(String word) throws IOException {
		String path = "/Volumes/DATA/Users/taylor/Documents/Computer Science/year 2/Principles and Applications of Programming JAVA/courswork 2/";
		// creating a new scanner object to read the dictionary file
		Scanner dictionaryScan = new Scanner( new File(path + "Dictionary.txt"));
		// a while loop check if the word apears in the dictionary file
		while(dictionaryScan.hasNext())
		{
			String current = dictionaryScan.next();
			if (current.equals(word)) {
				return true;
			}
		}
		return false;
	}

	// this method adds a new word to the dictionary fill and reordrs it
	public static void Amend(String word) throws IOException {
		String path = "/Volumes/DATA/Users/taylor/Documents/Computer Science/year 2/Principles and Applications of Programming JAVA/courswork 2/";
		File dictionaryAmend = new File(path + "Dictionary.txt");

		// creating a new scanner object to read the dictionary file
		Scanner scanD = new Scanner( new File(path + "Dictionary.txt"));

		// creating a treeset object to sort the words in the dictionary file
		Set<String> dictionary = new TreeSet<String>();
		// a while loop that adds all the words from the dictionary file to the treeset
		while(scanD.hasNext())
		{
			dictionary.add(scanD.next());
		}
		//this adds the new word to the treeset
		dictionary.add(word);

		// this turns the tree set in to  an array
		Object[] arr = dictionary.toArray();

		// creating a print writer object to overwrite the dictionary file
		PrintWriter print = new PrintWriter(dictionaryAmend);

		// for loop to print each element of the array to the dictionary file
		for (int i = 0; i < arr.length; i++) {
			print.println(arr[i]);
		}
		
		print.close();

	}

	public static void main(String[] args) throws IOException {
		
		// defining the path to the folder i am using
		String path = "/Volumes/DATA/Users/taylor/Documents/Computer Science/year 2/Principles and Applications of Programming JAVA/courswork 2/";

		// creating a new scanner object for user input
		Scanner user = new Scanner(System.in);
		System.out.print("enter file name: ");
		String fileName = user.next().trim();
		// creating a new scanner to read the input file
		Scanner scan = new Scanner( new File(path + fileName));

		// creating a new output file if one dosent already exsist 
      	File output = new File(path + "Spellchecked_" + fileName);
      	if (output.createNewFile()) {
        	System.out.println("File created: " + output.getName());
      	} else {
        	System.out.println("File already exists.");
      	}

      	// creating a print writer object to write to the output file 
    	PrintWriter myWriter = new PrintWriter(output);

    	// a while loop that iterates through the words in the input file
		while(scan.hasNext())
		{
			String curentWord = scan.next();

			// a while loop that loops aslon as the curent word is not present in the dictionary 
			while(!Check(curentWord)){
				System.out.println("incorect spelling found: " + curentWord);
				System.out.println("wold you like to add the word to the dictonary? (yes/no): ");
				String answer = user.next().trim();
				if (answer.equals("yes")) {
					// calling a method to add the curent word to the dictionary 
					Amend(curentWord);
				} else {
					// this alows the user to try a diforent word that will also be checked against the dictionary
					System.out.println("please enter a replacment for " + curentWord + " : ");
					curentWord = user.next().trim();
					}
			}
			// this uses the print method to write the curent word to the output file once it has been found in the dictionary
			myWriter.print(curentWord + " ");	
		}
		System.out.println("spell checker finished");
		myWriter.close();
		scan.close();
		user.close();
	}
}