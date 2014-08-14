/**
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */

package autosaveworld.threads.worldregen.pstones;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;

import net.sacredlabyrinth.Phaed.PreciousStones.PreciousStones;
import net.sacredlabyrinth.Phaed.PreciousStones.vectors.Field;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.util.Vector;

import autosaveworld.core.GlobalConstants;
import autosaveworld.core.logging.MessageLogger;
import autosaveworld.threads.worldregen.SchematicData.SchematicToSave;
import autosaveworld.threads.worldregen.SchematicOperations;

import com.sk89q.worldedit.bukkit.BukkitUtil;

public class PStonesCopy {

	private World wtoregen;

	public PStonesCopy(String worldtoregen) {
		wtoregen = Bukkit.getWorld(worldtoregen);
	}

	public void copyAllToSchematics() {
		MessageLogger.debug("Saving PreciousStones regions to schematics");

		PreciousStones pstones = PreciousStones.getInstance();

		new File(GlobalConstants.getPStonesTempFolder()).mkdirs();

		for (Field field : pstones.getForceFieldManager().getFields("*", wtoregen)) {
			MessageLogger.debug("Saving PreciousStones Region " + field.getId() + " to schematic");
			Vector min = new Vector(field.getMinx(), field.getMiny(), field.getMinz());
			Vector max = new Vector(field.getMaxx(), field.getMaxy(), field.getMaxz());
			SchematicToSave schematicdata = new SchematicToSave(GlobalConstants.getPStonesTempFolder() + field.getId(), BukkitUtil.toVector(min), BukkitUtil.toVector(max));
			SchematicOperations.saveToSchematic(wtoregen, new LinkedList<SchematicToSave>(Arrays.asList(schematicdata)));
			MessageLogger.debug("PreciousStones Region " + field.getId() + " saved");
		}

	}

}