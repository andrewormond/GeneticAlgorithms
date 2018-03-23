package com.adeo8.genetic.gcd;

import java.util.Random;

import com.adeo8.genetic.GeneticData;
import com.adeo8.genetic.IntegerData;

public class IncIntData extends IntegerData {

	public final int maxValue;

	public IncIntData(byte[] bytes, int maxValue) {
		super(bytes);
		this.maxValue = maxValue;
	}

	public IncIntData(Integer[] data, int maxValue) {
		super(data);
		this.maxValue = maxValue;
	}

	public IncIntData(IncIntData data) {
		this(data.bytes, data.maxValue);
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new IncIntData(this);
	}

	@Override
	public GeneticData mutate() {
		try {
			IncIntData data = (IncIntData) this.clone();
			data.mutated = true;
			Random r = new Random();
			int index = r.nextInt(this.length);
			int amount = (r.nextInt(2) * 2) - 1;
			int j = data.get(index) + amount;
			if (j > this.maxValue) {
				j = this.maxValue;
			} else if (j < 1) {
				j = 1;
			}

			data.set(index, j);

			return data;
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
