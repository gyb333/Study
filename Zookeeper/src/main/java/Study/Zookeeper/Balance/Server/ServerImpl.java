package Study.Zookeeper.Balance.Server;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ServerImpl implements Server {

	private EventLoopGroup bossGroup = new NioEventLoopGroup();
	private EventLoopGroup workGroup = new NioEventLoopGroup();
	private ServerBootstrap bootStrap = new ServerBootstrap();
	private ChannelFuture cf;

	private String serversPath;
	private String currentServerPath;
	private ServerData sd;

	private volatile boolean binded = false;

 
	private final RegistProvider registProvider;

	public ServerImpl(String serversPath, ServerData sd) {

		this.serversPath = serversPath;
 
		this.registProvider = new DefaultRegistProvider();
		this.sd = sd;
	}

	// 初始化服务端
	private void initRunning() throws Exception {

		String mePath = serversPath.concat("/").concat(sd.getPort().toString());
		// 注册到zookeeper
		registProvider.regist(new ZooKeeperRegistContext(mePath, sd));
		currentServerPath = mePath;
	}

	@Override
	public void bind() {

		if (binded) {
			return;
		}

		System.out.println(sd.getPort() + ":binding...");

		try {
			initRunning();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		bootStrap.group(bossGroup, workGroup)
				.channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 1024)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel ch) throws Exception {
						ChannelPipeline p = ch.pipeline();
						p.addLast(new ServerHandler(new DefaultBalanceUpdateProvider(currentServerPath)));
					}
				});

		try {
			cf = bootStrap.bind(sd.getPort()).sync();
			binded = true;
			System.out.println(sd.getPort() + ":binded...");
			cf.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}

	}

	public String getCurrentServerPath() {
		return currentServerPath;
	}

	public String getServersPath() {
		return serversPath;
	}

	public ServerData getSd() {
		return sd;
	}

	public void setSd(ServerData sd) {
		this.sd = sd;
	}

}
