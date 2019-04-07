package nl.sumo.jose.main;
import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.network.protocol.*;
import cn.nukkit.entity.Attribute;
public class Boss {
	public static void sendBossBarToPlayer(Player player, long eid, String title) {
		Boss.removeBossBar(player, (int)eid);
        AddEntityPacket packet = new AddEntityPacket();
        packet.entityUniqueId = eid;
		packet.entityRuntimeId = eid;
        packet.type = 52;
        packet.yaw = 0;
        packet.pitch = 0;

		long flags = 0;
		flags |= 1 << Entity.DATA_FLAG_SILENT;
		flags |= 1 << Entity.DATA_FLAG_INVISIBLE;
		flags |= 1 << Entity.DATA_FLAG_NO_AI;
        EntityMetadata dataProperties = new EntityMetadata()
				.putLong(Entity.DATA_FLAGS, flags)
				.putShort(Entity.DATA_AIR, 400)
				.putShort(Entity.DATA_MAX_AIR, 400)
				.putLong(Entity.DATA_LEAD_HOLDER_EID, -1)
				.putFloat(Entity.DATA_SCALE, 1f)
				.putString(Entity.DATA_NAMETAG, title)
				.putInt(Entity.DATA_SCALE, 0);
        packet.metadata = dataProperties;
        packet.x = (float) player.getX();
        packet.y = (float) player.getY()-20;
        packet.z = (float) player.getZ();
        packet.speedX = 0;
        packet.speedY = 0;
        packet.speedZ = 0;
        player.dataPacket(packet);
        BossEventPacket bpk = new BossEventPacket();
        bpk.bossEid = eid;
        bpk.type = 0;
        player.dataPacket(bpk);

    }
	public static void setVida(Player player, int eid, int percentage){
		if(percentage > 100){
			percentage = 100;
		}
		if(percentage < 0){
			percentage = 0;
		}
		UpdateAttributesPacket upk = new UpdateAttributesPacket(); 
		upk.entityId = eid;
		Attribute attr = Attribute.getAttribute(Attribute.MAX_HEALTH);
		attr.setMaxValue(100);
		attr.setValue((float) percentage);
		upk.entries = new Attribute[]{ attr };
		player.dataPacket(upk);
		BossEventPacket bpk = new BossEventPacket();
		bpk.bossEid = eid;
		bpk.type = 1;
		player.dataPacket(bpk);
	}
	public static void sendTitle(Player player, int eid, String title){
		SetEntityDataPacket npk = new SetEntityDataPacket();
		long flags = 0;
		flags |= 1 << Entity.DATA_FLAG_SILENT;
		flags |= 1 << Entity.DATA_FLAG_INVISIBLE;
		flags |= 1 << Entity.DATA_FLAG_NO_AI;
		EntityMetadata dataProperties = new EntityMetadata()
				.putLong(Entity.DATA_FLAGS, flags)
				.putShort(Entity.DATA_AIR, 400)
				.putShort(Entity.DATA_MAX_AIR, 400)
				.putLong(Entity.DATA_LEAD_HOLDER_EID, -1)
				.putFloat(Entity.DATA_SCALE, 1f)
				.putString(Entity.DATA_NAMETAG, title)
				.putInt(Entity.DATA_SCALE, 0);
		npk.metadata = dataProperties;
		npk.eid = eid;
		player.dataPacket(npk);
		BossEventPacket bpk = new BossEventPacket();
		bpk.bossEid = eid;
		bpk.type = 1; 
		player.dataPacket(bpk);
	}
	public static boolean removeBossBar(Player player, int eid){
		RemoveEntityPacket pk = new RemoveEntityPacket();
		pk.eid = eid;
		player.dataPacket(pk);
		return true;
	}
	public static int getIDBoss(){
		int vector = 0;
		int numero = (int) (Math.random() * 1005);
		int numero1 = (int) (Math.random() * 20);
		int numero2 = (int) (Math.random() * 666);
		vector = numero+numero1+numero2;
		return vector;
		}
}
