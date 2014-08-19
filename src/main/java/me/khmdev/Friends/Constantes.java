package me.khmdev.Friends;

import me.khmdev.APIBase.Almacenes.ConstantesAlmacen;

public class Constantes {
	public static String tabla="amigos",
			celdaUser="usuario",
			celdaAmigo="amigo",
			celdaEstado="estado",
			tipoEsperando="esperando",
			tipoSinAceptar="sin_aceptar",
			tipoConfirmado="confirmado";
	public static final String[] sql={
		"CREATE TABLE IF NOT EXISTS `"+tabla+"` (" +
		""+celdaUser+" varchar(16) NOT NULL, "+
		""+celdaAmigo+" varchar(16) NOT NULL, " +
		""+celdaEstado+" ENUM('"+tipoConfirmado+"', '"+tipoEsperando+
		"', '"+tipoSinAceptar+"') DEFAULT '"+tipoConfirmado+"'," +
		"PRIMARY KEY (`"+celdaUser+"`,`"+celdaAmigo+"`)," +
		"FOREIGN KEY ( "+celdaUser+" ) REFERENCES "+ConstantesAlmacen.tabla
		+"( "+ConstantesAlmacen.id+" ) ON DELETE CASCADE ON UPDATE CASCADE," +
		"FOREIGN KEY ( "+celdaAmigo+" ) REFERENCES "+ConstantesAlmacen.tabla
		+"( "+ConstantesAlmacen.id+" ) ON DELETE CASCADE ON UPDATE CASCADE" +
		")\n" };
}
