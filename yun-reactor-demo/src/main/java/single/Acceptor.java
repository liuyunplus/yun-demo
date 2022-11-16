package single;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Acceptor implements Runnable {

    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    public Acceptor(ServerSocketChannel serverSocketChannel, Selector selector) {
        this.serverSocketChannel = serverSocketChannel;
        this.selector = selector;
    }

    @Override
    public void run() {
        SocketChannel channel = null;
        try {
            channel = this.serverSocketChannel.accept();
            //设置为非阻塞
            channel.configureBlocking(false);
            //将连接注册到Selector，只注册读事件
            channel.register(selector, SelectionKey.OP_READ);

            System.out.println("新连接:" + channel.getRemoteAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
