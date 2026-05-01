package controller;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;

import model.Player;
import view.EnterPlayers;

public class Main {
	
	static ArrayList<Player> players = new ArrayList<Player>();

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() { public void run() {
													try {
														JFrame mainWindow = new JFrame("Schiffeversenken");
														mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
									                    mainWindow.setSize(800, 600);
									                    
									                    EnterPlayers enterPlayersPanel = new EnterPlayers();
									                    
									                    mainWindow.getContentPane().add(enterPlayersPanel);
									                    
									                    mainWindow.setVisible(true);
									                    
													} catch (Exception e) {
														e.printStackTrace();
													}
												}										
		});
		
	}
	
	public static void addPlayers() {
		
		Scanner input = new Scanner(System.in);
		
		String nameTmp;
		String colourTmp;
		
		//Spieler hinzufügen
		do {
			System.out.print("Erstlle Neuen Spieler (e to exit)... Name: ");
			nameTmp = input.next();
			System.out.print("Farbe: ");
			colourTmp = input.next();
			
			players.add(new Player(nameTmp, colourTmp));
			
		}
		while (!nameTmp.equals("e"));
		
		input.close();
	}

}
