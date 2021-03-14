package hu.bme.mit.yakindu.analysis.workhere;

import java.util.Random;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.yakindu.sct.model.sgraph.State;
import org.yakindu.sct.model.sgraph.Statechart;
import org.yakindu.sct.model.sgraph.Transition;

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
		
		//int nul = 0;
		State previousStat = null;
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			//EObject trans = content;
			if(content instanceof State) {
				
				State currentState = (State)content;
				if(previousStat != null) {
					System.out.println(previousStat.getName() + "->" + currentState.getName());
					
				}
				previousStat = currentState;
				//System.out.println(((Transition) content).getSource().getName() + " -> " + ((Transition) content).getTarget().getName());
				
				int outGoingSize = currentState.getOutgoingTransitions().size();
				if(outGoingSize == 0) {
					System.out.println("Found a trap: " + currentState.getName());
				}
				
				if(currentState.getName().equals("empty")) {
					Random rand = new Random();
					int newNameInt = rand.nextInt(100);
					currentState.setName("SuggestedName" + newNameInt);
					System.out.println(currentState.getName());
				}
				
			}
		}
		
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
	}
}