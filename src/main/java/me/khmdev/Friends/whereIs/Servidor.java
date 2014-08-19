package me.khmdev.Friends.whereIs;

public class Servidor {
	private String ip,name;
	public Servidor(String i,String n){
		setIp(i);setName(n);
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
