package com.adeo8.genetic;

import java.nio.ByteBuffer;

public class IntegerData extends PrimativeData<Integer> {

	public IntegerData(byte[] bytes) {
		super(bytes, Integer.BYTES);
	}

	public IntegerData(Integer[] data) {
		super(data, Integer.BYTES);
	}

	@Override
	public void putDat(ByteBuffer bb, Integer dat) {
		bb.putInt(dat);

	}

	@Override
	public Integer getDat(ByteBuffer bb) {
		return bb.getInt();
	}

	@Override
	public String formatData(Integer dat) {
		return String.format("%d", dat);
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new IntegerData(this.bytes);
	}

}
