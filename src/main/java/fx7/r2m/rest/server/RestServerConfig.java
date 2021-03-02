package fx7.r2m.rest.server;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

@SerializableAs("RestServerConfig")
public class RestServerConfig implements ConfigurationSerializable
{
	private static final String DEFAULT_HOST = "localhost";
	private static final String PARAM_HOST = "host";
	private String host;

	private static final int DEFAULT_PORT = 8080;
	private static final String PARAM_PORT = "port";
	private int port;

	private static final int DEFAULT_BACKLOG = 0;
	private static final String PARAM_BACKLOG = "backlog";
	private int backlog;

	public String getHost()
	{
		return host;
	}

	public int getPort()
	{
		return port;
	}

	public int getBacklog()
	{
		return backlog;
	}

	private RestServerConfig(String host, int port, int backlog)
	{
		this.host = host;
		this.port = port;
		this.backlog = backlog;
	}

	public static RestServerConfig getDefaultConfig()
	{
		return new RestServerConfig(DEFAULT_HOST, DEFAULT_PORT, DEFAULT_BACKLOG);
	}

	public static RestServerConfig deserialize(Map<String, Object> serialized)
	{
		String host = (String) serialized.get(PARAM_HOST);
		if (host == null || host.isEmpty())
			host = DEFAULT_HOST;
		Integer port = (Integer) serialized.get(PARAM_PORT);
		if (port == null)
			port = DEFAULT_PORT;
		Integer backlog = (Integer) serialized.get(PARAM_BACKLOG);
		if (backlog == null)
			backlog = DEFAULT_BACKLOG;
		RestServerConfig player = new RestServerConfig(//
				host, //
				port, //
				backlog);

		return player;
	}

	@Override
	public Map<String, Object> serialize()
	{
		Map<String, Object> serialized = new HashMap<String, Object>();

		serialized.put(PARAM_HOST, host);
		serialized.put(PARAM_PORT, port);
		serialized.put(PARAM_BACKLOG, backlog);

		return serialized;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + port;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RestServerConfig other = (RestServerConfig) obj;
		if (host == null)
		{
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (port != other.port)
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "RestServerConfig [host=" + host + ", port=" + port + ", backlog=" + backlog + "]";
	}
}
