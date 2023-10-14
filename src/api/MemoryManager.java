package api;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MemoryManager {
    public static void main(String[] args) {
        String[] traceFiles = {"src//txt//swim.trace"};

        int[] frameSizesToTest = {1024, 2048, 4096, 8192};
        int[] ageBitsToTest = {1, 2, 4, 8};

        for (String traceFile : traceFiles) {
            System.out.println("Resultados para o arquivo " + traceFile + ":");
            List<MemoryOperation> operations = readTraceFile(traceFile);

            for (int frameSizeToTest : frameSizesToTest) {
                int pageFaultsFIFO = executeFIFO(operations, frameSizeToTest);
                System.out.println("FIFO - Quadro: " + frameSizeToTest + ", Faltas de Página: " + pageFaultsFIFO);

                for (int ageBits : ageBitsToTest) {
                    int pageFaultsLRU = executeLRUAproximado(operations, frameSizeToTest, ageBits);
                    System.out.println("LRU(" + ageBits + " bits) - Quadro: " + frameSizeToTest + ", Faltas de Página: " + pageFaultsLRU);
                }
            }
        }
    }

    // le o arquivo e retonra a lista
    private static List<MemoryOperation> readTraceFile(String filename) {
        List<MemoryOperation> operations = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length == 2) {
                    String address = parts[0];
                    char operation = parts[1].charAt(0);
                    operations.add(new MemoryOperation(address, operation));
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return operations;
    }

    // executa o FIFO
    private static int executeFIFO(List<MemoryOperation> operations, int frameSize) {
        Queue<String> frameQueue = new LinkedList<>();
        int pageFaults = 0;
        for (MemoryOperation operation : operations) {
            String address = operation.getAddress();
            if (frameQueue.contains(address)) {
                continue;
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

    // executa o LRU
    private static int executeLRUAproximado(List<MemoryOperation> operations, int frameSize, int ageBits) {
        List<String> frameList = new ArrayList<>();
        List<Integer> ageBitsList = new ArrayList<>(frameSize);
        for (int i = 0; i < frameSize; i++) {
            ageBitsList.add(0);
        }
        int pageFaults = 0;
        for (MemoryOperation operation : operations) {
            String address = operation.getAddress();
            if (frameList.contains(address)) {
                frameList.remove(address);
                frameList.add(address);
            } else {
                pageFaults++;
                if (frameList.size() >= frameSize) {
                    int pageIndexToReplace = findPageToReplace(ageBitsList);
                    frameList.remove(pageIndexToReplace);
                    ageBitsList.remove(pageIndexToReplace);
                }
                frameList.add(address);
                ageBitsList.add(0);
            }
            for (int i = 0; i < ageBitsList.size(); i++) {
                int age = ageBitsList.get(i);
                ageBitsList.set(i, age >>> 1);
            }
        }
        return pageFaults;
    }

    // encontra  apagina mais antiga usando o LRU
    private static int findPageToReplace(List<Integer> ageBitsList) {
        int minAge = ageBitsList.get(0);
        int pageIndex = 0;
        for (int i = 1; i < ageBitsList.size(); i++) {
            int age = ageBitsList.get(i);
            if (age < minAge) {
                minAge = age;
                pageIndex = i;
            }
        }
        return pageIndex;
    }
}