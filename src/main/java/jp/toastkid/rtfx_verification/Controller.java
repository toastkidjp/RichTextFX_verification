package jp.toastkid.rtfx_verification;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.fxmisc.richtext.CodeArea;

import com.sun.javafx.PlatformUtil;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.InputMethodTextRun;
import javafx.scene.shape.Shape;

public class Controller implements Initializable {

    @FXML
    private CodeArea improved;

    /** Start of the text under input method composition. */
    private int imstart;

    /** Length of the text under input method composition. */
    private int imlength;

    /** Holds concrete attributes for the composition runs. */
    private final List<Shape> imattrs = new ArrayList<Shape>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        improved.setInputMethodRequests(new EditorInputMethodRequests(improved));
        improved.setOnInputMethodTextChanged(this::handleInputMethodEvent);
    }

    /**
     * Handle input method event.
     * @param event
     */
    private void handleInputMethodEvent(final InputMethodEvent event) {
        if (!improved.isEditable() || improved.isDisabled()) {
            return;
        }

        // just replace the text on iOS
        if (PlatformUtil.isIOS()) {
           improved.replaceText(event.getCommitted());
           return;
        }

        // remove previous input method text (if any) or selected text
        if (imlength != 0) {
            //removeHighlight(imattrs);
            imattrs.clear();
            improved.selectRange(imstart, imstart + imlength);
        }

        // Insert committed text
        if (event.getCommitted().length() != 0) {
            final String committed = event.getCommitted();
            improved.replaceText(improved.getSelection(), committed);
        }

        // Replace composed text
        imstart = improved.getSelection().getStart();
        final StringBuilder composed = new StringBuilder();
        for (final InputMethodTextRun run : event.getComposed()) {
            composed.append(run.getText());
        }
        improved.replaceText(improved.getSelection(), composed.toString());
        imlength = composed.length();
        if (imlength == 0) {
            return;
        }
        int pos = imstart;
        for (final InputMethodTextRun run : event.getComposed()) {
            final int endPos = pos + run.getText().length();
            //createInputMethodAttributes(run.getHighlight(), pos, endPos);
            pos = endPos;
        }
        //addHighlight(imattrs, imstart);

        // Set caret position in composed text
        final int caretPos = event.getCaretPosition();
        if (caretPos >= 0 && caretPos < imlength) {
            improved.selectRange(imstart + caretPos, imstart + caretPos);
        }
    }

}
