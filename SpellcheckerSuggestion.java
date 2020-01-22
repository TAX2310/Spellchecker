import java.io.*;
import java.util.*;

public class SpellcheckerSuggestion {

	// a method that creates an array of words from the Dictionary
	public static void createWordArray(ArrayList<String> array, String path) throws IOException {

		Scanner wordScan = new Scanner( new File(path + "Dictionary.txt"));

		while(wordScan.hasNext())
		{
			String current = wordScan.next();
			array.add(current);
		}
		wordScan.close();
	}

	// a method that checks a given word to see if it is in the Dictionary
	public static boolean check(ArrayList<String> array, String word){
		for (int i = 0; i < array.size(); i++) {
			if (array.get(i).equals(word)) {
				return true;
			}
		}
		return false;
	}

	// a method that uses an encoded word areturns the indexes of any maching codes in the codes array
	public static ArrayList<String> suggestions(ArrayList<String> array, ArrayList<String> index, String word){

		for (int i = 0; i < array.size(); i++) {
			if (distance(word ,array.get(i)) < 3) {
				index.add(array.get(i));
			}
		}
		return index;
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
 
    public static int distance(String a, String b) {
        a = a.toLowerCase();
        b = b.toLowerCase();

        // this implamentation of the levenstine distance reprasents a matrix using a one dimentional array

        // creating a one dimentional array called costs at the length of sting b + this is becaus comparisons in the array start at 1
        int [] costs = new int [b.length() + 1];
        // this first loop fills the array with its indecies 
        for (int j = 0; j < costs.length; j++)
            costs[j] = j;
        // this this loop iterates through the string a and the matrix verticaly 
        for (int i = 1; i <= a.length(); i++) {
            // this initalises the current row of the matrix you are working on
            costs[0] = i;
            // this enitalises the variable that represents the value in the top left position
            int tl = i - 1;
            for (int j = 1; j <= b.length(); j++) {
            	// this sets cj to be the minimum of 3 valuse the first represents the value to the left of where you are in the matrix + 1 the second 
            	// represents above + 1 and the last is the top left diagonal wich if the carecters are the same returns the diagonal value else the diagonal value + 1
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), a.charAt(i - 1) == b.charAt(j - 1) ? tl : tl + 1);
                // this moves the value above to the top left diagonal position for when you next move through the array  
                tl = costs[j];
                // this initalises the new left position with the new value created in cj
                costs[j] = cj;
            }
        }
        // this returns the final value in the costs array witch represents the the final value in the matrix 
        return costs[b.length()];
    }

    // amethod that runs the spellcheker and handels the user interface 
    public static void Run(Scanner user, Scanner fileScan, PrintWriter outputWriter, ArrayList<String> words, ArrayList<String> index, String path) throws IOException {

    	// a while loop that iterates through the words in the input file
    	while(fileScan.hasNext()){
			String curentWord = fileScan.next();
			// a while loop that loops aslon as the curent word is not present in the dictionary 
			while(!check(words, curentWord)){
				System.out.println("incorect spelling found: " + curentWord);

				System.out.println("wold you like to add the word to the dictonary? (yes/no): ");
				String answer = user.next().trim();
				if (answer.equals("yes")) {
					// calling a method to add the curent word to the dictionary 
					Amend(curentWord);
					createWordArray(words, path);
				} else {
					System.out.println("sugested spellings:");
					// find indexes of sugestions 
					suggestions(words, index, curentWord);
					// print sugestions
					for (int i = 0; i < index.size(); i++) {
						System.out.println(index.get(i));
					}
					// this alows the user to enter another word 
					System.out.println("enter either a sugested spelling or a replacment word:");
					curentWord = user.next().trim();
				}
			}
			// this uses the print method to write the curent word to the output file once it has been found in the dictionary
			outputWriter.print(curentWord + " ");
		}

    }

	public static void main(String[] args) throws IOException {

		String path = "/Volumes/DATA/Users/taylor/Documents/Computer Science/year 2/Principles and Applications of Programming JAVA/courswork 2/";

		ArrayList<String> words = new ArrayList<String>();
		ArrayList<String> index = new ArrayList<String>();

		createWordArray(words, path);

		// creating a new scanner object for user input
		Scanner user = new Scanner(System.in);
		System.out.print("enter file name: ");
		String fileName = user.next().trim();

		// creating a new output file if one dosent already exsist
		File output = new File(path + "Spellchecked_" + fileName);
      	if (output.createNewFile()) {
        	System.out.println("File created: " + output.getName());
      	} else {
        	System.out.println("File already exists.");
      	}

      	// creating a new scanner to read the input file
		Scanner fileScan = new Scanner( new File(path + fileName));

		// creating a print writer object to write to the output file 
		PrintWriter outputWriter = new PrintWriter(output);

		Run(user, fileScan, outputWriter, words, index, path);

		System.out.println("spell checker finished");
		outputWriter.close();
		fileScan.close();
		user.close();
	}
}