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

package autosaveworld.modules.worldregen;

import java.util.List;

import autosaveworld.core.logging.MessageLogger;
import autosaveworld.threads.restart.RestartWaiter;

public class WorldRegenJVMshutdownhook extends Thread {

	private List<WorldRegenTask> tasks;

	public WorldRegenJVMshutdownhook(List<WorldRegenTask> tasks) {
		this.tasks = tasks;
	}

	@Override
	public void run() {

		Thread.currentThread().setName("AutoSaveWorld WorldRegenShutdownHook");

		// Do tasks
		try {
			for (WorldRegenTask task : tasks) {
				task.run();
			}
		} catch (Throwable t) {
			t.printStackTrace();
			MessageLogger.printOutDebug("WorldRegen failed");
		}

		// Signal that restarthook can restart
		RestartWaiter.decrementWait();
	}

}
