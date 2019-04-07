package nl.sumo.jose.main.Events;
import cn.nukkit.event.Listener;
import nl.sumo.jose.main.Sumo;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import nl.sumo.jose.main.Entity.SumoEntity;
import cn.nukkit.Player;
import nl.sumo.jose.main.Arena;
public class NPC implements Listener{
	private Sumo sumo;
	public NPC(Sumo pl){
		this.sumo = pl;
	}
	
	
@EventHandler
public void onHit(EntityDamageByEntityEvent event){
		if(event.getEntity() instanceof SumoEntity){
			Player player = (Player) event.getDamager();
			if(sumo.countArchivos() == 0){
			player.sendMessage(Arena.title+"No hay arenas configuradas");	
			}else if(sumo.countArchivos() > 0){
				if(sumo.namefinal == "ARENA.NO.FOUND"){
			player.sendMessage(Arena.title+"No hay arenas disponibles");
			}else{	
			sumo.joinGame(player, sumo.namefinal);
			}
			}
		}
	}
	
	
}
