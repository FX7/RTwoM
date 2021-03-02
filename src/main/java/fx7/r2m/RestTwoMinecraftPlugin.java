package fx7.r2m;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import fx7.r2m.access.AccessManager;
import fx7.r2m.access.AppAccess;
import fx7.r2m.access.EntityAccess;
import fx7.r2m.entity.script.RestActionStub;
import fx7.r2m.entity.script.ScriptEntity;
import fx7.r2m.entity.script.ScriptManager;
import fx7.r2m.rest.RestException;
import fx7.r2m.rest.server.RestServer;
import fx7.r2m.rest.server.RestServerConfig;

public class RestTwoMinecraftPlugin extends JavaPlugin implements Coordinator
{
	private static final String CONSOLE_MESSAGE_PREFIX = "[R2M] ";

	private static final String STORE_KEY_APP_ACCESS = "appAccess";
	private List<AppAccess> appAccess;

	private static final String STORE_KEY_REST_SERVER_CONFIG = "restServerConfig";
	private RestServerConfig restServerConfig;

	private static final String STORE_KEY_SCRIPTS = "scripts";
	private List<ScriptEntity> scripts;

	private AccessManager accessManager;
	private ScriptManager scriptManager;
	private RestServer restServer;

	private YamlConfiguration customConfig = null;
	private File customConfigFile = null;

	@Override
	public void onDisable()
	{
		restServer.shutdownServer();
		storeConfigs();
	}

	private void storeConfigs()
	{
		getConfig().set(STORE_KEY_APP_ACCESS, this.accessManager.getAppAccesss());
		getConfig().set(STORE_KEY_REST_SERVER_CONFIG, this.restServer.getConfig());
		getScriptsConfigFile().set(STORE_KEY_SCRIPTS, this.scriptManager.getScripts());
		try
		{
			getScriptsConfigFile().save(customConfigFile);
		} catch (IOException e)
		{
			getLogger().log(Level.SEVERE, "Could not save config to " + customConfigFile, e);
		}
		saveConfig();
	}

	@Override
	public void onEnable()
	{
		if (this.accessManager == null)
			this.accessManager = new AccessManager(this.getAppAccesss());
		if (this.scriptManager == null)
			this.scriptManager = new ScriptManager(this.getScripts());
		if (this.restServer == null)
			this.restServer = new RestServer(this);

		try
		{
			restServer.shutdownServer();
			restServer.startServer();
			Bukkit.getConsoleSender().sendMessage("server started localhost:8001");
		} catch (IOException e)
		{
			sendConsoleMessage("IOException: " + e.getMessage());
		}

	}

	@Override
	public RestServerConfig getRestServerConfig()
	{
		RestServerConfig restServerConfig = this.restServerConfig;
		if (restServerConfig == null)
		{
			restServerConfig = (RestServerConfig) getConfig().get(STORE_KEY_REST_SERVER_CONFIG);
			if (restServerConfig == null)
				restServerConfig = RestServerConfig.getDefaultConfig();
			this.restServerConfig = restServerConfig;
		}
		return restServerConfig;
	}

	@Override
	public FileConfiguration getConfig()
	{
		// TODO why not in onEnable? tried it many times, but doesnt work
		registerConfigurationSerializables();
		return super.getConfig();
	}

	private void registerConfigurationSerializables()
	{
		ConfigurationSerialization.registerClass(AppAccess.class);
		ConfigurationSerialization.registerClass(EntityAccess.class);
		ConfigurationSerialization.registerClass(ScriptEntity.class);
		ConfigurationSerialization.registerClass(RestActionStub.class);
		ConfigurationSerialization.registerClass(RestServerConfig.class);
	}

	private List<AppAccess> getAppAccesss()
	{
		List<AppAccess> appAccess = this.appAccess;
		if (appAccess == null)
		{
			appAccess = (List<AppAccess>) getConfig().getList(STORE_KEY_APP_ACCESS);
			if (appAccess == null)
				appAccess = new ArrayList<>();
			this.appAccess = appAccess;
		}
		return appAccess;
	}

	private List<ScriptEntity> getScripts()
	{
		List<ScriptEntity> scripts = this.scripts;
		if (scripts == null)
		{
			YamlConfiguration scriptsConfig = getScriptsConfigFile();
			scripts = (List<ScriptEntity>) scriptsConfig.getList(STORE_KEY_SCRIPTS);
			if (scripts == null)
				scripts = new ArrayList<>();
			this.scripts = scripts;
		}
		return scripts;
	}

	private YamlConfiguration getScriptsConfigFile()
	{
		if (customConfig == null)
		{
			customConfigFile = new File(getDataFolder(), "scripts.yml");
			customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
		}
		return customConfig;
	}

	@Override
	public AccessManager getAccessManager()
	{
		return accessManager;
	}

	@Override
	public ScriptManager getScriptManager()
	{
		return scriptManager;
	}

	@Override
	public void sendConsoleMessage(String message)
	{
		if (message == null || message.isEmpty())
			return;

		Bukkit.getConsoleSender().sendMessage(CONSOLE_MESSAGE_PREFIX + message);
	}

	@Override
	public <T> T callSyncMethod(Callable<T> callable) throws RestException
	{
		Future<T> future = Bukkit.getScheduler().callSyncMethod(this, callable);
		try
		{
			return future.get();
		} catch (InterruptedException | ExecutionException e)
		{
			if (e.getCause() instanceof RestException)
				throw (RestException) e.getCause();
			throw RestException.unexpectedException(e);
		}
	}
}
