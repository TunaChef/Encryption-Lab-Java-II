package encryption;

import java.io.*;
import java.util.Scanner;

/**
 * Encryption - Class to encrypt string with basic rules
 *
 * @author Ryon Edwards
 * @copyright Howard Community College 2025
 * @version 2.0
 */

public class Encryptor {

	public static String encryptWord(String word) {
		if(word == null || word.isEmpty()) {
			throw new IllegalArgumentException("Word cannot be null or empty.");
		}
		
		int start = 0;
		int end = word.length() - 1;
		
		//two while loops
		//First finds the start index of the word by ensuring it is not a special character, and remains in the word index
		while (start < word.length() && !Character.isLetterOrDigit(word.charAt(start))) {
            start++;
        }
		//second while makes sure the index isn't zero and also not a special character
        while (end >= 0 && !Character.isLetterOrDigit(word.charAt(end))) {
            end--;
        }
        
        // Get prefix, suffix, and core parts of the word (without starting/ending special chars)
        String prefix = word.substring(0, start);
        String core = (start <= end) ? word.substring(start, end + 1) : "";
        String suffix = (end + 1 < word.length()) ? word.substring(end + 1) : "";

        if(core.isEmpty()) {
        	// nothing to encrypt, return
            return word;
        }
        
		//If word is a number
		if(core.matches("\\d+")) {
			int number = Integer.parseInt(core);
			return prefix + (number + 2501) + suffix;
		}
		
		//If word is alphabetic
		else {
			core = core.toLowerCase();
			int length = core.length();
			
			//Single letter case
			if(length == 1) {
				
				char ch = core.charAt(0);
				if(Character.isLetter(ch)) {
					//uses Unicode to subtract the value of the character with 'a' and go forward (+1), then using modulo to wrap around after z and then re-add the unicode value
					char nextLetter = (char)((ch - 'a' + 1) % 26 + 'a');
					return prefix + nextLetter + suffix;
				}
				return prefix + core + suffix; //leave symbol as is
			}
			
			//Even length word with modular division
			if(length % 2 == 0) {
				StringBuilder enc = new StringBuilder();
				
				for(int i = 0; i < length; i+=2) { 	//for loop iterates by 2 to go to each pairing of two letters and then switches them
					enc.append(core.charAt(i + 1));	//append the forward letter
					enc.append(core.charAt(i));		//then add the initial letter
				}
				
				return prefix + enc.toString() + suffix;	//return the string
			}
			else {
				return prefix + core.charAt(length - 1) + core.substring(1, length - 1) + core.charAt(0) + suffix;
			}
		}
	}
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Welcome to the file encryptor!");
		
		boolean keepRunning = true;
		while(keepRunning) {//use boolean to keep main encryption while loop
			
			 System.out.println("\nWould you like to encrypt a file? (yes/no)");
	            String choice = scanner.nextLine().trim().toLowerCase();	//ask if they want to encrypt a file and take input

	            if (!choice.matches(".*(yes|yea|yeah|mhm|please|sure).*")) { //It returns >:)
	                keepRunning = false;
	                break;
	            }

	            // Get input file
	            File inputFile = null;		//initialize input file
	            boolean fileFound = false;	//boolean for while loop
	            while (!fileFound) {
	                System.out.println("Enter the name of the input file:");	//ask for input
	                String inputName = scanner.nextLine().trim();				//save input
	                inputFile = new File(inputName);							//open file
	                if (inputFile.exists() && inputFile.isFile()) {				//see if file exists sand is a file and return true
	                    fileFound = true;
	                    break;
	                }
	                System.out.println("File not found. Please try again.");	//repeat cycle if not a file
	            }

	            // Get output file
	            File outputFile = null;			//initialize output file
	            boolean fileCreated = false;	//while boolean
	            while (!fileCreated) {
	                System.out.println("Enter the name of the output file:");
	                String outputName = scanner.nextLine().trim();	//get output file 
	                outputFile = new File(outputName);				//create it!

	                try {	//try-catch block to create the file and see if exists, if not then throw error
	                    if (outputFile.createNewFile() || outputFile.exists()) {
	                        break;
	                    }
	                } catch (IOException e) {
	                    System.out.println("Could not create output file. Try again.");
	                }
	            }

	            // Process file
	            try (
	            	//Buffered Reader and Writer to read from and write to the respective files
	                BufferedReader reader = new BufferedReader(new FileReader(inputFile));
	                BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))
	            ) {
	                String line;
	                while ((line = reader.readLine()) != null) {	//process through each line in while loop until null
	                    String[] words = line.split("\\s+");		//split each word
	                    //Use string builder to encrypt all words in file
	                    StringBuilder encryptedLine = new StringBuilder();

	                    for (String word : words) { //for every word in list, encrypt each one and add to string builder
	                        encryptedLine.append(encryptWord(word)).append(" ");
	                    }

	                    writer.write(encryptedLine.toString().trim());	//write the string builder to output file
	                    writer.newLine();								//with new line
	                }
	                System.out.println("File successfully encrypted to " + outputFile.getName());
	            } catch (IOException e) {
	                System.out.println("Error processing files: " + e.getMessage());
	            }
	        }

	        System.out.println("\nThank you for using the file encryptor! Bye");
	        scanner.close();
	    }

}
