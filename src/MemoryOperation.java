public class MemoryOperation {
    private String address;
    private char operation;

    public MemoryOperation(String address, char operation) {
        this.address = address;
        this.operation = operation;
    }

    public String getAddress() {
        return address;
    }

    public char getOperation() {
        return operation;
    }
}
