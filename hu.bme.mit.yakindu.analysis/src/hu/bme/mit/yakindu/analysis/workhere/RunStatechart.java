package hu.bme.mit.yakindu.analysis.workhere;

import java.io.IOException;
import java.util.Scanner;

import hu.bme.mit.yakindu.analysis.RuntimeService;
import hu.bme.mit.yakindu.analysis.TimerService;
import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;
import hu.bme.mit.yakindu.analysis.example.IExampleStatemachine;



public class RunStatechart {
	
	public static void main(String[] args) throws IOException {
		ExampleStatemachine s = new ExampleStatemachine();
		s.setTimer(new TimerService());
		RuntimeService.getInstance().registerStatemachine(s, 200);

		s.init();
		s.enter();
		s.runCycle();
		Scanner inputScan = new Scanner(System.in);
		while(true) {
			String readCommand = inputScan.nextLine();
			switch(readCommand) {
				case "start":
					s.raiseStart();
					s.runCycle();
					break;
				case "black":
					s.raiseBlack();
					s.runCycle();
					break;
				case "white":
					s.raiseWhite();
					s.runCycle();
					break;
				case "exit":
					System.out.println("Program exit.");
					System.exit(0);
				default:
					System.out.println("Command not legit.");
					break;
			}
			print(s);
		}
	}

	public static void print(IExampleStatemachine s) {
		System.out.println("W = " + s.getSCInterface().getWhiteTime());
		System.out.println("B = " + s.getSCInterface().getBlackTime());
	}
}
