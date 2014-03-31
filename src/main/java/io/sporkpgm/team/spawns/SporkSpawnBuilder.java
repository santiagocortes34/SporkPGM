package io.sporkpgm.team.spawns;

import io.sporkpgm.map.SporkMap;
import io.sporkpgm.module.exceptions.ModuleLoadException;
import io.sporkpgm.region.Region;
import io.sporkpgm.region.RegionBuilder;
import io.sporkpgm.region.exception.InvalidRegionException;
import io.sporkpgm.team.SporkTeam;
import io.sporkpgm.team.spawns.kits.SporkKit;
import io.sporkpgm.util.XMLUtil;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

public class SporkSpawnBuilder {

	public static List<SporkSpawn> build(SporkMap map) throws ModuleLoadException, InvalidRegionException {
		List<SporkSpawn> sporks = new ArrayList<>();
		Document document = map.getDocument();
		Element root = document.getRootElement();

		for(Element spawns : XMLUtil.getElements(root, "spawns")) {
			String teamS = spawns.attributeValue("team");

			sporks.addAll(parseSpawns(map, XMLUtil.getElements(spawns, "spawn", "default"), teamS));
			if(spawns.element("spawns") != null) {
				sporks.addAll(parseSpawns(map, XMLUtil.getElements(spawns.element("spawns"), "spawn", "default"), teamS));
			}
		}

		return sporks;
	}

	public static List<SporkSpawn> parseSpawns(SporkMap map, List<Element> spawns, String teamS) throws ModuleLoadException, InvalidRegionException {
		List<SporkSpawn> sporks = new ArrayList<>();

		for(Element element : spawns) {
			SporkSpawn spawn = parseSpawn(map, element, teamS);
			sporks.add(spawn);
		}

		return sporks;
	}

	public static SporkSpawn parseSpawn(SporkMap map, Element element, String teamS) throws ModuleLoadException, InvalidRegionException {
		String nameS = null;

		teamS = (element.attributeValue("team") != null ? element.attributeValue("team") : teamS);
		if(element.getName().equalsIgnoreCase("default")) {
			teamS = map.getObservers().getName();
		}

		String yawS = element.attributeValue("yaw");
		String pitchS = element.attributeValue("pitch");
		String kitS = element.attributeValue("kit");

		String name = (nameS == null ? "noname" : nameS);
		SporkTeam team = map.getTeam(teamS);

		List<Region> regions = RegionBuilder.parseSubRegions(element);
		float yaw = 0;
		float pitch = 0;

		try {
			yaw = Float.parseFloat(yawS);
		} catch(Exception ignored) {
		}

		try {
			pitch = Float.parseFloat(pitchS);
		} catch(Exception ignored) {
		}

		SporkKit match = null;
		if(kitS != null) {
			for(SporkKit ak : map.getKits()) {
				if(ak.getName().equalsIgnoreCase(kitS)) {
					match = ak;
					break;
				}
			}
			if(match == null)
				throw new ModuleLoadException("Kit `" + kitS + "` not found while parsing `" + teamS + "` spawn!");
		}

		SporkSpawn spawn = new SporkSpawn(name, regions, match, yaw, pitch);
		team.getSpawns().add(spawn);
		return spawn;
	}

}
