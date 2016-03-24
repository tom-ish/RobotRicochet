package controller;

import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

import communication.Client;
import model.LocalPlayer;
import model.Model;

public class Controller implements Observer {
	//FIELDS
	Model model;

	public Controller(Model model) {
		this.model=model;
	}

	public void connect(String name) throws ConnectException, UnknownHostException, IOException {
		//TODO : Classe de connexion (avec name etc..)
		Client.getInstance().addObserver(this);
		Client.getInstance().connect();
		System.out.println("(Controller) sent : CONNEXION/"+name+"/");
		Client.getInstance().sendMessage("CONNEXION/"+name+"/");
		LocalPlayer.getInstance().setName(name);

	}
	public void disconnect(String name) {
		System.out.println("(Controller) sent : SORT/"+name+"/");
		try {
			Client.getInstance().sendMessage("SORT/"+name+"/");
		} catch (IOException e) {
			e.printStackTrace();
		}
		Client.getInstance().deleteObserver(this);
		Client.getInstance().disconnect();
	}
	public void sendSolution(String name, int solutionInt){
		String solution = String.valueOf(solutionInt);
		System.out.println("(Controller) sent : SOLUTION/"+name+"/"+solution+"/");
		try {
			Client.getInstance().sendMessage("SOLUTION/"+name+"/"+solution+"/");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		String message = (String) arg;
		String[] tokens = message.split("/");
		System.out.println("(Controller) received : "+message);
		//S->C : BIENVENUE/user/
		if (tokens.length>1 && tokens[0].equals("BIENVENUE")) {
			//Should be the first message received.. should we test it ?
			System.out.println(message);

			//S->C : CONNECTE/user/
		} else if (tokens.length>1 && tokens[0].equals("CONNECTE")) {
			System.out.println(message);
			if(tokens.length<2) { System.err.println("probleme"); return; }
			String name = tokens[1];
			model.playerConnected(name);
			//S->C :DECONNEXION/user/
		} else if (tokens.length>1 && tokens[0].equals("DECONNEXION")) {
			System.out.println(message);
			if(tokens.length<2) { System.err.println("probleme"); return; }
			String name = tokens[1];
			model.playerLeaved(name);
			//S->C : SESSION/plateau/size_x/size_y
		} else if (tokens.length>1 && tokens[0].equals("SESSION")) {
			System.out.println(message);
			if(tokens.length<4) { System.err.println("probleme"); return; }
			String gridBuffer = tokens[1];
			int size_x = Integer.valueOf(tokens[2]);
			int size_y = Integer.valueOf(tokens[3]);
			model.getGrid().setGrid(model.getGridFromBuffer(size_x, size_y, gridBuffer));
			model.getGrid().update();
			System.out.println("update done");
		}
		//S->C : VAINQUEUR/bilan/
		else if (tokens.length>1 && tokens[0].equals("VAINQUEUR"))
			System.out.println(message);
		//S->C : TOUR/enigme/bilan/
		else if (tokens.length>1 && tokens[0].equals("TOUR")){
			System.out.println(message);
			if(tokens.length==2){
				// S->C : TOUR/enigme/
				String buffer = tokens[1];
				System.out.println("Starting Setting Robot...");
				model.setRobotsFromBuffer(buffer);
				model.getGrid().update();
			}
			else if(tokens.length==3){
				//S->C : TOUR/enigme/bilan/
				System.out.println("Starting Setting Robot with bilan of the current session...");
				String enigmaBuffer = tokens[1];
				String bilanBuffer = tokens[2];
				model.setRobotsFromBuffer(enigmaBuffer);
				model.setBilanCurrentSession(bilanBuffer);
				model.getGrid().update();
			}
			else { System.err.println("probleme"); return; }
		}
		//S->C : TUASTROUVE/
		else if (tokens.length>0 && tokens[0].equals("TUASTROUVE")){
			System.out.println(message);
			// model.setAuctionPhase();
		}	
		//S->C : ILATROUVE/user/coups/
		else if (tokens.length>2 && tokens[0].equals("ILATROUVE"))
			System.out.println(message);
		//S->C : FINREFLEXION/
		else if (tokens.length>0 && tokens[0].equals("FINREFLEXION"))
			System.out.println(message);
		//S->C : VALIDATION/
		else if (tokens.length>0 && tokens[0].equals("VALIDATION"))
			System.out.println(message);
		//S->C : ECHEC/user/
		else if (tokens.length>1 && tokens[0].equals("ECHEC"))
			System.out.println(message);
		//S->C : NOUVELLEENCHERE/user/coups/
		else if (tokens.length>2 && tokens[0].equals("NOUVELLEENCHERE"))
			System.out.println(message);
		//S->C : FINENCHERE/user/coups/
		else if (tokens.length>2 && tokens[0].equals("FINENCHERE"))
			System.out.println(message);
		//S->C : SASOLUTION/user/deplacements/
		else if (tokens.length>2 && tokens[0].equals("SASOLUTION"))
			System.out.println(message);
		//S->C : BONNE/
		else if (tokens.length>0 && tokens[0].equals("BONNE"))
			System.out.println(message);
		//S->C : MAUVAISE/
		else if (tokens.length>0 && tokens[0].equals("MAUVAISE"))
			System.out.println(message);
		//S->C : FINRESO/
		else if (tokens.length>0 && tokens[0].equals("FINRESO"))
			System.out.println(message);
		//S->C : TROPLONG/user/
		else if (tokens.length>1 && tokens[0].equals("TROPLONG"))
			System.out.println(message);
		else {
			//DO NOTHING
		}
	}

}
