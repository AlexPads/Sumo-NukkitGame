package nl.sumo.jose.main.Entity;
import cn.nukkit.level.Level;
import cn.nukkit.entity.Entity;
import nl.sumo.jose.main.Sumo;
import nl.sumo.jose.main.Entity.SumoEntity;
public class Update extends cn.nukkit.scheduler.Task{
	private Sumo sumo;
	public Update(Sumo pl){
		this.sumo = pl;
	}
	@Override
	public void onRun(int tick) {
		sumo.updateSystem();
	sumo.setPlayersArena();
		for(Level lv : sumo.getServer().getLevels().values()){
			for(Entity sumogame : lv.getEntities()){
				if(sumogame instanceof SumoEntity){
					sumogame.setNameTag(sumo.getNameNPC());
					sumogame.setNameTagAlwaysVisible(true);
					sumogame.setNameTagVisible(true);
				}
			}
		}
	}
}
