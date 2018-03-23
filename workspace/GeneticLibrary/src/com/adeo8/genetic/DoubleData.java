package com.adeo8.genetic;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class DoubleData extends PrimativeData<Double> {

	public DoubleData(byte[] bytes) {
		super(bytes, Double.SIZE / 8);
	}

	public DoubleData(ArrayList<Double> data) {
		super(DoubleToDouble(data), Double.SIZE / 8);
	}

	public static ArrayList<Double> doubleToDouble(double[] data) {
		ArrayList<Double> nD = new ArrayList<>();
		for (int i = 0; i < data.length; i++) {
			nD.add(data[i]);
		}
		return nD;
	}

	public static Double[] DoubleToDouble(ArrayList<Double> data) {
		Double[] nD = new Double[data.size()];
		for (int i = 0; i < data.size(); i++) {
			nD[i] = data.get(i);
		}
		return nD;
	}

	public DoubleData(double[] data) {
		this(doubleToDouble(data));
	}

	@Override
	public void putDat(ByteBuffer bb, Double dat) {
		bb.putDouble(dat);

	}

	@Override
	public Double getDat(ByteBuffer bb) {
		return bb.getDouble();
	}

	@Override
	public String formatData(Double dat) {

		return String.format("%.3f", dat);
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new DoubleData(this.bytes);
	}

}
