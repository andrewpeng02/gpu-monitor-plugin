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
    public Float windowSize = (float) 2;
    public Float refreshTime = (float) 0.5;

    public Boolean enableGPUTemp = true;
    public Boolean enableGPUUsage = true;
    public Boolean enableGPUMemory = true;

    public Boolean enableLight = true;
    public Boolean enableDarcula = false;
    public Boolean enableCustom = false;
    private StandardChartTheme defaultTheme = new StandardChartTheme("JFree/Shadow", true);
    private StandardChartTheme darculaTheme;
    private StandardChartTheme customTheme;

    public String titleColor = "#000000";
    public String axisLabelColor = "#404040";
    public String tickColor = "#404040";
    public String backgroundColor = "#FFFFFF";
    public String lineColor = "#FF0000";
    public String plotBackgroundColor = "#C0C0C0";

    Float getWindowSize() { return windowSize; }
    Float getRefreshTime() { return refreshTime; }

    Boolean getEnableGPUTemp() { return enableGPUTemp; }
    Boolean getEnableGPUUsage() { return enableGPUUsage; }
    Boolean getEnableGPUMemory() { return enableGPUMemory; }

    Boolean getEnableLight() { return enableLight; }
    Boolean getEnableDarcula() { return enableDarcula; }
    Boolean getEnableCustom() { return enableCustom; }

    String getTitleColor() { return titleColor; }
    String getAxisLabelColor() { return axisLabelColor; }
    String getTickColor() { return tickColor; }
    String getBackgroundColor() { return backgroundColor; }
    String getLineColor() { return lineColor; }
    String getPlotBackgroundColor() { return plotBackgroundColor; }

    StandardChartTheme getChartTheme() {
        if(enableDarcula) {
            if(darculaTheme == null) {
                darculaTheme = new StandardChartTheme("JFree/Shadow", true);
                darculaTheme.setChartBackgroundPaint(Color.decode("#3C3F41"));;
                darculaTheme.setTickLabelPaint(Color.decode("#BBBAB1"));
                darculaTheme.setTitlePaint(Color.decode("#BBBAB1"));
                darculaTheme.setAxisLabelPaint(Color.decode("#BBBAB1"));
            }

            setLineColor("#ff0000");
            return darculaTheme;
        } else if (enableLight){
            setLineColor("#ff0000");
            return defaultTheme;
        } else {
            customTheme = new StandardChartTheme("JFree/Shadow", true);
            customTheme.setTitlePaint(Color.decode(getTitleColor()));
            customTheme.setAxisLabelPaint(Color.decode(getAxisLabelColor()));
            customTheme.setTickLabelPaint(Color.decode(getTickColor()));
            customTheme.setChartBackgroundPaint(Color.decode(getBackgroundColor()));
            customTheme.setPlotBackgroundPaint(Color.decode(getPlotBackgroundColor()));

            return customTheme;
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

    void setTitleColor(String titleColor) { this.titleColor = titleColor; }
    void setAxisLabelColor(String axisLabelColor) { this.axisLabelColor = axisLabelColor; }
    void setTickColor(String tickColor) { this.tickColor = tickColor; }
    void setBackgroundColor(String backgroundColor) { this.backgroundColor = backgroundColor; }
    void setLineColor(String lineColor) { this.lineColor = lineColor; }
    void setPlotBackgroundColor(String plotBackgroundColor) { this.plotBackgroundColor = plotBackgroundColor; }

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
