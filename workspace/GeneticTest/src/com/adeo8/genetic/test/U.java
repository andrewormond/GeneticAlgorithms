package com.adeo8.genetic.test;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import com.adeo8.genetic.DoubleData;

public class U {

	private U() {

	}

	public static LinkedHashMap<Double, Double> evaluateLine(double m, double b, double xMin, double xMax, int n) {
		LinkedHashMap<Double, Double> points = new LinkedHashMap<Double, Double>();
		for (double x = xMin; x <= xMax; x += (xMax - xMin) / n) {
			double y = x * m + b;
			points.put(x, y);
		}
		return points;
	}

	public static LinkedHashMap<Double, Double> evaluateSeries(double[] coefficients, double xMin, double xMax, int n) {
		LinkedHashMap<Double, Double> points = new LinkedHashMap<Double, Double>();
		for (double x = xMin; x <= xMax; x += (xMax - xMin) / n) {
			double y = 0;
			for (int i = 0; i < coefficients.length; i++) {
				y += coefficients[i] * Math.pow(x, i);
			}
			points.put(x, y);
		}
		return points;
	}

	public static void plotAll(DoubleData[] population, double xMin, double xMax, int n) {
		final XYSeriesCollection data = new XYSeriesCollection();
		int i = 1;
		for (DoubleData dat : population) {
			LinkedHashMap<Double, Double> points = evaluateSeries(dat.toDoubles(), xMin, xMax, n);
			final XYSeries series = new XYSeries("[" + (i++) + "]");
			for (Map.Entry<Double, Double> p : points.entrySet()) {
				series.add(p.getKey(), p.getValue());
			}
			data.addSeries(series);
		}

		final JFreeChart chart = ChartFactory.createXYLineChart("Population", "X", "Y", data, PlotOrientation.VERTICAL,
				true, true, false);

		final ChartPanel chartPanel = new ChartPanel(chart);
		chart.removeLegend();
		chartPanel.setPreferredSize(new java.awt.Dimension(750, 500));
		ApplicationFrame frame = new ApplicationFrame("Population [" + population.length + "]");
		frame.setContentPane(chartPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		RefineryUtilities.positionFrameOnScreen(frame, 0d, 0.5d);
		frame.setVisible(true);
	}

	public static void plotLine(DoubleData data, FitterConfig config) {

		LinkedHashMap<Double, Double> points = U.evaluateSeries(data.toDoubles(), config.X_MIN, config.X_MAX,
				config.NUM_POINTS);
		U.plotPoints(data.toString(), points, config.testPoints);
	}

	static Object lock = new Object();
	static ApplicationFrame frame = null;

	public static void plotPoints(String name, LinkedHashMap<Double, Double> points,
			LinkedHashMap<Double, Double> realPoints) {

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				XYSeries series = new XYSeries(name);
				for (Map.Entry<Double, Double> p : points.entrySet()) {
					series.add(p.getKey(), p.getValue());
				}
				XYSeries realseries = new XYSeries("Real");
				for (Map.Entry<Double, Double> p : realPoints.entrySet()) {
					realseries.add(p.getKey(), p.getValue());
				}
				XYSeriesCollection data = new XYSeriesCollection(series);
				data.addSeries(realseries);

				JFreeChart chart = ChartFactory.createXYLineChart("Data vs. Best Fit", "X", "Y", data,
						PlotOrientation.VERTICAL, true, true, false);

				ChartPanel chartPanel = new ChartPanel(chart);
				chartPanel.setPreferredSize(new java.awt.Dimension(750, 400));
				synchronized (lock) {
					boolean centered = false;
					if (frame == null || !frame.isShowing()) {
						frame = new ApplicationFrame("Best Fit");
						centered = true;
						RefineryUtilities.positionFrameOnScreen(frame, 0.5d, 0d);
					}
					frame.setContentPane(chartPanel);
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.pack();
					frame.setVisible(true);
					if (centered) {
						RefineryUtilities.positionFrameOnScreen(frame, 0.5d, 0d);
					}
				}

			}

		});

		t.start();
	}

}
