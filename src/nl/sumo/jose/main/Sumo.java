package nl.sumo.jose.main;
import cn.nukkit.plugin.PluginBase;
import nl.sumo.jose.main.Arena;
import java.io.File;
import java.nio.charset.StandardCharsets;

import cn.nukkit.level.Level;
import cn.nukkit.level.Sound;
import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.entity.Attribute;
import cn.nukkit.entity.Entity;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import nl.sumo.jose.main.Entity.SumoEntity;
import nl.sumo.jose.main.Entity.Update;
import nl.sumo.jose.main.Events.NPC;
import cn.nukkit.math.Vector3;
import cn.nukkit.item.Item;
import nl.sumo.jose.main.Events.ArenaManager;
import nl.sumo.jose.main.GameTask;
import nl.sumo.jose.main.AntiCheat.Comandos;
import nl.sumo.jose.main.Boss;
import cn.nukkit.network.protocol.*;
import nl.cuboscraft.plugin.main.CubosCore;
public class Sumo extends PluginBase{
	public String namefinal = "";
	public int id = 0;
	public int players = 0;
	public CubosCore core;
	@Override
public void onEnable(){
		 Entity.registerEntity(SumoEntity.class.getSimpleName(), SumoEntity.class);
		new File(this.getDataFolder() + "/Arenas/").mkdirs();
	this.getServer().getLogger().info(Arena.title+" Cargando resources .....");
	this.getServer().getLogger().info(Arena.title+" Hay "+this.countArchivos()+" arenas configuradas");
	this.loadArenas();
	this.getServer().getScheduler().scheduleRepeatingTask(new Update(this), 5);
	this.getServer().getScheduler().scheduleRepeatingTask(new GameTask(this), 20);
	 this.getServer().getPluginManager().registerEvents(new NPC(this), this);
	 this.getServer().getPluginManager().registerEvents(new ArenaManager(this), this);
	 this.getServer().getPluginManager().registerEvents(new Comandos(this), this);
}
	
public void loadArenas(){
if(this.countArchivos() > 0){
String[] gameLevel = new File(this.getDataFolder()+"/Arenas/").list();	
for(String w : gameLevel){
	Config arena = new Config(this.getDataFolder()+"/Arenas/"+w,Config.YAML);
	this.getServer().loadLevel(arena.getString("level"));
	arena.set("status", 0);
	arena.set("time", 60*3);
	arena.set("start", 10);
	arena.set("slot1", "undefine");
	arena.set("slot2", "undefine");
	arena.save();
	this.getServer().getLevelByName(arena.getString("level")).setTime(0);
	this.getServer().getLevelByName(arena.getString("level")).stopTime();
	this.getServer().getLogger().info(Arena.title+" Cargando "+w.replace(".yml", ""));
}

}else{
	this.getServer().getLogger().info(Arena.title+" Se cargaron 0 arenas correctamente");	
}
}
	
public int countArchivos(){
	int total = 0;
	File archivos = new File(this.getDataFolder()+"/Arenas/");
	String[] isArchivo = archivos.list();
	total += isArchivo.length;
	  return total;
}

/*
 * Crear configuracion de una arena nueva
 * Usando sistema d eid
 */
public CompoundTag nbt(Player sender,String name) {
    CompoundTag nbt = new CompoundTag()
     .putList(new ListTag<>("Pos")
            .add(new DoubleTag("", sender.x))
            .add(new DoubleTag("", sender.y))
            .add(new DoubleTag("", sender.z)))
      .putList(new ListTag<DoubleTag>("Motion")
            .add(new DoubleTag("", 0))
            .add(new DoubleTag("", 0))
            .add(new DoubleTag("", 0)))
     .putList(new ListTag<FloatTag>("Rotation")
            .add(new FloatTag("", (float) sender.getYaw()))
            .add(new FloatTag("", (float) sender.getPitch())))
     .putBoolean("Invulnerable", true)
     .putString("NameTag", name)
     .putFloat("scale", 1);    
         nbt.putCompound("Skin", new CompoundTag()
                .putString("ModelId", sender.getSkin().getGeometryName())
                .putByteArray("Data", sender.getSkin().getSkinData())
                .putString("ModelId", sender.getSkin().getSkinId())
                .putByteArray("CapeData", sender.getSkin().getCapeData())
                .putString("GeometryName", sender.getSkin().getGeometryName())
                .putByteArray("GeometryData", sender.getSkin().getGeometryData().getBytes(StandardCharsets.UTF_8))
         );
         nbt.putBoolean("ishuman", true);
         nbt.putString("Item", sender.getInventory().getItemInHand().getName());
         nbt.putString("Helmet", sender.getInventory().getHelmet().getName());
         nbt.putString("Chestplate", sender.getInventory().getChestplate().getName());
         nbt.putString("Leggings", sender.getInventory().getLeggings().getName());
         nbt.putString("Boots", sender.getInventory().getBoots().getName());
     
     return nbt;

}
public void createArena(Player player , String id, String name, String level){
if(new File(this.getDataFolder()+"/Arenas/sumo-"+id+".yml").exists()){
	player.sendMessage(Arena.title+" Esta id ya esta registrada intenta usar otra");
}else{
Config arena = new Config(this.getDataFolder()+"/Arenas/sumo-"+id+".yml",Config.YAML);
arena.set("nameArena", name);
arena.set("level", level);
arena.set("status", 0);
arena.set("time", 60*3);
arena.set("game1x", 0);
arena.set("game1y", 0);
arena.set("game1z", 0);
arena.set("game2x", 0);
arena.set("game2y", 0);
arena.set("game2z", 0);
arena.set("minvoidx", 0);
arena.set("minvoidy", 0);
arena.set("minvoidz", 0);
arena.set("start", 10);
arena.set("id", Boss.getIDBoss());
arena.set("slot1","undefine");
arena.set("slot2","undefine");
arena.save();
this.getServer().loadLevel(level);
player.teleport(this.getServer().getLevelByName(level).getSafeSpawn());
player.sendMessage(Arena.title+" Configura la arena");
}
}

public void setGamePos1(Player player, String id){
	if(!new File(this.getDataFolder()+"/Arenas/sumo-"+id+".yml").exists()){
		player.sendMessage(Arena.title+" Esta id no esta registrada");
	}else{
		Config arena = new Config(this.getDataFolder()+"/Arenas/sumo-"+id+".yml",Config.YAML);
		int x = (int) player.getX(); int y = (int) player.getY(); int z = (int) player.getZ();
		int[] valores = {x,y,z};
		arena.set("game1x", valores[0]);
		arena.set("game1y", valores[1]);
		arena.set("game1z", valores[2]);
		arena.save();
		player.sendMessage(Arena.title+" Se ha seleccionado el punto 1 id : sumo-"+id);
	}
}

public void setGamePos2(Player player, String id){
	if(!new File(this.getDataFolder()+"/Arenas/sumo-"+id+".yml").exists()){
		player.sendMessage(Arena.title+" Esta id no esta registrada");
	}else{
		Config arena = new Config(this.getDataFolder()+"/Arenas/sumo-"+id+".yml",Config.YAML);
		int x = (int) player.getX(); int y = (int) player.getY(); int z = (int) player.getZ();
		int[] valores = {x,y,z};
		arena.set("game2x", valores[0]);
		arena.set("game2y", valores[1]);
		arena.set("game2z", valores[2]);
		arena.save();
		player.sendMessage(Arena.title+" Se ha seleccionado el punto 2 id : sumo-"+id);
	}
}
public void minvoid(Player player, String id){
	if(!new File(this.getDataFolder()+"/Arenas/sumo-"+id+".yml").exists()){
		player.sendMessage(Arena.title+" Esta id no esta registrada");
	}else{
		Config arena = new Config(this.getDataFolder()+"/Arenas/sumo-"+id+".yml",Config.YAML);
		int x = (int) player.getX(); int y = (int) player.getY(); int z = (int) player.getZ();
		int[] valores = {x,y,z};
		arena.set("minvoidx", valores[0]);
		arena.set("minvoidy", valores[1]);
		arena.set("minvoidz", valores[2]);
		arena.save();
		player.sendMessage(Arena.title+" Se ha seleccionado el minVoid id : sumo-"+id);
	}
}
public void saveArena(Player player , String id){
	if(!new File(this.getDataFolder()+"/Arenas/sumo-"+id+".yml").exists()){
		player.sendMessage(Arena.title+" Esta id no esta registrada");
	}else{
		player.teleport(this.getServer().getDefaultLevel().getSafeSpawn());
		player.sendMessage(Arena.title+" Se ha terminado la configuracion en id : sumo-"+id);
	}
}

@Override
public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
	 Player player = (Player) sender;
	 switch (command.getName().toLowerCase()) {
	 case "sumo":
		if(args[0].equals("create")){
			if(player.isOp()){
				this.createArena(player, args[2], args[1], args[3]);
			}
		}
		if(args[0].equals("setpos1")){
			if(player.isOp()){
				this.setGamePos1(player, args[1]);
			}
		}
		if(args[0].equals("setpos2")){
			if(player.isOp()){
				this.setGamePos2(player, args[1]);
			}
		}
		if(args[0].equals("minvoid")){
			if(player.isOp()){
				this.minvoid(player, args[1]);
			}
		}
		if(args[0].equals("save")){
			if(player.isOp()){
				this.saveArena(player, args[1]);
			}
		}
		if(args[0].equals("npcyaw")){
			if(player.isOp()){
				player.sendMessage("Tu yaw es : "+player.yaw);
			}
		}
		if(args[0].equals("setnpc")){
			if(player.isOp()){
				 CompoundTag nbt = this.nbt(player,this.getNameNPC());  
                 Entity ent = Entity.createEntity("SumoEntity", player.chunk, nbt);
                 ent.setNameTag("Loading...");
                 ent.setNameTagAlwaysVisible(true);
                 ent.setNameTagVisible(true);
                 ent.spawnTo(player);
			}
		}
		if(args[0].equals("removenpc")){
			for(Level lv : this.getServer().getLevels().values()){
				for(Entity game : lv.getEntities()){
					if(game instanceof SumoEntity){
						game.kill();
					}
				}
			}
		}
		 break;
	 }
	 return false;
}

