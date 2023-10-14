package api;

import java.util.ArrayList;
import java.util.List;

public class LRUAproximado {
    private int frameSize;
    private List<Integer> ageBits;
    private int pageFaults;

    // Construtor
    public LRUAproximado(int frameSize) {
        this.frameSize = frameSize;
        this.ageBits = new ArrayList<>(frameSize);
        for (int i = 0; i < frameSize; i++) {
            ageBits.add(0);
        }
        this.pageFaults = 0;
    }

    // recebe a lista e o numero de bytes como entrada
    // simula o comportamento do algoritimo LRU
    public int execute(List<MemoryOperation> operations, int bitsToShift) {
        for (MemoryOperation operation : operations) {
            String address = operation.getAddress();

            if (ageBits.contains(address)) {
            } else {
                pageFaults++;
                if (ageBits.size() >= frameSize) {
                    int pageIndexToReplace = findPageToReplace();
                    ageBits.remove(pageIndexToReplace);
                }
                ageBits.add(0, 0);
            }

            for (int i = 0; i < ageBits.size(); i++) {
                int age = ageBits.get(i);
                ageBits.set(i, age >>> bitsToShift);
            }
        }
        return pageFaults;
    }

    // percorre a lista retorna o indice da pagina e o numero total de falhas
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
