package hu.bme.mit.yakindu.analysis.workhere;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.yakindu.sct.model.sgraph.State;
import org.yakindu.sct.model.sgraph.Statechart;
import org.yakindu.sct.model.sgraph.Transition;
import org.yakindu.sct.model.stext.stext.EventDefinition;
import org.yakindu.sct.model.stext.stext.VariableDefinition;

import hu.bme.mit.model2gml.Model2GML;
import hu.bme.mit.yakindu.analysis.modelmanager.ModelManager;

public class Main {
	@Test
	public void test() {
		main(new String[0]);
	}
	
	public static void main(String[] args) {
		ModelManager manager = new ModelManager();
		Model2GML model2gml = new Model2GML();
		
		// Loading model
		EObject root = manager.loadModel("model_input/example.sct");
		
		// Reading model
		Statechart s = (Statechart) root;
		TreeIterator<EObject> iterator = s.eAllContents();
		
		ArrayList<String> events  = new ArrayList<String>();
		ArrayList<String> variables = new ArrayList<String>();
		
		//int nul = 0;
		//State previousStat = null;
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			//EObject transit = content;
			if(content instanceof VariableDefinition) {
				VariableDefinition vd = (VariableDefinition) content;
				variables.add(vd.getName());
			}
			else if(content instanceof EventDefinition) {
				EventDefinition ed = (EventDefinition) content;
				events.add(ed.getName());
			}
		}
		
		System.out.println("public class RunStatechart {\n");
		System.out.println("\tpublic static void main(String[] args) throws IOException {");
		System.out.println("\t\t\r\n " + 
			    "		ExampleStatemachine s = new ExampleStatemachine();\r\n" + 
				"		s.setTimer(new TimerService());\r\n" + 
				"		RuntimeService.getInstance().registerStatemachine(s, 200);\r\n" + 
				"		s.init();\r\n" + 
				"		s.enter();\r\n" + 
				"		s.runCycle();\r\n" + 
				"		Scanner inputScan = new Scanner(System.in);\r\n" + 
				"		while(true) {\r\n" +
				"		\tString readCommand = inputScan.nextLine();\r\n" + 
				"			switch(readCommand) {");
		
		for(String event:events) {
			String capitalized = event.substring(0, 1).toUpperCase() + event.substring(1);
			System.out.println("\t\t\t\tcase \"" + event + "\":\r\n" +
					"			\t\ts.raise" + capitalized + "();");
			System.out.println("\t\t\t\t\ts.runCycle();\r\n" + 
					"			\t\tbreak;");
		}
		System.out.println("\t\t\t\tcase \"exit\":\r\n" + 
				"					System.out.println(\"Program exit.\"); \r\n" +
				"					System.exit(0);\r\n" + 
				"					break;\r\n" + 
				"				default:\r\n" + 
				"					System.out.println(\"Command not legit.\");\r\n" + 
				"					break;");
		
		System.out.println("\t\t\t}");
		System.out.println("\t\t\tprint(s);");
		System.out.println("\t\t}");
		System.out.println("\t}\n");
		
		System.out.println("\tpublic static void print(IExampleStateachine s) {");
		
		for(String var:variables) {
			String cpd = var.substring(0, 1).toUpperCase() + var.substring(1);
			System.out.println("\t\tSystem.out.println(\"" + cpd.charAt(0) + " = \" + s.getSCInterface().get" + cpd + "());");
		}
		System.out.println("\t}");
		System.out.println("}");
		
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
	}
}

//4.feladat előtt kimásoltam a korábbi részekben megoldott feladatokat:

/*if(content instanceof Transition) {
	System.out.println(((Transition)content).getSource().getName() + " -> " + ((Transition)content).getTarget().getName());
}else if(content instanceof State) {
	
	State currentState = (State)content;
	if(previousStat != null) {
		//System.out.println(previousStat.getName() + "->" + currentState.getName());
		
	}
	previousStat = currentState;
	//System.out.println(((Transition) content).getSource().getName() + " -> " + ((Transition) content).getTarget().getName());
	
	int outGoingSize = currentState.getOutgoingTransitions().size();
	if(outGoingSize == 0) {
		System.out.println("Found a trap: " + currentState.getName());
	}
	
	if(currentState.getName().equals("empty") || currentState.getName().equals("")) {
		Random rand = new Random();
		int newNameInt = rand.nextInt(100);
		currentState.setName("SuggestedName" + newNameInt);
		System.out.println(currentState.getName());
	}
}*/

