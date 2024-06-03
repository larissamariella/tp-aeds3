import java.util.BitSet;

public class ArraydeBits {
    
    BitSet bitSet;
    int tamanho;
    
    public ArraydeBits() {
        bitSet = new BitSet(100000000);
        tamanho = 0;
    }

    public void adicionar(boolean bit) {
        bitSet.set(tamanho, bit);
        tamanho++;
    }

    public boolean get(int index) {
        return bitSet.get(index);
    }

    public void adicionar(byte b) {
        for (int i = 0; i < 8; i++) {
            boolean bit = (b & (1 << i)) != 0;
            bitSet.set(tamanho + i, bit);
        }
        tamanho += 8;
    }

    public byte getByte(int index) {
        byte b = 0;
        for (int i = 0; i < 8; i++) {
            if (bitSet.get(index + i)) {
                b |= (1 << i);
            }
        }
        return b;
    }

    public void adicionar(int b) {
        for (int i = 0; i < 32; i++) {
            boolean bit = (b & (1 << i)) != 0;
            bitSet.set(tamanho + i, bit);
        }
        tamanho += 32;
    }

    public int getInt(int index) {
        int b = 0;
        for (int i = 0; i < 32; i++) {
            if (bitSet.get(index + i)) {
                b |= (1 << i);
            }
        }
        return b;
    }

    public byte[] toByteArray() {
        byte[] ba = new byte[(tamanho + 7) / 8];
        for (int i = 0; i < tamanho; i++) {
            if (bitSet.get(i)) {
                ba[i / 8] |= (1 << (i % 8));
            }
        }
        return ba;
    }

    public void fromByteArray(byte[] ba) {
        for (int i = 0; i < ba.length; i++) {
            for (int j = 0; j < 8; j++) {
                boolean bit = (ba[i] & (1 << j)) != 0;
                bitSet.set(i * 8 + j, bit);
            }
        }
        tamanho = ba.length * 8;
    }
}
