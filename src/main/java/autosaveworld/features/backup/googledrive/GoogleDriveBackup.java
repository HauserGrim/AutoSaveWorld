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

package autosaveworld.features.backup.googledrive;

import autosaveworld.config.AutoSaveWorldConfig;
import autosaveworld.core.AutoSaveWorld;
import autosaveworld.features.backup.Backup;
import autosaveworld.features.backup.utils.virtualfilesystem.VirtualBackupManager;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

public class GoogleDriveBackup extends Backup {

	public GoogleDriveBackup() {
		super("Google Drive");
	}


	public void performBackup() throws IOException, GeneralSecurityException {
		AutoSaveWorldConfig config = AutoSaveWorld.getInstance().getMainConfig();

		GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(config.backupGDriveAuthFile))
				.createScoped(Collections.singletonList(DriveScopes.DRIVE));
		Drive driveclient = new Drive.Builder(GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance(), new HttpCredentialsAdapter(credentials))
				.setApplicationName(AutoSaveWorld.getInstance().getName()).build();

		VirtualBackupManager.builder()
				.setBackupPath(config.backupGDrivePath)
				.setWorldList(config.backupGDriveWorldsList)
				.setBackupPlugins(config.backupGDrivePluginsFolder)
				.setOtherFolders(config.backupGDriveOtherFolders)
				.setExcludedFolders(config.backupGDriveExcludeFolders)
				.setMaxBackupNumber(config.backupGDriveMaxNumberOfBackups)
				.setZip(config.backupGDriveZipEnabled)
				.setVFS(new GoogleDriveVirtualFileSystem(driveclient, config.backupGDRiveRootFolder))
				.create().backup();
	}

}
