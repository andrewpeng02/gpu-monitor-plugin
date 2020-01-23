import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.StandardChartTheme;

import java.awt.*;

@State(
        name = "GPUMonitorSettings",
        storages = {
                @Storage(
                        value = "GPUMonitorSettings.xml"
                )
        }
)
public class GPUMonitorSettings implements PersistentStateComponent<GPUMonitorSettings> {
    public Float windowSize = (float) 1;
    public Float refreshTime = (float) 0.5;

    public Boolean enableGPUTemp = true;
    public Boolean enableGPUUsage = true;
    public Boolean enableGPUMemory = true;

    public Boolean enableLight = true;
    public Boolean enableDarcula = false;
    public Boolean enableCustom = false;
    private StandardChartTheme defaultTheme = new StandardChartTheme("JFree/Shadow", true);
    private StandardChartTheme darculaTheme;

    Float getWindowSize() { return windowSize; }
    Float getRefreshTime() { return refreshTime; }

    Boolean getEnableGPUTemp() { return enableGPUTemp; }
    Boolean getEnableGPUUsage() { return enableGPUUsage; }
    Boolean getEnableGPUMemory() { return enableGPUMemory; }

    Boolean getEnableLight() { return enableLight; }
    Boolean getEnableDarcula() { return enableDarcula; }
    Boolean getEnableCustom() { return enableCustom; }

    StandardChartTheme getChartTheme() {
        if(enableDarcula) {
            if(darculaTheme == null) {
                darculaTheme = new StandardChartTheme("JFree/Shadow", true);
                Color color = new Color(60, 63, 65);
                darculaTheme.setChartBackgroundPaint(color);
                color = new Color(187, 186, 177);
                darculaTheme.setTickLabelPaint(color);
                color = new Color(187, 186, 177);
                darculaTheme.setTitlePaint(color);
                color = new Color(187, 186, 177);
                darculaTheme.setAxisLabelPaint(color);
            }
            return darculaTheme;
        } else {
            return defaultTheme;
        }
    }

    void setEnableGPUTemp(Boolean enableGPUTemp) { this.enableGPUTemp = enableGPUTemp; }
    void setEnableGPUUsage(Boolean enableGPUUsage) { this.enableGPUUsage = enableGPUUsage; }
    void setEnableGPUMemory(Boolean enableGPUMemory) { this.enableGPUMemory = enableGPUMemory; }

    void setEnableLight(Boolean enableLight) { this.enableLight = enableLight; }
    void setEnableDarcula(Boolean enableDarcula) { this.enableDarcula = enableDarcula; }
    void setEnableCustom(Boolean enableCustom) { this.enableCustom = enableCustom; }

    void setWindowSize(Float windowSize) { this.windowSize = windowSize; }
    void setRefreshTime(Float refreshTime) { this.refreshTime = refreshTime; }

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
