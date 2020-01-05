import com.intellij.openapi.wm.ToolWindow;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.List;
import java.util.Timer;

public class GPUMonitorWindow {
    private JPanel gpuMonitorPanel;
    private JPanel gpuTempPanel;
    private JTextField refreshFrequencyField;
    private JPanel gpuUsagePanel;
    private JPanel memoryUsagePanel;
    private JTextField windowLengthField;

    private List<Float> timeSteps = new ArrayList<>(); // in seconds
    private Long startTime; // in milliseconds

    private Float windowLength;
    private Float refreshFrequency;
    private Float totalMemory;
    private List<Float> memoryUsage = new ArrayList<>();
    private List<Float> gpuUsage = new ArrayList<>();
    private List<Float> gpuTemp = new ArrayList<>();
    private List<Float> gpuClock = new ArrayList<>();

    private GPUMonitorSettings settings;

    public GPUMonitorWindow(ToolWindow toolWindow) {
        // Set settings
        settings = GPUMonitorSettings.getInstance();
        windowLength = settings.getWindowLength();
        refreshFrequency = settings.getRefreshFrequency();

        windowLengthField.setText(String.valueOf(windowLength));
        refreshFrequencyField.setText(String.valueOf(refreshFrequency));

        startTime = System.currentTimeMillis();
        Timer timer = new Timer();
        timer.schedule(new RefreshGPUStats(), 0, 250);

        gpuTempPanel.setLayout(new BorderLayout());
        gpuUsagePanel.setLayout(new BorderLayout());
        memoryUsagePanel.setLayout(new BorderLayout());

        // Update timer if the user changes the refresh frequency
        refreshFrequencyField.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    timer.cancel();

                    Timer timer = new Timer();
                    try {
                        refreshFrequency = Float.parseFloat(refreshFrequencyField.getText());
                        if (refreshFrequency > 0) {
                            settings.setRefreshFrequency(refreshFrequency);
                            timer.schedule(new RefreshGPUStats(), 0, (int) (refreshFrequency * 1000));
                        }
                    } catch (NumberFormatException exception) {
                        refreshFrequencyField.setText(String.valueOf(refreshFrequency));
                    }
                }});

        windowLengthField.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
            try {
                if (windowLength > 0) {
                    windowLength = Float.parseFloat(windowLengthField.getText());
                    settings.setWindowLength(windowLength);
                }
            } catch (NumberFormatException exception) {
                windowLengthField.setText(String.valueOf(windowLength));
            }
        }});
    }


    public void setGPUStats() {
        timeSteps.add(((float)(System.currentTimeMillis() - startTime)) / 1000);

        HashMap<String, Float> gpuStats = getGPUStats();
        totalMemory = convertMiBtoGB(gpuStats.get("memory total"));
        memoryUsage.add(convertMiBtoGB(gpuStats.get("memory used")));
        gpuUsage.add(gpuStats.get("gpu usage"));
        gpuTemp.add(gpuStats.get("temperature"));
        gpuClock.add(gpuStats.get("gpu clock"));

        gpuTempPanel.removeAll();
        if(settings.getEnableGPUTemp()) {
            gpuTempPanel.add(createGraph(gpuTemp, timeSteps, "GPU Temperature", "Time (sec)", "Temperature (C)", (float) 100));
            gpuTempPanel.validate();
            gpuTempPanel.repaint();
        }

        gpuUsagePanel.removeAll();
        if(settings.getEnableGPUUsage()) {
            gpuUsagePanel.add(createGraph(gpuUsage, timeSteps, "GPU Usage", "Time (sec)", "Percentage", (float) 100));
            gpuUsagePanel.validate();
            gpuUsagePanel.repaint();
        }

        memoryUsagePanel.removeAll();
        if(settings.getEnableGPUMemory()) {
            memoryUsagePanel.add(createGraph(memoryUsage, timeSteps, "Memory Usage", "Time (sec)", "Memory Used (GB)", totalMemory));
            memoryUsagePanel.validate();
            memoryUsagePanel.repaint();
        }
    }

    public ChartPanel createGraph(List<Float> numbers, List<Float> timeSteps, String title, String xAxisLabel, String yAxisLabel, Float rangeAxisMax) {
        XYSeries series = new XYSeries(title);
        for(int i = 0; i < numbers.size(); i++) {
            series.add(timeSteps.get(i), numbers.get(i));
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        JFreeChart chart = ChartFactory.createXYLineChart(
                title, // Title
                xAxisLabel, // x-axis Label
                yAxisLabel, // y-axis Label
                 dataset, // Dataset
                 PlotOrientation.VERTICAL, // Plot Orientation
                true, // Show Legend
                true, // Use tooltips
                false // Configure chart to generate URLs?
        );
        XYPlot xyPlot = chart.getXYPlot();
        ValueAxis rangeAxis = xyPlot.getRangeAxis();
        rangeAxis.setRange(0.0, rangeAxisMax);

        Float currentTime = ((float)(System.currentTimeMillis() - startTime)) / 1000;
        ValueAxis domainAxis = xyPlot.getDomainAxis();
        domainAxis.setRange(currentTime - windowLength, currentTime);
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
            setGPUStats();
        }
    }

    public Float convertMiBtoGB(Float MiB) {
        return (float) (MiB / 953.6743164);
    }

    public JPanel getContent() {
        return gpuMonitorPanel;
    }
}