/*
 * NPC EVENTS
 */

public String getNameNPC(){
	String prefix = "§l§bSumo";
	String br = "\n§r";
return prefix+br+"§a"+players+" §eJugando";
}
public void setPlayersArena(){
	int total;
	if(this.countArchivos() > 0){
		total = 0;
		String[] games = new File(this.getDataFolder()+"/Arenas/").list();
		for(String w : games){
		Config game = new Config(this.getDataFolder()+"/Arenas/"+w,Config.YAML);
	for(Player p : this.getServer().getLevelByName(game.getString("level")).getPlayers().values()){
		if(p.getGamemode() == 2){
			total++;
		}
	}
		}
	}else{
		total = 0;
	}
	players = total;
}
/*
 * Sistema de juego
 */

public void updateSystem(){
	
	int ingame = 1;
	int wating = 0;
	if(id >= this.countArchivos()){
		id = 0;
		namefinal = "ARENA.NO.FOUND";
	}
	if(this.countArchivos() > 0){
		Config game = new Config(this.getDataFolder()+"/Arenas/sumo-"+id+".yml",Config.YAML);
		if(game.getInt("status") == ingame){
			id++;
		}else if(game.getInt("status") == wating){
				namefinal = "sumo-"+id;
				id = 0;	
				
			}
		
		}
	}
	

