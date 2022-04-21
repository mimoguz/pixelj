package io.github.mimoguz.pixelj.views.previewScreen;

import io.github.mimoguz.pixelj.controls.StringView;
import io.github.mimoguz.pixelj.models.ProjectModel;
import io.github.mimoguz.pixelj.util.Detachable;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
public class PreviewScreen extends JPanel implements Detachable {
    private final List<StringView> lines = new ArrayList<>(1);
    private final ProjectModel project;
    private final JTextArea textInput;

    public PreviewScreen(final ProjectModel project) {
        this.project = project;

        textInput = new JTextArea();
    }

    @Override
    public void detach() {

    }
}
