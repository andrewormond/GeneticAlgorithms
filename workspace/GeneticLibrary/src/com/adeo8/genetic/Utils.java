package com.adeo8.genetic;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class Utils {

	private Utils() {
	}

	public static byte[] toByteArray(double value) {
		byte[] bytes = new byte[8];
		ByteBuffer.wrap(bytes).putDouble(value);
		return bytes;
	}

	public static double toDouble(byte[] bytes) {
		return ByteBuffer.wrap(bytes).getDouble();
	}

	public static void println(String s) {
		System.out.println(s);
	}

	public static <K, V extends Comparable<? super V>> LinkedHashMap<K, V> sortByValue(Map<K, V> map) {
		List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
		list.sort((o1, o2) -> o1.getValue().compareTo(o2.getValue()));

		LinkedHashMap<K, V> result = new LinkedHashMap<>();
		for (Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}

		return result;
	}

	static <K, V extends Comparable<? super V>> SortedSet<Map.Entry<K, V>> entriesSortedByValues(Map<K, V> map) {
		SortedSet<Map.Entry<K, V>> sortedEntries = new TreeSet<Map.Entry<K, V>>(new Comparator<Map.Entry<K, V>>() {
			@Override
			public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
				int res = e1.getValue().compareTo(e2.getValue());
				return res != 0 ? res : 1;
			}
		});
		sortedEntries.addAll(map.entrySet());
		return sortedEntries;
	}

	private static final double MAX = 20;

	static Object lock = new Object();
	static ApplicationFrame frame = null;

	public static void plotHistogram(GeneticData[] population, GeneticHandler handler, int numBins, String name) {
		final GeneticData[] dats = population.clone();
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				GeneticData[] population = dats;
				double[] fitness = new double[population.length];
				for (int i = 0; i < fitness.length; i++) {
					fitness[i] = Math.log10(handler.getFitness(population[i]));
					if (fitness[i] > MAX) {
						fitness[i] = MAX;
					}
				}
				HistogramDataset dataset = new HistogramDataset();
				dataset.setType(HistogramType.RELATIVE_FREQUENCY);
				dataset.addSeries("Histogram", fitness, numBins);
				String plotTitle = name;
				String xaxis = "Log10(Fitness)";
				String yaxis = "Percentage of Population";
				PlotOrientation orientation = PlotOrientation.VERTICAL;
				boolean show = false;
				boolean toolTips = false;
				boolean urls = false;
				JFreeChart chart = ChartFactory.createHistogram(plotTitle, xaxis, yaxis, dataset, orientation, show,
						toolTips, urls);

				final ChartPanel chartPanel = new ChartPanel(chart);
				chartPanel.setPreferredSize(new java.awt.Dimension(750, 500));
				synchronized (lock) {
					boolean centered = false;
					if (frame == null || !frame.isShowing()) {
						frame = new ApplicationFrame("Histogram [" + population.length + "]");
						centered = true;
						RefineryUtilities.positionFrameOnScreen(frame, 0.5d, 0.8d);
					}
					frame.setContentPane(chartPanel);
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.pack();
					frame.setVisible(true);
					if (centered) {
						RefineryUtilities.positionFrameOnScreen(frame, 0.5d, 0.8d);
					}
				}

			}

		});

		t.start();

	}

}
