package me.khmdev.Friends.Gestores;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import me.khmdev.APIBase.API;
import me.khmdev.APIBase.Almacenes.Almacen;
import me.khmdev.APIBase.Almacenes.Datos;

public class GestorNBT extends IGestor implements Datos {
	public GestorNBT(){
		API.getInstance().getCentral().insertar(this);
	}
	@Override
	public void cargar(Almacen nbt) {
		for (String k : nbt.getKeys()) {
			Almacen alm=nbt.getAlmacen(k);
			List<String> l=new LinkedList<>(),
					 l2=new LinkedList<>();
			for (String s : alm.getKeys()) {
				if(alm.getByte(s)==1){
					l.add(s);
				}else if(alm.getByte(s)==0){
					l2.add(s);
				}
			}
			friends.put(k, l);
			peticiones.put(k, l2);

		}
	}

	@Override
	public void guardar(Almacen nbt) {
		for (Entry<String, List<String>> ent : friends.entrySet()) {
			Almacen alm = nbt.getAlmacen(ent.getKey());
			for (String s : ent.getValue()) {
				alm.setByte(s, (byte) 1);
			}
			nbt.setAlmacen(ent.getKey(), alm);
		}
		for (Entry<String, List<String>> ent : peticiones.entrySet()) {
			Almacen alm = nbt.getAlmacen(ent.getKey());
			for (String s : ent.getValue()) {
				alm.setByte(s, (byte) 0);
			}
			nbt.setAlmacen(ent.getKey(), alm);
		}
	}

	@Override
	public void actualizarFriends() {
		
	}


}
