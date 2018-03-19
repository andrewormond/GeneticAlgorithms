package com.adeo8.genetic;

import java.util.Random;

public class BinaryData implements GeneticData {

	public byte[] bytes;
	public boolean mutated = false;
	public boolean breeded = false;

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new BinaryData(this.bytes.clone());
	}

	public BinaryData() {
		this.bytes = null;
	}

	public BinaryData(byte[] bytes) {
		this.bytes = bytes.clone();
	}

	@Override
	public GeneticData mutate() {
		try {
			BinaryData data = (BinaryData) this.clone();
			data.mutated = true;
			Random r = new Random();
			int bytInd = r.nextInt(data.bytes.length);
			int bitInd = r.nextInt(8);

			byte b = data.bytes[bytInd];
			byte mask = (byte) (1 << bitInd);
			b = (byte) (b ^ mask);
			data.bytes[bytInd] = b;

			return data;
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String toString() {
		String res = "[";
		if (bytes != null) {
			for (int i = 0; i < bytes.length; i++) {
				byte b = this.bytes[i];
				if (i != 0) {
					res += "-";
				}
				res += String.format("%8s", Integer.toBinaryString(Byte.toUnsignedInt(b))).replace(' ', '0');
			}
		} else {
			res += "null";
		}

		res += "]";
		
		if(this.breeded) {
			res += " B";
		}
		
		if(this.mutated) {
			res += " M";
		}
		return res;

	}

	@Override
	public GeneticData breed(GeneticData other) {
		try {
			BinaryData data = (BinaryData) this.clone();
			data.breeded = true;
			Random r = new Random();
			int bytInd = r.nextInt(data.bytes.length);
			if (r.nextBoolean()) {
				System.arraycopy(((BinaryData) other).bytes, 0, data.bytes, 0, bytInd);
			}else {
				System.arraycopy(((BinaryData) other).bytes, bytInd, data.bytes, bytInd, data.bytes.length - bytInd);
			}

			return data;
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
