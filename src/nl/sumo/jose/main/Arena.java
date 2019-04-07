package nl.sumo.jose.main;
import cn.nukkit.Server;
public class Arena {
public int id = 0;
public int players = 0;
public String namefinal = "default";
public static String title = "§7[§l§aSumo§r§7] §9: §7";

public  int getId(){
	return id;
}
public void setId(int idvalue){
	id = idvalue;
}
public String getName(){
	return namefinal;
}

public int getPlayersArena(){
	return players;
}
public void setPlayers(int value){
	players = value;
}
public int getPlayersArenaById(int idvalue){
	return Server.getInstance().getLevelByName("sumo-"+idvalue).getPlayers().size();
}
public int getMaxPlayers(){
	return 2;
}







}
