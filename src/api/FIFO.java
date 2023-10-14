package api;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class FIFO {
    private Queue<String> frameQueue;
    private int frameSize;
    private int pageFaults;

    // construtor
    public FIFO(int frameSize) {
        this.frameQueue = new LinkedList<>();
        this.frameSize = frameSize;
        this.pageFaults = 0;
    }

    // recebe a lisat como entrada e siumla o comportamento do algoritimo FIFO
    public int execute(List<MemoryOperation> operations) {
        for (MemoryOperation operation : operations) {
            String address = operation.getAddress();

            if (frameQueue.contains(address)) {
            } else {
                pageFaults++;
                if (frameQueue.size() >= frameSize) {
                    frameQueue.poll();
                }
                frameQueue.offer(address);
            }
        }
        return pageFaults;
    }
}
