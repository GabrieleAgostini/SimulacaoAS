import java.util.ArrayList;
import java.util.List;

public class LRUAproximado {
    private int frameSize;
    private List<Integer> ageBits;
    private int pageFaults;

    public LRUAproximado(int frameSize) {
        this.frameSize = frameSize;
        this.ageBits = new ArrayList<>(frameSize);
        for (int i = 0; i < frameSize; i++) {
            ageBits.add(0); // Inicializa todos os bits de idade como 0
        }
        this.pageFaults = 0;
    }

    public int execute(List<MemoryOperation> operations, int bitsToShift) {
        for (MemoryOperation operation : operations) {
            String address = operation.getAddress();

            // Verifica se a página está no quadro
            if (ageBits.contains(address)) {
                // Página já está no quadro, continua para a próxima operação
            } else {
                // Trata como falta de página (page fault) e atualiza a lista de bits de idade
                pageFaults++;
                if (ageBits.size() >= frameSize) {
                    // Se o quadro tá cheio, escolhe a página menos recente (menor valor de bits) para substituição
                    int pageIndexToReplace = findPageToReplace();
                    ageBits.remove(pageIndexToReplace);
                }
                // Adiciona a página atual ao quadro e inicia seu bit de idade com 1
                ageBits.add(0, 0);
            }

            // Desloca/envelhece os bits de idade
            for (int i = 0; i < ageBits.size(); i++) {
                int age = ageBits.get(i);
                ageBits.set(i, age >>> bitsToShift);
            }
        }
        return pageFaults;
    }

    private int findPageToReplace() {
        int minAge = ageBits.get(0);
        int pageIndex = 0;
        for (int i = 1; i < ageBits.size(); i++) {
            int age = ageBits.get(i);
            if (age < minAge) {
                minAge = age;
                pageIndex = i;
            }
        }
        return pageIndex;
    }
}
