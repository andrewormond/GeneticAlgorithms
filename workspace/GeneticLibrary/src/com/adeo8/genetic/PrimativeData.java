package com.adeo8.genetic;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public abstract class PrimativeData<T> extends BinaryData {

	public final int wordSize;
	public int length;

	public abstract void putDat(ByteBuffer bb, T dat);

	public abstract T getDat(ByteBuffer bb);

	public abstract String formatData(T dat);

	public PrimativeData(byte[] bytes, int wordSize) {
		super(bytes);
		this.wordSize = wordSize;
		this.length = bytes.length / wordSize;
	}

	public void setArray(T[] data) {
		length = data.length;
		ByteBuffer bb = ByteBuffer.allocate(length * wordSize);
		for (T dat : data) {
			putDat(bb, dat);
		}
		this.bytes = bb.array();
	}

	private void setArray(ArrayList<T> array) {
		length = array.size();
		ByteBuffer bb = ByteBuffer.allocate(length * wordSize);
		for (T dat : array) {
			putDat(bb, dat);
		}
		this.bytes = bb.array();
	}

	public ArrayList<T> toArray() {
		ArrayList<T> array = new ArrayList<>();
		ByteBuffer bb = ByteBuffer.wrap(this.bytes);
		for (int i = 0; i < length; i++) {
			array.add(getDat(bb));
		}
		return array;
	}

	public T get(int index) {
		return this.toArray().get(index);
	}

	public void set(int index, T data) {
		ArrayList<T> array = this.toArray();
		array.set(index, data);
		this.setArray(array);
	}

	public PrimativeData(T[] data, int wordSize) {
		this.wordSize = wordSize;
		setArray(data);
	}

	@Override
	public String toString() {
		String res = "[";
		ArrayList<T> array = this.toArray();
		for (int i = 0; i < length; i++) {
			T d = array.get(i);
			if (i > 0) {
				res += " |";
			}
			res += this.formatData(d);
		}
		res += "]";

		if (this.breeded) {
			res += " B";
		}

		if (this.mutated) {
			res += " M";
		}
		return res;
	}

}
