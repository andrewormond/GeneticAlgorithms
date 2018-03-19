package com.adeo8.genetic.test;

import com.adeo8.genetic.DoubleData;

public class LineData extends DoubleData {


	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new LineData(m(), b());
	}

	public LineData(double m, double b) {
		super(new double[] { b, m });
	}

	public double m() {
		return this.get(1);
	}

	public double b() {
		return this.get(0);
	}

	@Override
	public String toString() {
		return String.format("m: %.3f b: %.3f", m(), b()) + "\n" + super.toString() + "\n";
	}
}
