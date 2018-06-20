package src.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nfunk.jep.JEP;

public class MainClass {

	private static final PrintStream OUTPUT = System.out;
	private static final BufferedReader INPUT = new BufferedReader(new InputStreamReader(System.in));

	public static void main(String[] args) throws NumberFormatException, IOException {

		OUTPUT.println("Enter formula: ");
		String formula = INPUT.readLine();
		
		String pattern = "(\\*)|(\\+)|(-)";
		String[] formulaSplit = formula.split(pattern);
		
		List<String> elements = getElements(formulaSplit);
		
		Map<String,Double> parameters = new HashMap<String,Double>();
		for (String elem : elements) {
			OUTPUT.print("Enter value of " + elem + ": ");
			double value = Double.parseDouble(INPUT.readLine());
			
			if (!parameters.containsKey(elem)) {
				parameters.put(elem, value);
			}
		}      

		/** Evaluating the parametric formula taking the parameters as inputs */
		JEP jep = new JEP();
		jep.addStandardConstants();
		jep.addStandardFunctions();
		
		for (String key : parameters.keySet()) {
			jep.addVariable(key, parameters.get(key));
		}
		
		try {
			jep.parseExpression(formula);
			Object reliability = jep.getValueAsObject();
			OUTPUT.println("System's reliability is: " + reliability);
		}
		catch (Throwable e) {
			OUTPUT.println(e.getMessage());
		}
	}

	private static List<String> getElements(String[] formulaSplit) {
		int n = formulaSplit.length;
		String pattern = "\\s*\\(.*\\s*1*\\s*|\\s*\\(.*\\s*|\\s*1*\\s*|\\s*\\).*\\s*";
		
		for (int i = 0; i < n; i++){
			if (formulaSplit[i].matches("\\s*\\(.*\\w+")) {
				String[] aux = formulaSplit[i].split("\\(");
				formulaSplit[i] = aux[aux.length-1];
			}
			if (formulaSplit[i].matches("\\w+\\).*")) {
				String[] aux = formulaSplit[i].split("\\).*");
				formulaSplit[i] = aux[0];
			}
			if (formulaSplit[i].matches(pattern)) {
				formulaSplit[i] = "";
			}
		}
		
		List<String> parameter = new ArrayList<String>();
		int i = 0;
		while (i < formulaSplit.length) {
			if (!formulaSplit[i].matches("\\s*")){
				if (!parameter.contains(formulaSplit[i].trim())) {
					parameter.add(formulaSplit[i].trim());
				}	
			}
			i++;
		}
		return parameter;
	}
}