public void joinGame(Player player , String game){

	String name = "§7"+player.getName();
	Config arena = new Config(this.getDataFolder()+"/Arenas/"+game+".yml",Config.YAML);	
	if(arena.getInt("status") == 0){
	int x1 = arena.getInt("game1x");int y1 = arena.getInt("game1y");int z1 = arena.getInt("game1z");
	int x2 = arena.getInt("game2x");int y2 = arena.getInt("game2y");int z2 = arena.getInt("game2z");
	if(arena.getString("slot1").equals("undefine")){
	arena.set("slot1", player.getName());
	arena.save();
	player.teleport(this.getServer().getLevelByName(arena.getString("level")).getSafeSpawn());
	player.teleport(new Vector3(x1,y1,z1));
	player.setImmobile(true);
	player.getInventory().clearAll();
	player.getInventory().setItem(8, Item.get(152,0,1).setCustomName("§l§bSalir"));	
	player.setGamemode(2);
	player.setNameTag("§l§6"+player.getName());
	Boss.sendBossBarToPlayer(player, arena.getInt("id"), "Sumo");
	Boss.setVida(player, arena.getInt("id"), 100);
	Boss.removeBossBar(player, 3984549);
	this.setFood(player, 20);
	player.setFoodEnabled(false);
	}else{
		if(arena.getString("slot2").equals("undefine")){
			arena.set("slot2", player.getName());
			arena.save();
			player.teleport(this.getServer().getLevelByName(arena.getString("level")).getSafeSpawn());
			player.teleport(new Vector3(x2,y2,z2));
			player.setImmobile(true);
			player.getInventory().clearAll();
			player.getInventory().setItem(8, Item.get(152,0,1).setCustomName("§l§bSalir"));	
			player.setGamemode(2);
			player.setNameTag("§l§6"+player.getName());
			Boss.sendBossBarToPlayer(player, arena.getInt("id"), "Sumo");
			Boss.setVida(player, arena.getInt("id"), 100);
			Boss.removeBossBar(player, 3984549);
			this.setFood(player, 20);
			player.setFoodEnabled(false);
			}	
	}

for(Player p : this.getServer().getLevelByName(arena.getString("level")).getPlayers().values()){
	this.getServer().getLevelByName(arena.getString("level")).addSound(new Vector3(p.getX(),p.getY(),p.getZ()), Sound.MOB_ENDERMEN_PORTAL);
	p.sendMessage(Arena.title+name+" §ase ha unido §e(§b"+this.countPlayersGM(game)+"§e/§b2§e)");
}
	}
}

