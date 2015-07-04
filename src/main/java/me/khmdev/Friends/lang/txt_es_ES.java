package me.khmdev.Friends.lang;
import java.util.*;

public class txt_es_ES extends ListResourceBundle 
{ 
   public Object[][] getContents() 
   {
      return contenido;
   }
   private Object[][] contenido = { 
		   {"tpToPlayer.noPerms","No tienes permisos"},
		   {"tpToPlayer.error","No se ha podido teletransportar a %player%"},
		   {"tpToPlayer.tp","Teletransportando a %player%"},
		   {"tpToPlayer.offline","%player% esta desconectado"},
		   {"tpToPlayer.serverError","No se puede teletransportar entre servidores"},
		   {"tpToPlayer.reload", "&CAun no se ha recargado el item"},

		   
		   
   };
}