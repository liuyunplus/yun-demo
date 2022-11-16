package single;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Reactor implements Runnable {

    private Selector selector;

    private ServerSocketChannel serverSocketChannel;


    public Reactor(Integer port) throws IOException {
        this.selector = Selector.open();
        this.serverSocketChannel = ServerSocketChannel.open();
        //绑定端口
        this.serverSocketChannel.bind(new InetSocketAddress(port));
        //设置为非阻塞
        this.serverSocketChannel.configureBlocking(false);
        //注册到选择器上面
        this.serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    @Override
    public void run() {
        while (true) {
            try {
                // 阻塞等待
                int select = selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    //分发事件
                    dispatch(selectionKey);
                    iterator.remove();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 进行事件分发
     *
     * @param selectionKey
     */
    private void dispatch(SelectionKey selectionKey) {
        if (selectionKey.isAcceptable()) {
            //连接事件
            new Acceptor(serverSocketChannel, selector).run();
        } else {
            //读写事件
            new Handler(selectionKey).run();
        }
    }

}
