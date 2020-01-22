package streams;
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

	// a method that creates an array of sondex encoded words from the Dictionary that is in exactly the same order as the words array.
	public static void createCodeArray(ArrayList<String> array, String path) throws IOException {

		Scanner codeScan = new Scanner( new File(path + "Dictionary.txt"));

		while(codeScan.hasNext())
		{
			String current = codeScan.next();
			array.add(soundex(current));
		}
		codeScan.close();
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
	public static ArrayList<Integer> suggestions(ArrayList<String> array, ArrayList<Integer> index, String code){

		for (int i = 0; i < array.size(); i++) {
			if (array.get(i).equals(code)) {
				index.add(i);
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

	// a function to encode words using sondex
	public static String soundex(String s) { 
        char[] x = s.toUpperCase().toCharArray();
        char firstLetter = x[0];

        // convert letters to numeric code
        for (int i = 0; i < x.length; i++) {
            switch (x[i]) {

                case 'B':
                case 'F':
                case 'P':
                case 'V':
                    x[i] = '1';
                    break;

                case 'C':
                case 'G':
                case 'J':
                case 'K':
                case 'Q':
                case 'S':
                case 'X':
                case 'Z':
                    x[i] = '2';
                    break;

                case 'D':
                case 'T':
                    x[i] = '3';
                    break;

                case 'L':
                    x[i] = '4';
                    break;

                case 'M':
                case 'N':
                    x[i] = '5';
                    break;

                case 'R':
                    x[i] = '6';
                    break;

                default:
                    x[i] = '0';
                    break;
            }
        }

        // remove duplicates
        String output = "" + firstLetter;
        for (int i = 1; i < x.length; i++)
            if (x[i] != x[i-1] && x[i] != '0')
                output += x[i];

        // pad with 0's or truncate
        output = output + "0000";
        return output.substring(0, 4);
    }

    // amethod that runs the spellcheker 
    public static void Run(Scanner user, Scanner fileScan, PrintWriter outputWriter, ArrayList<String> words, ArrayList<String> codes, ArrayList<Integer> index, String path) throws IOException {

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
					createCodeArray(codes, path);
				} else {
					System.out.println("sugested spellings:");
					// find indexes of sugestions 
					suggestions(codes, index, soundex(curentWord));
					// print sugestions
					for (int i = 0; i < index.size(); i++) {
						System.out.println(words.get(index.get(i)));
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
		ArrayList<String> codes = new ArrayList<String>();
		ArrayList<Integer> index = new ArrayList<Integer>();

		createWordArray(words, path);
		createCodeArray(codes, path);

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

		Run(user, fileScan, outputWriter, words, codes, index, path);

		System.out.println("spell checker finished");
		outputWriter.close();
		fileScan.close();
		user.close();
	}
}