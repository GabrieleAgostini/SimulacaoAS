import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class FIFO {
    private Queue<String> frameQueue;
    private int frameSize;
    private int pageFaults;

    public FIFO(int frameSize) {
        this.frameQueue = new LinkedList<>();
        this.frameSize = frameSize;
        this.pageFaults = 0;
    }

    public int execute(List<MemoryOperation> operations) {
        for (MemoryOperation operation : operations) {
            String address = operation.getAddress();

            // Verifica se a página está no quadro
            if (frameQueue.contains(address)) {
                // Página já está no quadro, continua para a próxima operação
            } else {
                // Trata como falta de página (page fault) e atualiza a fila
                pageFaults++;
                if (frameQueue.size() >= frameSize) {
                    // Se o quadro está cheio, remove a página mais antiga
                    frameQueue.poll();
                }
                // Adiciona a página atual ao quadro
                frameQueue.offer(address);
            }
        }
        return pageFaults;
    }
}
