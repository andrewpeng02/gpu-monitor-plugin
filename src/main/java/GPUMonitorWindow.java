import com.intellij.openapi.wm.ToolWindow;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.List;
import java.util.Timer;

public class GPUMonitorWindow {
    private JPanel gpuMonitorPanel;
    private JPanel gpuTempPanel;
    private JPanel gpuUsagePanel;
    private JPanel memoryUsagePanel;

    private List<Float> timeSteps = new ArrayList<>(); // in minutes
    private Long startTime; // in milliseconds

    private int previousWidth;
    private Float windowSize;
    private Float refreshTime;
    private Float totalMemory;
    private List<Float> memoryUsage = new ArrayList<>();
    private List<Float> gpuUsage = new ArrayList<>();
    private List<Float> gpuTemp = new ArrayList<>();
    private List<Float> gpuClock = new ArrayList<>();

    private StandardChartTheme currentTheme;

    private GPUMonitorSettings settings;

    public GPUMonitorWindow(ToolWindow toolWindow) {
        // Set settings
        settings = GPUMonitorSettings.getInstance();
        windowSize = settings.getWindowSize();
        refreshTime = settings.getRefreshTime();

        startTime = System.currentTimeMillis();
        Timer timer = new Timer();
        timer.schedule(new RefreshGPUStats(), 0);

        gpuTempPanel.setLayout(new BorderLayout());
        gpuUsagePanel.setLayout(new BorderLayout());
        memoryUsagePanel.setLayout(new BorderLayout());

        previousWidth = gpuMonitorPanel.getWidth();
    }


    public void setGPUStats(Boolean updateCharts) {
        timeSteps.add(((float)(System.currentTimeMillis() - startTime)) / 1000 / 60);

        HashMap<String, Float> gpuStats = getGPUStats();
        totalMemory = convertMiBtoGB(gpuStats.get("memory total"));
        memoryUsage.add(convertMiBtoGB(gpuStats.get("memory used")));
        gpuUsage.add(gpuStats.get("gpu usage"));
        gpuTemp.add(gpuStats.get("temperature"));
        gpuClock.add(gpuStats.get("gpu clock"));

        // Update theme if needed
        if(updateCharts) {
            if (!settings.getChartTheme().equals(currentTheme)) {
                ChartFactory.setChartTheme(settings.getChartTheme());
                currentTheme = settings.getChartTheme();
            }

            gpuTempPanel.removeAll();
            if (settings.getEnableGPUTemp()) {
                gpuTempPanel.add(createGraph(gpuTemp, timeSteps, "GPU Temperature", "Time (min)", "Temperature (C)", (float) 0, (float) 100));
            }

            gpuUsagePanel.removeAll();
            if (settings.getEnableGPUUsage()) {
                gpuUsagePanel.add(createGraph(gpuUsage, timeSteps, "GPU Usage", "Time (min)", "Percentage", (float) 0, (float) 100));
            }

            memoryUsagePanel.removeAll();
            if (settings.getEnableGPUMemory()) {
                memoryUsagePanel.add(createGraph(memoryUsage, timeSteps, "Memory Usage", "Time (min)", "Memory Used (GB)", (float) 0, totalMemory));
            }
            gpuMonitorPanel.validate();
            gpuMonitorPanel.repaint();
        }
    }

    public ChartPanel createGraph(List<Float> numbers, List<Float> timeSteps, String title, String xAxisLabel, String yAxisLabel, Float rangeAxisMin, Float rangeAxisMax) {
        Float currentTime = ((float)(System.currentTimeMillis() - startTime)) / 1000 / 60;
        XYSeries series = new XYSeries(title);
        for(int i = 0; i < numbers.size(); i++) {
            series.add(timeSteps.get(i) - currentTime, numbers.get(i));
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        JFreeChart chart = ChartFactory.createXYLineChart(
                title, // Title
                xAxisLabel, // x-axis Label
                yAxisLabel, // y-axis Label
                 dataset, // Dataset
                 PlotOrientation.VERTICAL, // Plot Orientation
                false, // Show Legend
                true, // Use tooltips
                false // Configure chart to generate URLs?
        );
        XYPlot xyPlot = chart.getXYPlot();
        ValueAxis rangeAxis = xyPlot.getRangeAxis();
        rangeAxis.setRange(rangeAxisMin, rangeAxisMax);

        XYItemRenderer renderer = xyPlot.getRenderer();
        renderer.setSeriesPaint(0, Color.decode(settings.getLineColor()));

        ValueAxis domainAxis = xyPlot.getDomainAxis();
        domainAxis.setRange(-1 * windowSize, 0);
        return new ChartPanel(chart);
    }

    public HashMap<String, Float> getGPUStats() {
        List<String> output = new ArrayList<String>();
        HashMap<String, Float> parsedOutput = new HashMap<>();
        try {
            Process process = Runtime.getRuntime().exec("nvidia-smi -q -d UTILIZATION,TEMPERATURE,CLOCK,MEMORY");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while((line = reader.readLine()) != null) {
                output.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        parsedOutput.put("memory total", Float.parseFloat(output.get(10).replaceAll("\\D+","")));
        parsedOutput.put("memory used", Float.parseFloat(output.get(11).replaceAll("\\D+","")));

        parsedOutput.put("gpu usage", Float.parseFloat(output.get(18).replaceAll("\\D+","")));
        parsedOutput.put("temperature", Float.parseFloat(output.get(47).replaceAll("\\D+","")));
        parsedOutput.put("gpu clock", Float.parseFloat(output.get(54).replaceAll("\\D+","")));

        return parsedOutput;
    }

    class RefreshGPUStats extends TimerTask {
        public void run() {
            if(previousWidth == gpuMonitorPanel.getWidth())
                setGPUStats(true);
            else
                setGPUStats(false);
            previousWidth = gpuMonitorPanel.getWidth();

            windowSize = settings.getWindowSize();
            refreshTime = settings.getRefreshTime();

            Timer timer = new Timer();
            timer.schedule(new RefreshGPUStats(), (int) (refreshTime * 1000));
        }
    }

    public Float convertMiBtoGB(Float MiB) {
        return (float) (MiB / 953.6743164);
    }

    public JPanel getContent() {
        return gpuMonitorPanel;
    }
}


