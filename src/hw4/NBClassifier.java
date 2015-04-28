package hw4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class NBClassifier extends Classifier {
	
	/*
	 * 
	 * we need
	 * probability that you have capital gains, given that you make over 50k (over 50k & CG / CG)
	 * probability that you do not have capital gains, given that you make over 50k (over 50k & noCG / noCG)
	 * probability that you have capital gains, given that you do not make over 50k (under50K & CG / CG)
	 * probability that you do not have capital gains, given that you do not make over 50k (under50K & noCG / noCG)
	 * 
	 */

	private String nameFile;
	private double probG50;
	private double probL50;
	public HashMap<String, Double> workClassOver50Map;
	public HashMap<String, Double> educationOver50Map;
	public HashMap<String, Double> maritalStatusOver50Map;
	public HashMap<String, Double> occupationOver50Map;
	public HashMap<String, Double> relationshipOver50Map;
	public HashMap<String, Double> raceOver50Map;
	public HashMap<String, Double> sexOver50Map;
	public HashMap<String, Double> nativeCountryOver50Map;
	public HashMap<String, Double> workClassUnder50Map;
	public HashMap<String, Double> educationUnder50Map;
	public HashMap<String, Double> maritalStatusUnder50Map;
	public HashMap<String, Double> occupationUnder50Map;
	public HashMap<String, Double> relationshipUnder50Map;
	public HashMap<String, Double> raceUnder50Map;
	public HashMap<String, Double> sexUnder50Map;
	public HashMap<String, Double> nativeCountryUnder50Map;
	public double probOfCGgivenOver50;
	public double probOfNoCGgivenOver50;
	public double probOfCGgivenUnder50;
	public double probOfNoCGgivenUnder50;
	public final double SCALE = 0.0001;

	public NBClassifier(String namesFilepath) {
		super(namesFilepath);
		this.nameFile = namesFilepath;
		this.probG50 = 0;
		this.probL50 = 0;
		this.workClassOver50Map = new HashMap<String, Double>();
		this.educationOver50Map = new HashMap<String, Double>();
		this.maritalStatusOver50Map = new HashMap<String, Double>();
		this.occupationOver50Map = new HashMap<String, Double>();
		this.relationshipOver50Map = new HashMap<String, Double>();
		this.raceOver50Map = new HashMap<String, Double>();
		this.sexOver50Map = new HashMap<String, Double>();
		this.nativeCountryOver50Map = new HashMap<String, Double>();
		this.workClassUnder50Map = new HashMap<String, Double>();
		this.educationUnder50Map = new HashMap<String, Double>();
		this.maritalStatusUnder50Map = new HashMap<String, Double>();
		this.occupationUnder50Map = new HashMap<String, Double>();
		this.relationshipUnder50Map = new HashMap<String, Double>();
		this.raceUnder50Map = new HashMap<String, Double>();
		this.sexUnder50Map = new HashMap<String, Double>();
		this.nativeCountryUnder50Map = new HashMap<String, Double>();
		this.probOfCGgivenOver50 = 0.0;
		this.probOfCGgivenUnder50 = 0.0;
		this.probOfNoCGgivenOver50 = 0.0;
		this.probOfNoCGgivenUnder50 = 0.0;
	}

	public void setProbG50(double probG50) {
		this.probG50 = probG50;
	}

	public void setProbL50(double probL50) {
		this.probL50 = probL50;
	}

	public double getProbG50() {
		return probG50;
	}

	public double getProbL50() {
		return probL50;
	}

	@Override
	public void train(String trainingDataFilpath) {

		Scanner trainer = null;
		int o50 = 0; // instances of people making more than 50k
		int u50 = 0; // instances of people making less than 50k
		int count = 0; // number of training cases
		int Over50AndCG = 0;
		int over50AndNoCG = 0;
		int under50AndCG = 0;
		int under50AndNoCG = 0;
		int CG = 0;
		int noCG = 0;
		
		try {
			trainer = new Scanner(new File(trainingDataFilpath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (trainer.hasNextLine()) {
			count++;
			String[] line = trainer.nextLine().split(" ");
			// System.out.println(line[line.length-1]);
			if (line[line.length - 1].equals(">50K"))
				o50++;
			else if (line[line.length - 1].equals("<=50K"))
				u50++;
			
			if (line[line.length-1].equals(">50K") && Integer.parseInt(line[9])>0)
				Over50AndCG++;
			else if (line[line.length-1].equals(">50K") && Integer.parseInt(line[9]) == 0) // over 50K and no CG
				over50AndNoCG++;
			
			if (Integer.parseInt(line[9])>0)
				CG++;
			else if (Integer.parseInt(line[9]) == 0) // don't make CG
				noCG++;
			
			if (line[line.length-1].equals("<=50K") && Integer.parseInt(line[9]) > 0) // under 50 and CG
				under50AndCG++;
			else if (line[line.length-1].equals("<=50K") && Integer.parseInt(line[9]) == 0) // under 50 and no CG
				under50AndNoCG++;

			String workClass = line[1];
			String education = line[2];
			String maritalStatus = line[4];
			String occupation = line[5];
			String relationship = line[6];
			String race = line[7];
			String sex = line[8];
			String nativeCountry = line[12];

			if (line[line.length - 1].equals(">50K")) {
				if (workClassOver50Map.containsKey(workClass))
					workClassOver50Map.put(workClass,
							workClassOver50Map.get(workClass) + 1.0);
				else
					workClassOver50Map.put(workClass, 1.0);

				if (educationOver50Map.containsKey(education))
					educationOver50Map.put(education,
							educationOver50Map.get(education) + 1.0);
				else
					educationOver50Map.put(education, 1.0);

				if (maritalStatusOver50Map.containsKey(maritalStatus))
					maritalStatusOver50Map.put(maritalStatus,
							maritalStatusOver50Map.get(maritalStatus) + 1.0);
				else
					maritalStatusOver50Map.put(maritalStatus, 1.0);

				if (occupationOver50Map.containsKey(occupation))
					occupationOver50Map.put(occupation,
							occupationOver50Map.get(occupation) + 1.0);
				else
					occupationOver50Map.put(occupation, 1.0);

				if (relationshipOver50Map.containsKey(relationship))
					relationshipOver50Map.put(relationship,
							relationshipOver50Map.get(relationship) + 1.0);
				else
					relationshipOver50Map.put(relationship, 1.0);

				if (raceOver50Map.containsKey(race))
					raceOver50Map.put(race, raceOver50Map.get(race) + 1.0);
				else
					raceOver50Map.put(race, 1.0);

				if (sexOver50Map.containsKey(sex))
					sexOver50Map.put(sex, sexOver50Map.get(sex) + 1.0);
				else
					sexOver50Map.put(sex, 1.0);

				if (nativeCountryOver50Map.containsKey(nativeCountry))
					nativeCountryOver50Map.put(nativeCountry,
							nativeCountryOver50Map.get(nativeCountry) + 1.0);
				else
					nativeCountryOver50Map.put(nativeCountry, 1.0);

			}
			
			if (line[line.length - 1].equals("<=50K")) {
				if (workClassUnder50Map.containsKey(workClass))
					workClassUnder50Map.put(workClass,
							workClassUnder50Map.get(workClass) + 1.0);
				else
					workClassUnder50Map.put(workClass, 1.0);

				if (educationUnder50Map.containsKey(education))
					educationUnder50Map.put(education,
							educationUnder50Map.get(education) + 1.0);
				else
					educationUnder50Map.put(education, 1.0);

				if (maritalStatusUnder50Map.containsKey(maritalStatus))
					maritalStatusUnder50Map.put(maritalStatus,
							maritalStatusUnder50Map.get(maritalStatus) + 1.0);
				else
					maritalStatusUnder50Map.put(maritalStatus, 1.0);

				if (occupationUnder50Map.containsKey(occupation))
					occupationUnder50Map.put(occupation,
							occupationUnder50Map.get(occupation) + 1.0);
				else
					occupationUnder50Map.put(occupation, 1.0);

				if (relationshipUnder50Map.containsKey(relationship))
					relationshipUnder50Map.put(relationship,
							relationshipUnder50Map.get(relationship) + 1.0);
				else
					relationshipUnder50Map.put(relationship, 1.0);

				if (raceUnder50Map.containsKey(race))
					raceUnder50Map.put(race, raceUnder50Map.get(race) + 1.0);
				else
					raceUnder50Map.put(race, 1.0);

				if (sexUnder50Map.containsKey(sex))
					sexUnder50Map.put(sex, sexUnder50Map.get(sex) + 1.0);
				else
					sexUnder50Map.put(sex, 1.0);

				if (nativeCountryUnder50Map.containsKey(nativeCountry))
					nativeCountryUnder50Map.put(nativeCountry,
							nativeCountryUnder50Map.get(nativeCountry) + 1.0);
				else
					nativeCountryUnder50Map.put(nativeCountry, 1.0);

			}

			// System.out.println(workClass + " " + education + " " +
			// maritalStatus + " " + occupation + " " + relationship + " " +
			// race + " " + sex + " " + nativeCountry);
		}

		// inside the loop, the maps hold the count of how many times each
		// discrete class was seen
		// here we update the maps to hold the probability of seeing that class
		for (String workClass : workClassOver50Map.keySet())
			workClassOver50Map.put(workClass, workClassOver50Map.get(workClass)
					/ (double) count);
		for (String education : educationOver50Map.keySet())
			educationOver50Map.put(education, educationOver50Map.get(education)
					/ (double) count);
		for (String maritalStatus : maritalStatusOver50Map.keySet())
			maritalStatusOver50Map.put(maritalStatus,
					maritalStatusOver50Map.get(maritalStatus) / (double) count);
		for (String occupation : occupationOver50Map.keySet())
			occupationOver50Map.put(occupation,
					occupationOver50Map.get(occupation) / (double) count);
		for (String relationship : relationshipOver50Map.keySet())
			relationshipOver50Map.put(relationship,
					relationshipOver50Map.get(relationship) / (double) count);
		for (String race : raceOver50Map.keySet())
			raceOver50Map.put(race, raceOver50Map.get(race) / (double) count);
		for (String sex : sexOver50Map.keySet())
			sexOver50Map.put(sex, sexOver50Map.get(sex) / (double) count);
		for (String nativeCountry : nativeCountryOver50Map.keySet())
			nativeCountryOver50Map.put(nativeCountry,
					nativeCountryOver50Map.get(nativeCountry) / (double) count);
		
		for (String workClass : workClassUnder50Map.keySet())
			workClassUnder50Map.put(workClass, workClassUnder50Map.get(workClass)
					/ (double) count);
		for (String education : educationUnder50Map.keySet())
			educationUnder50Map.put(education, educationUnder50Map.get(education)
					/ (double) count);
		for (String maritalStatus : maritalStatusUnder50Map.keySet())
			maritalStatusUnder50Map.put(maritalStatus,
					maritalStatusUnder50Map.get(maritalStatus) / (double) count);
		for (String occupation : occupationUnder50Map.keySet())
			occupationUnder50Map.put(occupation,
					occupationUnder50Map.get(occupation) / (double) count);
		for (String relationship : relationshipUnder50Map.keySet())
			relationshipUnder50Map.put(relationship,
					relationshipUnder50Map.get(relationship) / (double) count);
		for (String race : raceUnder50Map.keySet())
			raceUnder50Map.put(race, raceUnder50Map.get(race) / (double) count);
		for (String sex : sexUnder50Map.keySet())
			sexUnder50Map.put(sex, sexUnder50Map.get(sex) / (double) count);
		for (String nativeCountry : nativeCountryUnder50Map.keySet())
			nativeCountryUnder50Map.put(nativeCountry,
					nativeCountryUnder50Map.get(nativeCountry) / (double) count);

		// double prob = 0.0;
		// for (String m : maritalStatusMap.keySet()) {
		// System.out.println("The probability of marital status " + m + " is: "
		// + maritalStatusMap.get(m));
		// prob += maritalStatusMap.get(m);
		// }
		// System.out.println(prob);

//		System.out.println(o50 + " make over 50k");
//		System.out.println(u50 + " make under 50k");
		this.setProbG50((double) o50 / (double) count);
		this.setProbL50((double) u50 / (double) count);
		// System.out.println("over 50 " + this.getProbG50());
		// System.out.println("under 50 " + this.getProbL50());
		
//		System.out.println(Over50AndCG);
//		System.out.println(CG);
		
		probOfCGgivenOver50 = (double)Over50AndCG / (double)CG;
		probOfNoCGgivenOver50 = (double)over50AndNoCG / (double)noCG;
		probOfCGgivenUnder50 = (double)under50AndCG / (double)CG;
		probOfNoCGgivenUnder50 = (double)under50AndNoCG / (double)noCG;
		
		
//		System.out.println(this.probOfCGgivenOver50 + " prob of having CG given that you make over 50");
//		System.out.println(this.probOfNoCGgivenOver50 + " prob of NO CG given that you make over 50");
//		System.out.println(this.probOfCGgivenUnder50 + " prob of CG given you make under 50");
//		System.out.println(this.probOfNoCGgivenUnder50 + " prob of NO CG given you make under 50");
		

	}

	@Override
	public void makePredictions(String testDataFilepath) {

		Scanner tester = null;

		try {
			tester = new Scanner(new File(testDataFilepath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int correct = 0;
		int wrong = 0;
		int total = 0;
		while (tester.hasNextLine()) {
			total++;
			String[] line = tester.nextLine().split(" ");
			double[] values = predict(line);
			// the probability that they make over 50k is the first value, under 50k is the second
			if (values[0] > values[1]) {
				System.out.print(">50K");
				if (line[line.length-1].equals(">50K"))
					correct++;
				else
					wrong++;
			} else {
				System.out.print("<=50K");
				if (line[line.length-1].equals("<=50K"))
					correct++;
				else
					wrong++;
			}
			
			System.out.println("\t " + line[line.length-1]);
		}
		
		System.out.println(correct + " correct predictions");
		System.out.println(wrong + " incorrect predictions");
		double accuracy = (double) correct / (double) total;
		accuracy *= 100.0;
		System.out.println(accuracy + "% accuracy");
		

	}

	public double[] predict(String[] line) {

		double[] predictions = { 0.0, 0.0 };

		String workClass = line[1];
		String education = line[2];
		String maritalStatus = line[4];
		String occupation = line[5];
		String relationship = line[6];
		String race = line[7];
		String sex = line[8];
		int CG = Integer.parseInt(line[9]);
		String nativeCountry = line[12];
		
		double wCOver50 = (workClassOver50Map.containsKey(workClass)) ? workClassOver50Map.get(workClass) : SCALE;
		double wCUnder50 = (workClassUnder50Map.containsKey(workClass)) ? workClassUnder50Map.get(workClass) : SCALE;
		
		double eOver50 = (educationOver50Map.containsKey(education)) ? educationOver50Map.get(education) : SCALE;
		double eUnder50 = (educationUnder50Map.containsKey(education)) ? educationUnder50Map.get(education) : SCALE;
		
		double mSOver50 = (maritalStatusOver50Map.containsKey(maritalStatus)) ? maritalStatusOver50Map.get(maritalStatus) : SCALE;
		double mSUnder50 = (maritalStatusUnder50Map.containsKey(maritalStatus)) ? maritalStatusUnder50Map.get(maritalStatus) : SCALE;
		
		double oOver50 = (occupationOver50Map.containsKey(occupation)) ? occupationOver50Map.get(occupation) : SCALE;
		double oUnder50 = (occupationUnder50Map.containsKey(occupation)) ? occupationUnder50Map.get(occupation) : SCALE;
		
		double relOver50 = (relationshipOver50Map.containsKey(relationship)) ? relationshipOver50Map.get(relationship) : SCALE;
		double relUnder50 = (relationshipUnder50Map.containsKey(relationship)) ? relationshipUnder50Map.get(relationship) : SCALE;
		
		double rOver50 = (raceOver50Map.containsKey(race)) ? raceOver50Map.get(race) : SCALE;
		double rUnder50 = (raceUnder50Map.containsKey(race)) ? raceUnder50Map.get(race) : SCALE;
		
		double sOver50 = (sexOver50Map.containsKey(sex)) ? sexOver50Map.get(sex) : SCALE;
		double sUnder50 = (sexUnder50Map.containsKey(sex)) ? sexUnder50Map.get(sex) : SCALE;
		
		double nCOver50 = (nativeCountryOver50Map.containsKey(nativeCountry)) ? nativeCountryOver50Map.get(nativeCountry) : SCALE;
		double nCUnder50 = (nativeCountryUnder50Map.containsKey(nativeCountry)) ? nativeCountryUnder50Map.get(nativeCountry) : SCALE;
		
		double CGfactorOver50 = (CG > 0) ? probOfCGgivenOver50 : probOfNoCGgivenOver50;
		double CGfactorUnder50 = (CG == 0) ? probOfCGgivenUnder50 : probOfNoCGgivenUnder50;

		double pOver50 = this.getProbG50()
				* wCOver50
				* eOver50
				* mSOver50
				* oOver50
				* relOver50
				* rOver50
				* sOver50
				* nCOver50
				//* CGfactorOver50
				;
		
		predictions[0] = pOver50;
		
		double pUnder50 = this.getProbL50()
				* wCUnder50
				* eUnder50
				* mSUnder50
				* oUnder50
				* relUnder50
				* rUnder50
				* sUnder50
				* nCUnder50
				//* CGfactorUnder50
				;
		
		predictions[1] = pUnder50;

		return predictions;

	}

	public static void main(String[] args) {

		NBClassifier c = new NBClassifier("census.names");
		c.train("census.train");
		c.makePredictions("census.test");

	}

}
