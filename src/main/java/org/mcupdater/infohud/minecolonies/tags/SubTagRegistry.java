package org.mcupdater.infohud.minecolonies.tags;

import static org.mcupdater.infohud.tags.TagRegistry.register;

public class SubTagRegistry {

	public static void init() {
		register("colony_present", MCTags::isColonyPresent);
		register("colony_name", MCTags::getColonyName);
		register("colony_pop", MCTags::getColonyPopulation);
		register("colony_maxpop", MCTags::getColonyMaxPopulation);
		register("colony_sick", MCTags::getSickColonists);
		register("colony_unemployed", MCTags::getUnemployed);
		register("colony_children", MCTags::getChildren);
		register("colony_starving", MCTags::getStarving);
		register("colony_students", MCTags::getStudents);
		register("colony_workorders", MCTags::getWorkorders);
		register("colony_workorder_full", MCTags::getWorkorderFull);
		register("colony_workorder_exists", MCTags::getWorkorderExists);
		register("colony_workorder_owner", MCTags::getWorkorderOwner);
		register("colony_workorder_detail", MCTags::getWorkorderDetail);
		register("colony_workorder_location", MCTags::getWorkorderLocation);
		register("colony_workorder_stage", MCTags::getWorkorderStage);
		register("colony_willraid", MCTags::getWillRaid);
		register("colony_warehousefree", MCTags::getWarehouseFreeSlots);
		register("colony_requests", MCTags::getRequests);
		register("colony_request", MCTags::getRequest);
	}
}
