package nl.sumo.jose.main.AntiCheat;
import nl.sumo.jose.main.Sumo;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.player.PlayerCommandPreprocessEvent;
import cn.nukkit.item.Item;
import cn.nukkit.event.Listener;
import cn.nukkit.utils.Config;
import java.io.File;
import cn.nukkit.Player;
import nl.sumo.jose.main.Arena;
import nl.sumo.jose.main.Boss;
public class Comandos implements Listener{
	private Sumo sumo;
	public Comandos(Sumo pl){
		this.sumo = pl;
	}
	
@EventHandler
public void antiCommands(PlayerCommandPreprocessEvent event){
	Player player = event.getPlayer();
	String[] games = new File(sumo.getDataFolder()+"/Arenas/").list();
	for(String w : games){
	Config arena = new Config(sumo.getDataFolder()+"/Arenas/"+w,Config.YAML);
	if(event.getPlayer().getLevel() == sumo.getServer().getLevelByName(arena.getString("level"))){
	String text = event.getMessage();
if(text.startsWith("/lobby")){
	event.setCancelled(true);
if(arena.getInt("status") == 0){
	if(arena.getString("slot1").equals(player.getName())){
		arena.set("slot1", "undefine");
		arena.save();
	}
	if(arena.getString("slot2").equals(player.getName())){
		arena.set("slot1", "undefine");
		arena.save();
	}
player.teleport(sumo.getServer().getDefaultLevel().getSafeSpawn());
player.setGamemode(2);
player.getInventory().clearAll();
player.setImmobile(false);
Boss.removeBossBar(player, arena.getInt("id"));
player.sendMessage(Arena.title+"Has dejado el combate");
}else{
	sumo.getMessagePlayerError(event.getPlayer());
}
	
}
if(text.startsWith("/hub")){
	event.setCancelled(true);
	if(arena.getInt("status") == 0){
		if(arena.getString("slot1").equals(player.getName())){
			arena.set("slot1", "undefine");
			arena.save();
		}
		if(arena.getString("slot2").equals(player.getName())){
			arena.set("slot1", "undefine");
			arena.save();
		}
	player.teleport(sumo.getServer().getDefaultLevel().getSafeSpawn());
	player.setGamemode(2);
	player.getInventory().clearAll();
	player.setImmobile(false);
	Boss.removeBossBar(player, arena.getInt("id"));
	player.sendMessage(Arena.title+"Has dejado el combate");
	}else{
		sumo.getMessagePlayerError(event.getPlayer());	
	}
	
}

if(text.startsWith("/gamemode")){
	event.setCancelled(true);
	sumo.getMessagePlayerError(event.getPlayer());
}
if(text.startsWith("/tp")){
	event.setCancelled(true);
	sumo.getMessagePlayerError(event.getPlayer());
}
if(text.startsWith("/kick")){
	event.setCancelled(true);
	sumo.getMessagePlayerError(event.getPlayer());
}
if(text.startsWith("/give")){
	event.setCancelled(true);
	sumo.getMessagePlayerError(event.getPlayer());
}
if(text.startsWith("//pos1")){
	event.setCancelled(true);
	sumo.getMessagePlayerError(event.getPlayer());
}
if(text.startsWith("//pos2")){
	event.setCancelled(true);
	sumo.getMessagePlayerError(event.getPlayer());
}
if(text.startsWith("//set")){
	event.setCancelled(true);
	sumo.getMessagePlayerError(event.getPlayer());
}
if(text.startsWith("/ban")){
	event.setCancelled(true);
	sumo.getMessagePlayerError(event.getPlayer());
}
if(text.startsWith("/ban-ip")){
	event.setCancelled(true);
	sumo.getMessagePlayerError(event.getPlayer());
}
if(text.startsWith("/say")){
	event.setCancelled(true);
	sumo.getMessagePlayerError(event.getPlayer());
}
if(text.startsWith("/kill")){
	event.setCancelled(true);
	sumo.getMessagePlayerError(event.getPlayer());
}
if(text.startsWith("/suicide")){
	event.setCancelled(true);
	sumo.getMessagePlayerError(event.getPlayer());
}
if(text.startsWith("/help")){
	event.setCancelled(true);
	sumo.getMessagePlayerError(event.getPlayer());
}

	}
	}	
}
	
	
	
}
