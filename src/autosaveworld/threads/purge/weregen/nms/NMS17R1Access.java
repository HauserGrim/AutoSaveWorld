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

package autosaveworld.threads.purge.weregen.nms;

import net.minecraft.server.v1_7_R1.NBTTagCompound;
import net.minecraft.server.v1_7_R1.Block;
import net.minecraft.server.v1_7_R1.Chunk;
import net.minecraft.server.v1_7_R1.TileEntity;
import net.minecraft.server.v1_7_R1.WorldServer;

import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R1.CraftWorld;

import autosaveworld.threads.purge.weregen.NMSWorldEditRegeneration.NMSBlock;

import com.sk89q.worldedit.Vector;

public class NMS17R1Access implements NMSAccess {

	@Override
	public Object generateNMSChunk(World world, int cx, int cz) {
		WorldServer nmsWorld = ((CraftWorld)world).getHandle();
		return nmsWorld.chunkProviderServer.chunkProvider.getOrCreateChunk(cx, cz);
	}

	@Override
	public void setBlockTileEntity(World world, Vector pt, Object tileEntity) {
		int x = pt.getBlockX();
		int y = pt.getBlockY();
		int z = pt.getBlockZ();
		WorldServer nmsWorld = ((CraftWorld)world).getHandle();
		NBTTagCompound tag = new NBTTagCompound();
		((TileEntity) tileEntity).b(tag);
		nmsWorld.getTileEntity(x, y, z).a(tag);
	}

	@Override
	public NMSBlock getBlock(Object nmsChunk, Vector pt) {
		Chunk chunk = (Chunk) nmsChunk;
		return new NMSBlock(
			Block.b(chunk.getType(pt.getBlockX() & 0xF, pt.getBlockY(), pt.getBlockZ() & 0xF)),
			(byte) chunk.getData(pt.getBlockX() & 0xF, pt.getBlockY(), pt.getBlockZ() & 0xF),
			chunk.world.getTileEntity(pt.getBlockX(), pt.getBlockY(), pt.getBlockZ())
		);
	}

}
