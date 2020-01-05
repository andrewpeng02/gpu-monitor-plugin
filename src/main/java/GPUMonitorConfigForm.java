import com.intellij.openapi.project.Project;

import javax.swing.*;

public class GPUMonitorConfigForm {
    private JCheckBox gpuTempCheckbox;
    private JPanel gpuMonitorConfigPanel;
    private JCheckBox gpuUsageCheckbox;
    private JCheckBox gpuMemoryCheckbox;

    private GPUMonitorSettings settings;

    public void createUI() {
        settings = GPUMonitorSettings.getInstance();
        gpuTempCheckbox.setSelected(settings.getEnableGPUTemp());
        gpuUsageCheckbox.setSelected(settings.getEnableGPUUsage());
        gpuMemoryCheckbox.setSelected(settings.getEnableGPUMemory());

    }

    public JPanel getRootPanel() {
        return gpuMonitorConfigPanel;
    }

    public boolean isModified() {
        return true;
    }

    public void apply() {
        settings.setEnableGPUTemp(gpuTempCheckbox.isSelected());
        settings.setEnableGPUUsage(gpuUsageCheckbox.isSelected());
        settings.setEnableGPUMemory(gpuMemoryCheckbox.isSelected());
    }
}
