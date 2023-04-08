package me.gorenjec.bunnyeggs.storage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.gorenjec.bunnyeggs.BunnyEggs;
import me.gorenjec.bunnyeggs.cache.InMemoryCache;
import me.gorenjec.bunnyeggs.models.BunnyEgg;
import me.gorenjec.bunnyeggs.models.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SQLStorage {
    private HikariDataSource dataSource;
    private final BunnyEggs instance;
    private final InMemoryCache inMemoryCache;
    private final FileConfiguration config;
    private static final String PLAYERDATA_TABLE = "player_data";
    private static final String EGG_PLAYERDATA_TABLE = "egg_player_data";

    public SQLStorage(BunnyEggs instance) {
        this.instance = instance;
        this.inMemoryCache = instance.getInMemoryCache();
        this.config = instance.getConfig();

        boolean useMySQL = config.getBoolean("data_storage.use_mysql");
        String path = instance.getDataFolder().getPath();

        HikariConfig hikariConfig = new HikariConfig();

        if (useMySQL) {
            hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
            hikariConfig.setUsername(config.getString("data_storage.username"));
            hikariConfig.setPassword(config.getString("data_storage.password"));
            String hostname = config.getString("data_storage.ip");
            String port = config.getString("data_storage.port");
            String database = config.getString("data_storage.database");
            String useSSL = config.getBoolean("data_storage.database") ? "true" : "false";
            hikariConfig.setJdbcUrl("jdbc:mysql://" + hostname + ":" + port + "/" + database + "?useSSL=" + useSSL);
        } else {
            hikariConfig.setDriverClassName("org.sqlite.JDBC");
            hikariConfig.setJdbcUrl("jdbc:sqlite:" + path + "/database.sqlite");
        }

        hikariConfig.setPoolName("BunnyEggsPlugin");
        hikariConfig.setMaxLifetime(60000);
        hikariConfig.setMaximumPoolSize(10);
        hikariConfig.addDataSourceProperty("database", config.getString("data_storage.database"));

        this.dataSource = new HikariDataSource(hikariConfig);

        constructTables();
    }

    private void constructTables() {
        boolean mysql = instance.getConfig().getBoolean("data_storage.use_mysql");
        String autoInc = mysql ? "AUTO_INCREMENT" : "AUTOINCREMENT";

        try (Connection connection = dataSource.getConnection()) {
            String createPlayerDataTableSql = "CREATE TABLE IF NOT EXISTS " + PLAYERDATA_TABLE + "("
                    + "uuid VARCHAR(36) PRIMARY KEY, "
                    + "points BIGINT(20) "
                    + ")";

            String createEggPlayerDataTableSql = "CREATE TABLE IF NOT EXISTS " + EGG_PLAYERDATA_TABLE + "("
                    + "id INTEGER PRIMARY KEY " + autoInc + ","
                    + "uuid VARCHAR(36), "
                    + "egg TEXT, "
                    + "world TEXT, "
                    + "loc_x INT, "
                    + "loc_y INT, "
                    + "loc_z INT"
                    + ")";

            PreparedStatement playerDataStatement = connection.prepareStatement(createPlayerDataTableSql);
            PreparedStatement eggPlayerDataStatement = connection.prepareStatement(createEggPlayerDataTableSql);

            playerDataStatement.execute();
            eggPlayerDataStatement.execute();

            instance.getLogger().info("Verified data tables.");
        } catch (SQLException e) {
            instance.getLogger().severe("Could not create tables!");
            e.printStackTrace();
        }
    }

    public void insertPlayerData(PlayerProfile playerProfile) {
        String sql = "INSERT INTO " + PLAYERDATA_TABLE + " (uuid, points) VALUES (?, ?) ON DUPLICATE KEY UPDATE uuid = ?;";

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            Player player = playerProfile.getPlayer();
            UUID uuid = player.getUniqueId();
            int points = playerProfile.getPoints();

            statement.setString(1, uuid.toString());
            statement.setInt(2, points);
            statement.setString(3, uuid.toString());

            statement.execute();
        } catch (SQLException e) {
            instance.getLogger().severe("Could not insert player data!");
            e.printStackTrace();
        }
    }

    public void updatePlayerData(PlayerProfile playerProfile) {
        String sql = "UPDATE " + PLAYERDATA_TABLE + " SET points = ? WHERE uuid = ?;";

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            Player player = playerProfile.getPlayer();
            UUID uuid = player.getUniqueId();
            int points = playerProfile.getPoints();

            statement.setInt(1, points);
            statement.setString(2, uuid.toString());

            statement.execute();
        } catch (SQLException e) {
            instance.getLogger().severe("Could not store player data!");
            e.printStackTrace();
        }
    }

    public void insertCollectedEgg(PlayerProfile playerProfile, Location location, BunnyEgg bunnyEgg) {
        String sql = "INSERT INTO " + EGG_PLAYERDATA_TABLE + " (uuid, egg, world, loc_x, loc_y, loc_z) VALUES (?, ?, ?, ?, ?, ?);";

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            Player player = playerProfile.getPlayer();
            UUID uuid = player.getUniqueId();
            World world = location.getWorld();
            String worldName = world.getName();
            int x = location.getBlockX();
            int y = location.getBlockY();
            int z = location.getBlockZ();

            statement.setString(1, uuid.toString());
            statement.setString(2, bunnyEgg.id());
            statement.setString(3, worldName);
            statement.setInt(4, x);
            statement.setInt(5, y);
            statement.setInt(6, z);

            statement.execute();
        } catch (SQLException e) {
            instance.getLogger().severe("Could not insert collected egg!");
            e.printStackTrace();
        }
    }

    public int getPoints(UUID uuid) {
        String sql = "SELECT points FROM " + PLAYERDATA_TABLE + " WHERE uuid = ? LIMIT 1;";

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, uuid.toString());

            ResultSet playerEggDataSet = statement.executeQuery();

            return playerEggDataSet.getInt("points");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public Map<Location, BunnyEgg> getBunnyEggs(PlayerProfile playerProfile) {
        Map<Location, BunnyEgg> bunnyEggs = new HashMap<>();
        String sql = "SELECT egg, world, loc_x, loc_y, loc_z FROM " + EGG_PLAYERDATA_TABLE + " WHERE uuid = ?;";

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            Player player = playerProfile.getPlayer();
            UUID uuid = player.getUniqueId();

            statement.setString(1, uuid.toString());

            ResultSet playerEggDataSet = statement.executeQuery();

            while (playerEggDataSet.next()) {
                String name = playerEggDataSet.getString("egg");
                String worldName = playerEggDataSet.getString("world");
                World world = Bukkit.getWorld(worldName);
                int x = playerEggDataSet.getInt("loc_x");
                int y = playerEggDataSet.getInt("loc_y");
                int z = playerEggDataSet.getInt("loc_z");
                Location location = new Location(world, x, y, z);
                BunnyEgg bunnyEgg = inMemoryCache.getBunnyEgg(location);

                bunnyEggs.put(location, bunnyEgg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bunnyEggs;
    }

    public Map<Location, BunnyEgg> getBunnyEggs(UUID uuid) {
        Map<Location, BunnyEgg> bunnyEggs = new HashMap<>();
        String sql = "SELECT egg, world, loc_x, loc_y, loc_z FROM " + EGG_PLAYERDATA_TABLE + " WHERE uuid = ?;";

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, uuid.toString());

            ResultSet playerEggDataSet = statement.executeQuery();

            while (playerEggDataSet.next()) {
                String name = playerEggDataSet.getString("egg");
                String worldName = playerEggDataSet.getString("world");
                World world = Bukkit.getWorld(worldName);
                int x = playerEggDataSet.getInt("loc_x");
                int y = playerEggDataSet.getInt("loc_y");
                int z = playerEggDataSet.getInt("loc_z");
                Location location = new Location(world, x, y, z);
                BunnyEgg bunnyEgg = inMemoryCache.getBunnyEgg(location);

                bunnyEggs.put(location, bunnyEgg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bunnyEggs;
    }



    public HikariDataSource getDataSource() {
        return dataSource;
    }
}
