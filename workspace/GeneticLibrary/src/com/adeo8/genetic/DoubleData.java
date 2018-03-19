package com.adeo8.genetic;

import java.nio.ByteBuffer;
import java.util.Random;

public class DoubleData extends BinaryData {

	private static final int double_width = 8;
	private int length;

	public int size() {
		return length;
	}

	public DoubleData(byte[] bytes) {
		super(bytes);
		length = bytes.length / double_width;
	}

	public DoubleData(double[] doubles) {
		length = doubles.length;
		ByteBuffer bb = ByteBuffer.allocate(length * double_width);
		for (double d : doubles) {
			bb.putDouble(d);
		}
		this.bytes = bb.array();
	}

	public double[] toDoubles() {
		ByteBuffer bb = ByteBuffer.wrap(this.bytes);
		double[] doubles = new double[length];
		for (int i = 0; i < doubles.length; i++) {
			doubles[i] = bb.getDouble();
		}
		return doubles;
	}

	public double get(int index) {
		return this.toDoubles()[index];
	}

	@Override
	public GeneticData mutate() {
		DoubleData data = (DoubleData) super.mutate();
		if (new Random().nextDouble() < 0.2d) {
			double[] doubles = data.toDoubles();
			int i = new Random().nextInt(doubles.length);
			doubles[i] *= -1;
			data = new DoubleData(doubles);
		}
		return data;
	}

	@Override
	public String toString() {
		String res = "[";
		double[] doubles = this.toDoubles();
		for (int i = 0; i < length; i++) {
			double d = doubles[i];
			if (i > 0) {
				res += "|";
			}
			res += String.format("%.5f", d);
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

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new DoubleData(this.bytes);
	}

}
