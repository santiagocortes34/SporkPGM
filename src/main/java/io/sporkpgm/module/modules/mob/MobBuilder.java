package io.sporkpgm.module.modules.mob;

import io.sporkpgm.map.SporkMap;
import io.sporkpgm.module.Module;
import io.sporkpgm.module.builder.Builder;
import io.sporkpgm.module.builder.BuilderInfo;
import io.sporkpgm.module.exceptions.ModuleLoadException;
import io.sporkpgm.region.exception.InvalidRegionException;
import io.sporkpgm.util.XMLUtil;
import org.bukkit.entity.CreatureType;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

@BuilderInfo(documentable = false)
public class MobBuilder extends Builder{
	public MobBuilder(Document document){
		super(document);
	}

	public MobBuilder(SporkMap map){
		super(map);
	}

	@Override
	public List<Module> build() throws ModuleLoadException, InvalidRegionException{
		List<Module> modules = new ArrayList<>();
		Element root = getRoot();

		for (Element e : XMLUtil.getElements(root, "mobs")){
			for (Element filter : XMLUtil.getElements(e, "filter")){
				List<CreatureSpawnEvent.SpawnReason> reasons = new ArrayList<>();
				List<CreatureType> mobs = new ArrayList<>();
				for (Element reason : XMLUtil.getElements(filter, "spawn")){
					String reasonS = reason.getText();
					reasons.add(CreatureSpawnEvent.SpawnReason.valueOf(reasonS));
				}
				for (Element mob : XMLUtil.getElements(filter, "mob")){
					String mobS = mob.getText();
					mobs.add(CreatureType.valueOf(mobS));
				}
			}
		}
		return modules;
	}
}