public void getMessagePlayerError(Player player){
	player.sendMessage(Arena.title+"Comando no valido en Arena");
}

public void updateTextBoss(Player player ,String name){
	String text = "";
	Config config = new Config(this.getDataFolder()+"/Arenas/"+name,Config.YAML);
	int id = config.getInt("id");
	int status = config.getInt("status");
	int players = this.getServer().getLevelByName(config.getString("level")).getPlayers().size();
	if(players < 2 && status == 0){
	text = "\n\n§6Buscando oponente§b....";	
	}
	if(players >= 2 && status == 1 && config.getInt("start") > 0){
		text = "\n\n";
	}
	if(players >=2 && status ==1 && config.getInt("start") == 0){
		text = "\n\n"+"§bEl duelo temrina en §7: §e"+getReloj(config.getInt("time"))+" §bsegundos";
	}
	Boss.sendTitle(player, id, text);
}
public String getReloj(int num){
    int hor=num/3600;
    int min=(num-(3600*hor))/60;
    int seg=num-((hor*3600)+(min*60));
return "§e"+min+"§a:§e"+seg;
}
public void setFood(Player player,int food){
	UpdateAttributesPacket upk = new UpdateAttributesPacket();
	upk.entityId = player.getId();
	Attribute attr = Attribute.getAttribute(Attribute.MAX_HUNGER);
	attr.setMaxValue(20);
	attr.setMinValue(0);
	attr.setValue(food);
	upk.entries = new Attribute[]{ attr };
	player.dataPacket(upk);
}

public void setVida(Player player, String name){
	int i = 100;
	Config config = new Config(this.getDataFolder()+"/Arenas/"+name,Config.YAML);
	int id = config.getInt("id");
	int status = config.getInt("status");
	int value = config.getInt("start");
	if(players >= 2 && status == 1 && value > 0){
		if(value == 10){i = 100;}
		if(value == 9){i = 90;}
		if(value == 8){i = 80;}
		if(value == 7){i = 70;}
		if(value == 6){i = 60;}
		if(value == 5){i = 50;}
		if(value == 4){i = 40;}
		if(value == 3){i = 25;}
		if(value == 2){i = 10;}
		if(value == 1){i = 0;}
		if(value == 0){i = 0;}
	}else{
		i = 100;
	}
	Boss.setVida(player, id, i);
}

public int countPlayersGM(String name){
	int i = 0;
	Config arena = new Config(this.getDataFolder()+"/Arenas/"+name+".yml",Config.YAML);
	for(Player p :this.getServer().getLevelByName(arena.getString("level")).getPlayers().values()){
		if(p.getGamemode() == 2){
			i++;
		}
	}
	return i;
}


}
