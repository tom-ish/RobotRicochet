package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Players extends Observable {
	
	public class Player {
		String name;
		int nbPoints;
		int nbCoups;
		boolean isConnected;
		
		Player(String name) {
			this.name=name;
			isConnected = true;
		}

		public String getName() {
			return name;
		}

		public int getScore() {
			return nbPoints;
		}

		public int getCoups() {
			return nbCoups;
		}
	}

	List<Player> players;
	
	public Players() {
		players = new ArrayList<>();
	}
	
	public void add(String name) {
		//Recherche si on a un joueur de ce nom là.
		//Si oui -> le mettre à connecté, il est revenu
		//Si non -> le créer et l'ajouter
		Player p;
		if((p=get(name)) != null) {
			p.isConnected=true;
		} else {
			p = new Player(name);
			players.add(p);
		}
		this.setChanged();
		this.notifyObservers(players);
	}
	
	private Player get(String name) {
		for(Player p : players) {
			if(p.name == name)
				return p;
		}
		return null;
	}
	
	public void remove(String name) {
		Player p = get(name);
		p.isConnected = false;
	}
	
	//Nouveau round
	public void resetRound() {
		for(Player p : players)
			p.nbCoups=Integer.MAX_VALUE;
	}
	
	//Nouveau jeu/session
	public void resetSession() {
		for(Player p : players) {
			p.nbPoints = 0;
			if(!p.isConnected) {
				players.remove(p); //Cette fois, pas besoin de le garder pour son score, tout le monde est à zéro.
			}
		}
	}
	
//	public void sortScores() {
//		sort(players);
//	}
//	public void sortRound() {
//		sort(round);
//	}
//	
//	private void sort(List<Player> list) {
//		//Pour les coups, il va y avoir des chagements un par un, donc c'est déjà presque trié => tri par insertion !
//		int n = list.size();
//		for(int i = 2; i<n; i++)
//			for (int k = i; k > 1 && list.get(k).nbCoups < list.get(k-1).nbCoups; k--) 
//				swap(list, k, k-1);
//	}
//	
//	private void swap(List<Player> list, int i, int j) {
//		Player p = list.get(i);
//		list.set(i, list.get(j));
//		list.set(j, p);
//	}
	

}