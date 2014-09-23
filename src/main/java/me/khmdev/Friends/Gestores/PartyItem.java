package me.khmdev.Friends.Gestores;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.CustomItem;
import me.khmdev.APIAuxiliar.Players.AuxPlayer;
import me.khmdev.Partys.Base;
import me.khmdev.Partys.Gestores.Gestor;
import me.khmdev.Partys.Gestores.Party;

public class PartyItem extends CustomItem{
	private String player;

	public PartyItem(String pl) {
		super(AuxPlayer.getItem(Material.IRON_SWORD,"Invitar a party"));
		player=pl;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(InventoryClickEvent event) {
		if(!(event.getWhoClicked() instanceof Player)){return;}
		Player sender=(Player) event.getWhoClicked();
		Gestor gestor=Base.getInstance().getGestor();
		Party par = gestor.getParty(sender);
		if (par == null) {
			gestor.crearParty(sender);
			par = gestor.getParty(sender);
		}
		if (par.getOwn() != sender) {
			sender.sendMessage("No tienes permisos para invitar a nadie en esta party");
			return;
		}
		if (sender.getName().equals(player)) {
			sender.sendMessage("No puedes invitarte a ti mismo");
			return;
		}
		Player pl2 = Bukkit.getPlayerExact(player);
		if (pl2 == null) {
			sender.sendMessage(player + " no esta conectado");
			return;
		}
		if (par.esta(pl2)) {
			sender.sendMessage(player + " ya esta en la partida");
			return;
		}
		if (par.tienePeticion(pl2)) {
			sender.sendMessage("Ya se ha enviado una invitacion a "
					+ player);
			return;
		}
		gestor.enviarPeticion(pl2, par);
		sender.sendMessage("Peticion enviada");
		return;
	
	}

	@Override
	public void execute(PlayerInteractEvent event) {
		
	}
}
