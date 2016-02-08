package frc.team5333.lib.util;

import java.lang.reflect.Array;

public class FifoBuffer<T> {

	Object[] array;
	int size;

	/**
	 * Create a new FIFO buffer.
	 * @param i The size of the array
	 */
	public FifoBuffer(int i) {
		this.size = i;
		array = new Object[size];
	}

	/**
	 * Add a new value to the array
	 * @param value
	 */
	public void add(T value) {
		pushBack();
		array[0] = value;
	}

	public T get(int index) {
        return (T) array[index];
    }
	/**
	 * Clear the array
	 */
	public void clear() {
		array = new Object[size];
	}

	/**
	 * Push values back
	 */
	void pushBack() {
		for (int i = array.length - 1; i > 0; i--) {
			if (array[i - 1] != null) {
				array[i] = null;
				array[i] = array[i - 1];
			}
		}
		array[0] = null;
	}

	/**
	 * Get the contents of the array
	 * @param contents A 'base' array to convert to. <i> e.g. new String[0]; </i>
	 * @return The array
	 */
	public T[] toArray(T[] contents) {
		int s = size;
		if (contents.length < s) {
			@SuppressWarnings("unchecked") T[] newArray
				= (T[]) Array.newInstance(contents.getClass().getComponentType(), s);
			contents = newArray;
		}
		System.arraycopy(this.array, 0, contents, 0, s);
		if (contents.length > s) {
			contents[s] = null;
		}
		return contents;
	}

}