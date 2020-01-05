import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class GPUMonitorConfig implements SearchableConfigurable {
    private GPUMonitorConfigForm configForm;

    @NotNull
    @Override
    public String getId() {
        return "gpu-monitor";
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "GPU Monitor";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        configForm = new GPUMonitorConfigForm();
        configForm.createUI();
        return configForm.getRootPanel();
    }

    @Override
    public boolean isModified() {
        return configForm.isModified();
    }

    @Override
    public void apply() throws ConfigurationException {
        configForm.apply();
    }
}
