package Questions;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;




public class Question {
	private static ArrayList<String> mathsQuestions = new ArrayList<String>();
	private static ArrayList<String> englishQuestions = new ArrayList<String>();
	
	//private static ArrayList<String> tempMathsQuestions = new ArrayList<String>();
	private static ArrayList<String> tempEnglishQuestions = new ArrayList<String>();
	

	/*public static void main(String[] args) {
		englishQuestions();
		mathsQuestions();
		///writeDocument();
		randomQuestionGenerator();
		}
		*/
		
	
	public Question() {
		englishQuestions();
		mathsQuestions();
	}
	
	public static void mathsQuestions() {
		try {
//			FileReader fr = null;
//			fr = new FileReader("C:\\Users\\randy\\Documents\\Los thunder game\\Game\\core\\assets\\mathsQuestions.txt");
			BufferedReader br = null;
			FileHandle fr= Gdx.files.internal("mathsQuestions.txt");
			br = new BufferedReader(new InputStreamReader(fr.read()));
			String mCurrentLine="";
			while((mCurrentLine = br.readLine()) != null) {
				mathsQuestions.add(mCurrentLine);
				}
			} catch (Exception e) {
				e.printStackTrace();
				}
	}
	
	
	public static void englishQuestions() {
	try {
		BufferedReader br = null;
//FileReader frMath = null;
//frMath = new FileReader("C:\\Users\\randy\\Documents\\Los thunder game\\Game\\core\\assets\\englishQuestions.txt");
//brEng = new BufferedReader(frMath);
		FileHandle fr= Gdx.files.internal("englishQuestions.txt");
		br = new BufferedReader(new InputStreamReader(fr.read()));
		
		String eCurrentLine="";
		while((eCurrentLine = br.readLine()) != null) {
			englishQuestions.add(eCurrentLine);
			}
		} catch (Exception e) {
			e.printStackTrace();
			}
	
		}
	
	
	public static ArrayList<String> randomQuestionGenerator() {
		///ramdom number generator
		Random r = new Random();
		int rand = r.nextInt((11))*7;
		ArrayList<String> tempQuestion = new ArrayList<String>();
		
	
		///Add random questions to maths temp array
		if (rand<42){
			for (int m = rand; m < rand+6; m++) {
				tempQuestion.add(mathsQuestions.get(m));
			}
			return tempQuestion;
		}else {
			for (int e = rand%42; e < (rand%42)+6; e++) {
				tempQuestion.add(englishQuestions.get(e));
			}
			return tempQuestion;
		}

		
		///Add random questions to english temp array
		
	}
	
/*
	public static void writeDocument() {
		try {
		PrintWriter writer = new PrintWriter("C:\\Users\\randy\\Downloads\\QuestionGenerator\\questions\\englishQuestions.txt", "UTF-8");
		writer.println("Line 1111");
		writer.close();
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (UnsupportedEncodingException e) {
		e.printStackTrace();
			}
		}
		
		*/

}
