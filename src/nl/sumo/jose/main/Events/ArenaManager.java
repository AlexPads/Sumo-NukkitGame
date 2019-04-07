package nl.sumo.jose.main.Events;
import nl.sumo.jose.main.Sumo;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.utils.Config;
import cn.nukkit.Player;
import java.io.File;
import cn.nukkit.event.player.PlayerDropItemEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.item.Item;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.level.Sound;
import cn.nukkit.math.Vector3;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import nl.sumo.jose.main.Boss;
import cn.nukkit.entity.Entity;
import nl.sumo.jose.main.Entity.SumoEntity;
import cn.nukkit.network.protocol.*;
public class ArenaManager implements Listener{
	private Sumo sumo;
	public ArenaManager(Sumo pl){
		this.sumo = pl;
	}
@EventHandler
public void onMoveNPC(PlayerMoveEvent event){
	Player player = event.getPlayer();
	
	for(Entity en : player.getLevel().getNearbyEntities(player.getBoundingBox().grow(4, 4, 4),player)){
		if(en instanceof SumoEntity){
	if(player.distance(en) < 4){
	
		((SumoEntity) en).MovePlayerNpc(player);
	
	}else if (player.distance(en) > 4){
		MoveEntityAbsolutePacket pk = new MoveEntityAbsolutePacket();
		pk.eid = en.getId();
		pk.x = en.getX();
		pk.y = en.getY()+1.5;
		pk.z = en.getZ();
		pk.yaw = 3;
		pk.pitch = 0;
		pk.headYaw = 3;
		player.dataPacket(pk);
	}
		}
	}
	
}
@EventHandler
public void quitPlayer(PlayerQuitEvent event){
	Player player = event.getPlayer();
	String[] files = new File(sumo.getDataFolder()+"/Arenas/").list();
	for(String w : files){
		Config arena = new Config(sumo.getDataFolder()+"/Arenas/"+w,Config.YAML);
		if(player.getLevel() == sumo.getServer().getLevelByName(arena.getString("level"))){
			if(arena.getInt("status") == 0){
				if(arena.getString("slot1").equals(player.getName())){
					arena.set("slot1", "undefine");
					arena.save();
				}
				if(arena.getString("slot2").equals(player.getName())){
					arena.set("slot1", "undefine");
					arena.save();
				}
			}
		}
	}
	}
@EventHandler
public void MinVoid(PlayerMoveEvent event){
	Player player = event.getPlayer();
	String[] files = new File(sumo.getDataFolder()+"/Arenas/").list();
	for(String w : files){
		Config arena = new Config(sumo.getDataFolder()+"/Arenas/"+w,Config.YAML);
		if(player.getLevel() == sumo.getServer().getLevelByName(arena.getString("level"))){
			int minimo = arena.getInt("minvoidy");
			if(player.getY() <= minimo){
				if(arena.getInt("status") == 1){
					if(player.getGamemode() == 2){
						player.setGamemode(3);
						
		sumo.getServer().getLevelByName(arena.getString("level")).addSound(new Vector3(player.getX(),player.getY(),player.getZ()), Sound.MOB_VILLAGER_NO);		
					}
				}
			}
		}
	}
}
	
@EventHandler
public void noDestroy(BlockBreakEvent event){
	Player player = event.getPlayer();
	String[] files = new File(sumo.getDataFolder()+"/Arenas/").list();
	for(String w : files){
		Config arena = new Config(sumo.getDataFolder()+"/Arenas/"+w,Config.YAML);
		if(player.getLevel() == sumo.getServer().getLevelByName(arena.getString("level"))){
			event.setCancelled(true);
		}
	}
	}
@EventHandler
public void noPlace(BlockPlaceEvent event){
	Player player = event.getPlayer();
	String[] files = new File(sumo.getDataFolder()+"/Arenas/").list();
	for(String w : files){
		Config arena = new Config(sumo.getDataFolder()+"/Arenas/"+w,Config.YAML);
		if(player.getLevel() == sumo.getServer().getLevelByName(arena.getString("level"))){
			event.setCancelled(true);
		}
	}
	}	
@EventHandler
public void NoDrop(PlayerDropItemEvent event){
	Player player = event.getPlayer();
	String[] files = new File(sumo.getDataFolder()+"/Arenas/").list();
	for(String w : files){
		Config arena = new Config(sumo.getDataFolder()+"/Arenas/"+w,Config.YAML);
		if(player.getLevel() == sumo.getServer().getLevelByName(arena.getString("level"))){
			event.setCancelled(true);
		}
	}
	
}
@EventHandler
public void onSalir(PlayerInteractEvent event){
Player player = event.getPlayer();
Item item = event.getItem();
String[] files = new File(sumo.getDataFolder()+"/Arenas/").list();
for(String w : files){
	Config arena = new Config(sumo.getDataFolder()+"/Arenas/"+w,Config.YAML);
	if(player.getLevel() == sumo.getServer().getLevelByName(arena.getString("level"))){
	if(item.getId() == 152 && item.getCustomName() == "§l§bSalir"){
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
player.getInventory().setItem(1, new Item(345,0,1).setCustomName("§cGadgets"));
player.getInventory().setItem(7, new Item(339,0,1).setCustomName("§aINFO"));
Boss.removeBossBar(player, arena.getInt("id"));
Boss.sendBossBarToPlayer(player, 3984549, "");
player.setImmobile(false);
		}
	}
	}
}
}
@EventHandler
public void HitPlayer(EntityDamageByEntityEvent event){
	Player player = (Player) event.getDamager();
	String[] files = new File(sumo.getDataFolder()+"/Arenas/").list();
	for(String w : files){
		Config arena = new Config(sumo.getDataFolder()+"/Arenas/"+w,Config.YAML);
		if(player.getLevel() == sumo.getServer().getLevelByName(arena.getString("level"))){
	sumo.getServer().getLevelByName(arena.getString("level")).addSound(new Vector3(player.getX(),player.getY(),player.getZ()), Sound.MOB_BLAZE_HIT);	
		}
	}
}



}
