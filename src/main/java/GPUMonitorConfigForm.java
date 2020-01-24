import com.intellij.openapi.project.Project;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class GPUMonitorConfigForm {
    private JCheckBox gpuTempCheckbox;
    private JPanel gpuMonitorConfigPanel;
    private JCheckBox gpuUsageCheckbox;
    private JCheckBox gpuMemoryCheckbox;
    private JCheckBox darculaThemeCheckbox;
    private JCheckBox lightThemeCheckbox;
    private JCheckBox customThemeCheckbox;
    private JTextField windowSizeField;
    private JTextField refreshTimeField;
    private JTextField titleColorField;
    private JTextField axisLabelColorField;
    private JTextField tickColorField;
    private JTextField backgroundColorField;
    private JTextField lineColorField;
    private JTextField plotBackgroundColorField;

    private GPUMonitorSettings settings;

    public void createUI() {
        settings = GPUMonitorSettings.getInstance();
        gpuTempCheckbox.setSelected(settings.getEnableGPUTemp());
        gpuUsageCheckbox.setSelected(settings.getEnableGPUUsage());
        gpuMemoryCheckbox.setSelected(settings.getEnableGPUMemory());

        windowSizeField.setText(settings.getWindowSize().toString());
        refreshTimeField.setText(settings.getRefreshTime().toString());

        lightThemeCheckbox.setSelected(settings.getEnableLight());
        darculaThemeCheckbox.setSelected(settings.getEnableDarcula());
        customThemeCheckbox.setSelected(settings.getEnableCustom());

        titleColorField.setText(settings.getTitleColor());
        titleColorField.setBackground(Color.decode(settings.getTitleColor()));
        axisLabelColorField.setText(settings.getAxisLabelColor());
        axisLabelColorField.setBackground(Color.decode(settings.getAxisLabelColor()));
        tickColorField.setText(settings.getTickColor());
        tickColorField.setBackground(Color.decode(settings.getTickColor()));
        backgroundColorField.setText(settings.getBackgroundColor());
        backgroundColorField.setBackground(Color.decode(settings.getBackgroundColor()));
        lineColorField.setText(settings.getLineColor());
        lineColorField.setBackground(Color.decode(settings.getLineColor()));
        plotBackgroundColorField.setText(settings.getPlotBackgroundColor());
        plotBackgroundColorField.setBackground(Color.decode(settings.getPlotBackgroundColor()));
    }

    public JPanel getRootPanel() {
        return gpuMonitorConfigPanel;
    }

    public boolean isModified() {
        return gpuTempCheckbox.isSelected() != settings.getEnableGPUTemp() ||
                gpuUsageCheckbox.isSelected() != settings.getEnableGPUUsage() ||
                gpuMemoryCheckbox.isSelected() != settings.getEnableGPUMemory() ||
                lightThemeCheckbox.isSelected() != settings.getEnableLight() ||
                darculaThemeCheckbox.isSelected() != settings.getEnableDarcula() ||
                customThemeCheckbox.isSelected() != settings.getEnableCustom() ||
                !windowSizeField.getText().equals(settings.getWindowSize().toString()) ||
                !refreshTimeField.getText().equals(settings.getRefreshTime().toString()) ||
                !titleColorField.getText().equals(settings.getTitleColor()) ||
                !axisLabelColorField.getText().equals(settings.getAxisLabelColor()) ||
                !tickColorField.getText().equals(settings.getTickColor()) ||
                !backgroundColorField.getText().equals(settings.getBackgroundColor()) ||
                !lineColorField.getText().equals(settings.getLineColor()) ||
                !plotBackgroundColorField.getText().equals(settings.getPlotBackgroundColor());
    }

    public void apply() {
        settings.setEnableGPUTemp(gpuTempCheckbox.isSelected());
        settings.setEnableGPUUsage(gpuUsageCheckbox.isSelected());
        settings.setEnableGPUMemory(gpuMemoryCheckbox.isSelected());

        settings.setEnableLight(lightThemeCheckbox.isSelected());
        settings.setEnableDarcula(darculaThemeCheckbox.isSelected());
        settings.setEnableCustom(customThemeCheckbox.isSelected());

        try {
            Float refreshTime = Float.parseFloat(refreshTimeField.getText());
            settings.setRefreshTime(refreshTime);
        } catch (NumberFormatException exception) {
            refreshTimeField.setText(String.valueOf(settings.getRefreshTime()));
        }

        try {
            Float windowSize = Float.parseFloat(windowSizeField.getText());
            settings.setWindowSize(windowSize);
        } catch (NumberFormatException exception) {
            windowSizeField.setText(String.valueOf(settings.getWindowSize()));
        }

        trySetColor(titleColorField, settings.getTitleColor(), a -> settings.setTitleColor(a));
        trySetColor(axisLabelColorField, settings.getAxisLabelColor(), a -> settings.setAxisLabelColor(a));
        trySetColor(tickColorField, settings.getTickColor(), a -> settings.setTickColor(a));
        trySetColor(backgroundColorField, settings.getBackgroundColor(), a -> settings.setBackgroundColor(a));
        trySetColor(lineColorField, settings.getLineColor(), a -> settings.setLineColor(a));
        trySetColor(plotBackgroundColorField, settings.getPlotBackgroundColor(), a -> settings.setPlotBackgroundColor(a));
    }

    public void reset() {
        gpuTempCheckbox.setSelected(settings.getEnableGPUTemp());
        gpuUsageCheckbox.setSelected(settings.getEnableGPUUsage());
        gpuMemoryCheckbox.setSelected(settings.getEnableGPUMemory());

        lightThemeCheckbox.setSelected(settings.getEnableLight());
        darculaThemeCheckbox.setSelected(settings.getEnableDarcula());
        customThemeCheckbox.setSelected(settings.getEnableCustom());

        windowSizeField.setText(settings.getWindowSize().toString());
        refreshTimeField.setText(settings.getRefreshTime().toString());
    }

    private void trySetColor(JTextField colorField, String prevColor, Consumer<String> setColor) {
        try {
            String color = colorField.getText();
            if(!color.substring(0, 1).equals("#"))
                color = "#" + color;

            colorField.setBackground(Color.decode(color));
            colorField.setText(color);
            setColor.accept(color);
        } catch (Exception exception) {
            colorField.setText(prevColor);
        }
    }
}
