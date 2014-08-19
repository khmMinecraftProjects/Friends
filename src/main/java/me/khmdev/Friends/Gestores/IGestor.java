package me.khmdev.Friends.Gestores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public abstract class IGestor {
	protected static HashMap<String, List<String>> friends = new HashMap<>();
	protected static HashMap<String, List<String>> peticiones = new HashMap<>();

	public HashMap<String, List<String>> getFriends() {
		return friends;
	}

	public abstract void actualizarFriends();

	public List<String> getFriends(String pl) {
		List<String> l = friends.get(pl);
		if (l == null) {
			l = new ArrayList<>();
		}
		return l;
	}

	public void addFriend(String pl1, String pl2) {
		List<String> l1 = getFriends(pl1);
		if(!l1.contains(pl2)){l1.add(pl2);}
		friends.put(pl1, l1);
		List<String> l2 = getFriends(pl2);
		if(!l2.contains(pl1)){l2.add(pl1);}
		friends.put(pl2, l2);
	}

	public void removeFriend(String pl1, String pl2) {
		List<String> l1 = getFriends(pl1);
		l1.remove(pl2);
		friends.put(pl1, l1);
		List<String> l2 = getFriends(pl2);
		l2.remove(pl2);
		friends.put(pl2, l2);
	}

	public HashMap<String, List<String>> getPeticiones() {
		return peticiones;
	}

	public List<String> getPeticiones(String pl) {
		List<String> l = peticiones.get(pl);
		if (l == null) {
			l = new ArrayList<>();
		}
		return l;
	}

	public void addPeticion(String emisor,String receptor){
		List<String> l1=getPeticiones(receptor);
		l1.add(emisor);peticiones.put(receptor, l1);
		
	}

	public void removePeticion(String emisor, String receptor) {
		List<String> l1 = getPeticiones(receptor);
		l1.remove(emisor);
		peticiones.put(receptor, l1);
	}

	public List<String> getPeticionesRealizadas(String emisor) {
		List<String> l = new ArrayList<>();
		for (Entry<String, List<String>> ent : peticiones.entrySet()) {
			if (ent.getValue().contains(emisor)) {
				l.add(ent.getKey());
			}
		}
		return l;
	}

	public boolean existe(String string) {
		return friends.containsKey(string);
	}
}
