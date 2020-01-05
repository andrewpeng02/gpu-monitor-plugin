import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
        name = "GPUMonitorSettings",
        storages = {
                @Storage(
                        value = "GPUMonitorSettings.xml"
                )
        }
)
public class GPUMonitorSettings implements PersistentStateComponent<GPUMonitorSettings> {
    public Float windowLength = (float) 30;
    public Float refreshFrequency = (float) 0.25;

    public Boolean enableGPUTemp = true;
    public Boolean enableGPUUsage = true;
    public Boolean enableGPUMemory = true;

    Float getWindowLength() { return windowLength; }
    Float getRefreshFrequency() { return refreshFrequency; }

    Boolean getEnableGPUTemp() { return enableGPUTemp; }
    Boolean getEnableGPUUsage() { return enableGPUUsage; }
    Boolean getEnableGPUMemory() { return enableGPUMemory; }

    void setEnableGPUTemp(Boolean enableGPUTemp) { this.enableGPUTemp = enableGPUTemp; }
    void setEnableGPUUsage(Boolean enableGPUUsage) { this.enableGPUUsage = enableGPUUsage; }
    void setEnableGPUMemory(Boolean enableGPUMemory) { this.enableGPUMemory = enableGPUMemory; }

    void setWindowLength(Float windowLength) { this.windowLength = windowLength; }
    void setRefreshFrequency(Float refreshFrequency) { this.refreshFrequency = refreshFrequency; }

    @Nullable
    @Override
    public GPUMonitorSettings getState() { return this; }

    @Override
    public void loadState(@NotNull GPUMonitorSettings state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public static GPUMonitorSettings getInstance() {
        return ServiceManager.getService(GPUMonitorSettings.class);
    }
}
