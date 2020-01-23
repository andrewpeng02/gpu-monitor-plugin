import com.intellij.openapi.project.Project;

import javax.swing.*;

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
                !refreshTimeField.getText().equals(settings.getRefreshTime().toString());
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
}
