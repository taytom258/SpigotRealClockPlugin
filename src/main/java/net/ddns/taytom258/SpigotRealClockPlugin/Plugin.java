package net.ddns.taytom258.SpigotRealClockPlugin;

import java.io.File;
import java.io.IOException;

import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import net.ddns.taytom258.SpigotRealClockPlugin.commands.ClockCommand;
import net.ddns.taytom258.SpigotRealClockPlugin.commands.CommandHandler;
import net.ddns.taytom258.SpigotRealClockPlugin.config.ConfigHandler;
import net.ddns.taytom258.SpigotRealClockPlugin.config.Configuration;
import net.ddns.taytom258.SpigotRealClockPlugin.geoIP.GeoIP;
import net.ddns.taytom258.SpigotRealClockPlugin.listeners.JoinListener;
import net.ddns.taytom258.SpigotRealClockPlugin.listeners.ListenerHandler;
import net.ddns.taytom258.SpigotRealClockPlugin.logger.LogHandler;
import net.ddns.taytom258.SpigotRealClockPlugin.reference.Strings;

/**
 * Main plugin class
 * 
 * @author taytom258
 * @see JavaPlugin
 *
 */
public class Plugin extends JavaPlugin {

	public static String ver;

	@Override
	public void onLoad() {
		
		// Plugin properties
		ver = "1.0";

		// Log Initialization
		LogHandler.init();
	}
	
	@Override
	public void onEnable() {
		
		// Config Loader
		ConfigHandler.init();
		ConfigHandler.checkConfig();
		
		// GeoDB Checker & Initializer
		File db = new File(Strings.geodb);
		if (!db.exists()) {
			LogHandler.warning(Strings.dberror);
			this.saveResource(Strings.geojar, true);
			GeoIP.init();
		} else {
			LogHandler.info(Strings.dbsuccess);
			GeoIP.init();
		}

		// Register & Initialize Commands
		ClockCommand.init();
		CommandHandler.registerCommand("clock", new ClockCommand());
		
		// Register Listeners
		ListenerHandler.registerListener(new JoinListener());
		
		//TimeZoneDB Checker
		if (Configuration.api.equalsIgnoreCase("InsertKeyHere") || Configuration.api.equalsIgnoreCase("")){
			LogHandler.severe(Strings.apierror, true);
		}
		
		//Metrics Loader
		Metrics m;
		try {
			m = new Metrics(this);
			m.start();
		} catch (IOException e) {
			LogHandler.warning("", e);
		}
		

	}

	@Override
	public void onDisable() {

		GeoIP.deinit();
		ClockCommand.deinit();
	}

}
