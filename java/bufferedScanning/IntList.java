package bufferedScanning;


import java.util.Arrays;

public class IntList {
    private int mSize;
    private int[] mArray;

    public IntList() {
        mSize = 0;
        mArray = new int[0];
    }

    public int size() {
        return mSize;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    private void reallocate(int newCapacity) {
        if (mArray != null && mArray.length != 0) {
            mArray = Arrays.copyOf(mArray, newCapacity); // This function is pretty weird: it crashes if input size == 0
        } else {
            mArray = new int[newCapacity];
        }
    }

    public IntList add(int element) {
        if (mSize + 1 >= mArray.length) {
            reallocate(Math.max(mArray.length * 2, 4));
        }
        mArray[mSize++] = element;

        return this;
    }
    public int get(int index) {
        return mArray[index];
    }
    public IntList set(int index, int element) {
        mArray[index] = element;

        return this;
    }

}
