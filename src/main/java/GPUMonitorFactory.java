import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.*;
import com.intellij.ui.content.*;

public class GPUMonitorFactory implements ToolWindowFactory {
    // Create the tool window content.
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        GPUMonitorWindow gpuMonitorWindow = new GPUMonitorWindow(toolWindow);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(gpuMonitorWindow.getContent(), "", false);
        toolWindow.getContentManager().addContent(content);
    }
}

