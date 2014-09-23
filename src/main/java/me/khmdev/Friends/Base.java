package me.khmdev.Friends;

import java.util.List;

import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.CItems;
import me.khmdev.APIAuxiliar.whereIs.SQLControl;
import me.khmdev.APIBase.API;
import me.khmdev.APIBase.Almacenes.sql.AlmacenSQL;
import me.khmdev.Friends.Gestores.GestorNBT;
import me.khmdev.Friends.Gestores.GestorSQL;
import me.khmdev.Friends.Gestores.IGestor;
import me.khmdev.Friends.Gestores.ItemGestor;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Base implements Listener {
	private IGestor gestor;
	private static Base instance;
	private ItemGestor item;
	private JavaPlugin plugin;
	
	public Base(JavaPlugin pl) {
		Bukkit.getMessenger()
		.registerOutgoingPluginChannel(pl,"BungeeCord");
		plugin=pl;
		instance = this;
		item = new ItemGestor();
		CItems.addItem(item);
		AlmacenSQL sql = API.getInstance().getSql();
		if (sql.isEnable()) {
			
			gestor = new GestorSQL(sql, pl);
			for (String s : ConstantesFriendSQL.sql) {
				sql.sendUpdate(s);

			}
		} else {
			gestor = new GestorNBT();

		}
		Bukkit.getServer().getPluginManager().registerEvents(this, pl);

	}

	@EventHandler
	public void logeIn(PlayerJoinEvent e) {
		List<String> p = gestor.getPeticiones(e.getPlayer().getName());

		e.getPlayer().sendMessage(peticiones(p));
	}

	public static String peticiones(List<String> p) {
		if (p.size() == 0) {
			return "";
		}
		String s = "Tienes peticiones de amistad de: ";
		for (String ss : p) {
			s += ss + " ";
		}
		s += "\nAcepta la peticion con  /amigos aceptar <usuario>";
		s += "\nCancela la peticion con /amigos cancelar <usuario>";
		return s;
	}

	private static String help() {
		String s = "/amigos listar   lista todos tus amigos\n";
		s += "/amigos listar   lista toda las peticiones\n";
		s += "/amigos agregar <Usuario>   agrega un usuario a amigos\n";
		s += "/amigos eliminar <Usuario>   elimina un amigos\n";
		s += "/amigos aceptar <Usuario>   acepta una solicitud de amistad\n";
		s += "/amigos cancelar <Usuario>   rechaza una solicitud de amistad\n";
		return s;
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("amigos")) {
			if (args.length == 0) {
				sender.sendMessage(help());
				return true;
			}
			if (args[0].equalsIgnoreCase("listar")) {
				List<String> f = gestor.getFriends(sender.getName());
				sender.sendMessage("Amigos:\n");
				for (String string : f) {
					sender.sendMessage(string + "("
							+ SQLControl.getServer(string) + ")");
				}
				return true;
			} else if (args[0].equalsIgnoreCase("peticiones")) {
				List<String> f = gestor.getPeticiones(sender.getName());
				sender.sendMessage("peticiones:\n");
				for (String string : f) {
					sender.sendMessage(string);
				}
				return true;
			} else if (args[0].equalsIgnoreCase("get")) {
				Player pl = sender instanceof Player?(Player)sender:null;
				if (pl == null) {
					return true;
				}
				pl.getInventory().addItem(item.getItem());
				pl.updateInventory();
				sender.sendMessage("Item enviado");
				return true;
			}
			if (args.length < 2) {
				sender.sendMessage(help());
				return true;
			}
			if (args[0].equalsIgnoreCase("agregar")) {
				if (sender.getName().equals(args[1])) {
					sender.sendMessage("No puedes agregarte a ti mismo");
					return true;
				}
				if (!gestor.existe(args[1])) {
					sender.sendMessage("Nunca se ha conectado ese usuario");
					return true;
				}
				if (gestor.getFriends(sender.getName()).contains(args[1])) {
					sender.sendMessage("Ya eres amigo de " + args[1]);
					return true;
				}
				if (gestor.getPeticiones(args[1]).contains(sender.getName())) {
					sender.sendMessage("Ya has enviado una peticion a "
							+ args[1]);
					return true;
				}
				gestor.addPeticion(sender.getName(), args[1]);
				sender.sendMessage("Peticion enviada");
				return true;
			} else if (args[0].equalsIgnoreCase("aceptar")) {
				List<String> l = gestor.getPeticiones(sender.getName());
				if (!l.contains(args[1])) {
					sender.sendMessage("No ha recibido ninguna invitacion de "
							+ args[1]);
					return true;
				}
				gestor.addFriend(sender.getName(), args[1]);
				sender.sendMessage("Ahora " + args[1] + " es tu amigo");
				return true;
			} else if (args[0].equalsIgnoreCase("cancelar")) {
				List<String> l = gestor.getPeticiones(sender.getName());
				if (!l.contains(args[1])) {
					sender.sendMessage("No ha recibido ninguna invitacion de "
							+ args[1]);
					return true;
				}
				gestor.removePeticion(sender.getName(), args[1]);
				sender.sendMessage("Se ha eliminado la peticion de amistad de "
						+ args[1]);
				return true;
			} else if (args[0].equalsIgnoreCase("eliminar")) {
				List<String> l = gestor.getFriends(sender.getName());
				if (!l.contains(args[1])) {
					sender.sendMessage("No eres amigo de " + args[1]);
					return true;
				}
				gestor.removeFriend(sender.getName(), args[1]);
				sender.sendMessage("Ya no eres amigo de " + args[1]);
				return true;
			}
			sender.sendMessage(help());
			return true;
		}
		return false;
	}

	public static Base getInstance() {
		return instance;
	}

	public IGestor getGestor() {
		return gestor;
	}

	public Plugin getPlugin() {
		return plugin;
	}
	public ItemGestor getItemManager(){
		return item;
	}
}
