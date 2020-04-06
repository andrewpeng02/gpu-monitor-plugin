import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.*;
import com.intellij.ui.content.*;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class GPUMonitorFactory implements ToolWindowFactory {
    // Create the tool window content.
    private Boolean enableMonitor = true;

    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        if(enableMonitor) {
            GPUMonitorWindow gpuMonitorWindow = new GPUMonitorWindow(toolWindow);
            ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
            Content content = contentFactory.createContent(gpuMonitorWindow.getContent(), "", false);
            toolWindow.getContentManager().addContent(content);
        } else {
            new ErrorDialog().show();
        }
    }

    @Override
    public boolean shouldBeAvailable(@NotNull Project project) {
        // Check if NVIDIA SMI is installed
        List<String> output = new ArrayList<>();
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
            enableMonitor = false;
        }

        if(output.size() < 50)
            enableMonitor = false;

        if(!enableMonitor)
            new ErrorDialog().show();

        return enableMonitor;
    }
}

