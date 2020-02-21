import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ErrorDialog extends DialogWrapper {
    private JPanel ErrorPanel;

    public ErrorDialog() {
        super(true);
        init();
        setTitle("GPU Monitor");
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return ErrorPanel;
    }

    @NotNull
    protected Action[] createActions() {
        Action helpAction = getHelpAction();
        return helpAction == myHelpAction && getHelpId() == null ?
                new Action[]{getOKAction()} :
                new Action[]{getOKAction(), helpAction};
    }
}
