/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evocomp.genetics.citysearch;

import java.awt.Dimension;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author jamhb
 */
public class Test extends JFrame{

    public static XYSeriesCollection minDistancesCollection = null;
    public static XYSeries minDistanceSeries = null;
    public static JPanel chartDistancePanel = null;
    
    public static XYSeriesCollection pathCollection = null;
    public static XYSeries pathSeries = null;
    public static JPanel chartPathPanel = null;
    
    public static JPanel mainPanel=null;
    
    public static Integer[][] output = null;
    public static CitySearch cs;
    public static int generation = 0;
    public static int maximumGenerations = 200;
    public static int interval = 100;
    public static Timer timer = new Timer();
    
    
    
    public Test() {
        super("XY Line Chart Example with JFreechart");
        
        initializeOutput();
 
        minDistancesCollection = new XYSeriesCollection();
        minDistanceSeries = new XYSeries("Minimum Distance");
        
        pathCollection = new XYSeriesCollection();
        pathSeries = new XYSeries("Path");
        
        chartDistancePanel = createProgressChartPanel();
        chartPathPanel = createPathChartPanel();
        
        mainPanel = new JPanel();
        mainPanel.setSize(1280,480);
        
        mainPanel.add(chartDistancePanel);
        mainPanel.add(chartPathPanel);
        add(mainPanel);
 
        setSize(1320,520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runAlgorithm();
                if(generation>maximumGenerations){
                    timer.cancel();
                }
            }
          }, interval, interval);
    }
     
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Test().setVisible(true);
            }
        });
    }
    
    private static void initializeOutput(){
        cs = new CitySearch(30,100);
        output = cs.createMatrix();
        
        System.out.println("Initial Random Matrix... ");
        for (Integer[] is : output) {
            System.out.println(Arrays.toString(is));
        }
    }
    
    private static void runAlgorithm() {
        //System.out.println("Getting Distance Calculation...");
        Position [] positionArray = getPositionArray();
        Double [] distances = cs.getCromoDistance(output, positionArray);

        List distanceList = Arrays.asList(distances);
        double minValue = (double) Collections.min(distanceList);
        int minIndex = distanceList.indexOf(minValue);

        //System.out.println("Matching matrix... ");
        Integer[][] matchedOutput = cs.matchMatrix(distances, output);

        //System.out.println("Muting generation...");
        Integer[][] mutedOutput = cs.getMutatedChildren(matchedOutput);

        System.out.println("Min generation value: " + minValue + " Route: " + Arrays.toString(output[minIndex]));
        minDistanceSeries.add(generation, minValue);
        minDistancesCollection.removeAllSeries();
        minDistancesCollection.addSeries(minDistanceSeries);
        
        
        pathCollection.removeAllSeries();
        pathCollection.addSeries(getCurrentPathSeries(output[minIndex],positionArray));
        
        output = mutedOutput;
        
        generation++;
    }
    
    private static XYSeries getCurrentPathSeries(Integer [] routes, Position [] positions){
        XYSeries series = new XYSeries("Current Path", false);
        for (Integer route : routes) {
            series.add(positions[route].getX(),positions[route].getY());
        }
        return series;
    } 
    
    private JPanel createProgressChartPanel() {
        String chartTitle = "City Distance";
        String xAxisLabel = "Generation";
        String yAxisLabel = "Distance";

        JFreeChart chart = ChartFactory.createXYLineChart(chartTitle,
                xAxisLabel, yAxisLabel, minDistancesCollection);
        ChartPanel cp = new ChartPanel(chart);
        
        
        cp.setPreferredSize(new Dimension(640,480));
        return cp;
    }
    
    private JPanel createPathChartPanel() {
        String chartTitle = "Path";
        String xAxisLabel = "X";
        String yAxisLabel = "Y";

        JFreeChart chart = ChartFactory.createScatterPlot(chartTitle,
                xAxisLabel, yAxisLabel, pathCollection, PlotOrientation.VERTICAL,
                true,                     // include legend
                true,                     // tooltips
                false                     // urls
        );
        
        XYPlot plot = (XYPlot) chart.getPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, true);
        plot.setRenderer(renderer);
        
        ChartPanel cp = new ChartPanel(chart);
        cp.setPreferredSize(new Dimension(640,480));
        return cp;
    }
    
    public static Position [] getPositionArray(){
        return new Position[]{
            new Position(0,0), //0 doesn't work
            new Position(1,9), //1
            new Position(4,4), //2
            new Position(4,9), //3
            new Position(1,2), //4
            new Position(9,10), //5
            new Position(7,7), //6
            new Position(9,5), //7
            new Position(0,7), //8
            new Position(7,6), //9
            new Position(3,7), //10
            new Position(7,3), //11
            new Position(1,6), //12
            new Position(1,4), //13
            new Position(7,10), //14
            new Position(4,2), //15
            new Position(9,2), //16
            new Position(9,8), //17
            new Position(8,9), //18
            new Position(5,6), //19
            new Position(10,7), //20
            new Position(2,9), //21
            new Position(2,5), //22
            new Position(9,4), //23
            new Position(3,1), //24
            new Position(6,9), //25
            new Position(5,5), //26
            new Position(0,0), //27
            new Position(9,7), //28
            new Position(3,10), //29
            new Position(5,8)  //30
        };
    }
}
