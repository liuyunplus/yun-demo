package single;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class Handler implements Runnable {

    private SelectionKey selectionKey;

    public Handler(SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
    }

    @Override
    public void run() {
        try {
            if (selectionKey.isReadable()) {
                //读事件处理
                this.read();
            } else {
                //写事件处理
                this.write();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理读事件
     *
     * @throws IOException
     */
    private void read() throws IOException {
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        ByteBuffer allocate = ByteBuffer.allocate(1024);
        int read = channel.read(allocate);
        if (read > 0) {
            String message = new String(allocate.array(), 0, read);
            System.out.println(String.format("[%s]发送消息:%s", channel.getRemoteAddress(), message));
            // 返回响应
            String response = String.format("You Message Is: %s\n", message);
            channel.write(ByteBuffer.wrap(response.getBytes()));
        }
    }

    /**
     * 处理写事件
     *
     * @throws IOException
     */
    private void write() throws IOException {

    }

}
