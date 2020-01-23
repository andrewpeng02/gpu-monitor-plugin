import com.intellij.openapi.project.Project;

import javax.swing.*;

public class GPUMonitorConfigForm {
    private JCheckBox gpuTempCheckbox;
    private JPanel gpuMonitorConfigPanel;
    private JCheckBox gpuUsageCheckbox;
    private JCheckBox gpuMemoryCheckbox;
    private JCheckBox darculaThemeCheckbox;
    private JCheckBox lightThemeCheckbox;

    private GPUMonitorSettings settings;

    public void createUI() {
        settings = GPUMonitorSettings.getInstance();
        gpuTempCheckbox.setSelected(settings.getEnableGPUTemp());
        gpuUsageCheckbox.setSelected(settings.getEnableGPUUsage());
        gpuMemoryCheckbox.setSelected(settings.getEnableGPUMemory());
        darculaThemeCheckbox.setSelected(settings.getEnableDarcula());
    }

    public JPanel getRootPanel() {
        return gpuMonitorConfigPanel;
    }

    public boolean isModified() {
        return gpuTempCheckbox.isSelected() != settings.getEnableGPUTemp() ||
                gpuUsageCheckbox.isSelected() != settings.getEnableGPUUsage() ||
                gpuMemoryCheckbox.isSelected() != settings.getEnableGPUMemory() ||
                darculaThemeCheckbox.isSelected() != settings.getEnableDarcula();
    }

    public void apply() {
        settings.setEnableGPUTemp(gpuTempCheckbox.isSelected());
        settings.setEnableGPUUsage(gpuUsageCheckbox.isSelected());
        settings.setEnableGPUMemory(gpuMemoryCheckbox.isSelected());
        settings.setEnableDarcula(darculaThemeCheckbox.isSelected());
    }

    public void reset() {
        gpuTempCheckbox.setSelected(settings.getEnableGPUTemp());
        gpuUsageCheckbox.setSelected(settings.getEnableGPUUsage());
        gpuMemoryCheckbox.setSelected(settings.getEnableGPUMemory());
        darculaThemeCheckbox.setSelected(settings.getEnableDarcula());
    }
}
