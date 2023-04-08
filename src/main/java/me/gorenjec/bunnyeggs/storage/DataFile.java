package me.gorenjec.bunnyeggs.storage;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;

/**
 * A wrapper for a file that holds information that can be written to and read from.
 * Upon instantiation, will create a new file at the given directory if there is no matching file there.
 */
public abstract class DataFile {

    private static final Logger logger = getLogger("DataFile");

    protected final File file;
    protected final FileConfiguration data;

    /**
     * Creates a new storage file in the given directory and with the given name.
     * If the file already exists, the existing file will be used.
     *
     * @param directory The directory to act in
     * @param name The file name to look for. If the file must be created, this will be its name
     * @throws IOException If anything goes wrong while finding or creating the file at the given path and file name
     */
    public DataFile(Path directory, String name, JavaPlugin instance) throws IOException {
        try {
            file = new File(directory.toFile(), name + ".yml");
            if (!file.exists()) {
                instance.saveResource(name + ".yml", false);
                file.createNewFile();
            }
            data = YamlConfiguration.loadConfiguration(file);
        } catch (IOException e) {
            logger.severe("Unable to set up " + name + ".yml file");
            throw e;
        }
    }

    private static String parsePath(String path) {
        return path.replace("/", File.separator);
    }

    /**
     * Flushes any changes made since the last save of the file to the actual file wrapped by this object
     *
     * @throws IOException If something went wrong saving changes
     */
    public final void flush() throws IOException {
        data.save(file);
    }
}
