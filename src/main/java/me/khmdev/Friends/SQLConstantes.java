package me.khmdev.Friends;

import me.khmdev.APIBase.Almacenes.ConstantesAlmacen;

public class SQLConstantes {
	public static final String tabla = "servidor", ServidorID = "IP",
			ServidorAlias = "alias", UserServidor = "servidor",
			ServerNull = "null", ServerNullName = "Desconectado",
			tablaAvisos = "avisos", AvisosUser = "usuario",
			AvisosMSG = "mensaje",AvisosID="id",AvisosRecv="recibido";;

	public static final String[] sql = {
			"CREATE TABLE IF NOT EXISTS `" + tabla + "` (" + "" + ServidorID
					+ " varchar(22) NOT NULL," + "" + ServidorAlias
					+ " varchar(30) NOT NULL," + "PRIMARY KEY (" + ServidorID
					+ ")" + ")\n",
					
			"CREATE TABLE IF NOT EXISTS `" + tablaAvisos + "` (" 
					+AvisosID+" MEDIUMINT NOT NULL AUTO_INCREMENT,"
					+ AvisosUser + " varchar(16) NOT NULL," 
					+ AvisosMSG  + " varchar(300) NOT NULL," 
					+ AvisosRecv+" tinyint(1) NOT NULL DEFAULT 0,"
					+ "PRIMARY KEY ( id )," 
					+ "FOREIGN KEY ( " + AvisosUser + " ) REFERENCES "
					+ ConstantesAlmacen.tabla + "( " + ConstantesAlmacen.id
					+ " ) ON DELETE CASCADE ON UPDATE CASCADE" + ")\n",

			"INSERT INTO `" + tabla + "` (`" + ServidorID + "`, `"
					+ ServidorAlias + "`)" + " VALUES (" + "'" + ServerNull
					+ "', '" + ServerNullName + "'" + ")\n",
			"ALTER TABLE `" + ConstantesAlmacen.tabla + "` " + "ADD "
					+ UserServidor + " varchar(22)," + "ADD FOREIGN KEY ( "
					+ UserServidor + " ) REFERENCES " + tabla + "( "
					+ ServidorID + " ) ON DELETE CASCADE ON UPDATE CASCADE"
					+ ")\n" };
}
