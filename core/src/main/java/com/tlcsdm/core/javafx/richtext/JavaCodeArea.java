package com.tlcsdm.core.javafx.richtext;

import cn.hutool.log.StaticLog;
import com.tlcsdm.core.factory.config.ThreadPoolTaskExecutor;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.Subscription;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Java Code TextArea
 * 
 * <pre>{@code
        JavaCodeArea codeArea = new JavaCodeArea();
        codeArea.replaceText(0, 0, sampleCode);

        Scene scene = new Scene(new StackPane(new VirtualizedScrollPane<>(codeArea)), 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("JavaCodeArea Demo");
        primaryStage.show();
 * }</pre>
 * 
 * @author: unknowIfGuestInDream
 * @date: 2023/8/3 11:21
 */
public class JavaCodeArea extends CodeArea {

    private static final String[] KEYWORDS = new String[] { "abstract", "assert", "boolean", "break", "byte", "case",
        "catch", "char", "class", "const", "continue", "default", "do", "double", "else", "enum", "extends", "final",
        "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long",
        "native", "new", "package", "private", "protected", "public", "return", "short", "static", "strictfp", "super",
        "switch", "synchronized", "this", "throw", "throws", "transient", "try", "void", "volatile", "while",
        "record" };

    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String SEMICOLON_PATTERN = "\\;";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";

    private static final Pattern PATTERN = Pattern
        .compile("(?<KEYWORD>" + KEYWORD_PATTERN + ")" + "|(?<PAREN>" + PAREN_PATTERN + ")" + "|(?<BRACE>"
            + BRACE_PATTERN + ")" + "|(?<BRACKET>" + BRACKET_PATTERN + ")" + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
            + "|(?<STRING>" + STRING_PATTERN + ")" + "|(?<COMMENT>" + COMMENT_PATTERN + ")");

    private ExecutorService executor;
    // recompute the syntax highlighting for all text, 500 ms after user stops
    // editing area
    // Note that this shows how it can be done but is not recommended for production
    // with
    // large files as it does a full scan of ALL the text every time there is a
    // change !
    /*
    Subscription cleanupWhenNoLongerNeedIt = codeArea
    
        // plain changes = ignore style changes that are emitted when syntax
        // highlighting is reapplied
        // multi plain changes = save computation by not rerunning the code multiple
        // times
        // when making multiple changes (e.g. renaming a method at multiple parts in
        // file)
        .multiPlainChanges()
    
        // do not emit an event until 500 ms have passed since the last emission of
        // previous stream
        .successionEnds(Duration.ofMillis(500))
    
        // run the following code block when previous stream emits an event
        .subscribe(ignore -> codeArea.setStyleSpans(0, computeHighlighting(codeArea.getText())));
    */
    // when no longer need syntax highlighting and wish to clean up memory leaks
    // run: `cleanupWhenNoLongerNeedIt.unsubscribe();`
    private Subscription subScript;

    public JavaCodeArea() {
        super();
        getStyleClass().add("text-java-area");
        getStylesheets()
            .add(getClass().getResource("/com/tlcsdm/core/static/javafx/richtext/java-keywords.css").toExternalForm());
        executor = ThreadPoolTaskExecutor.hasInitialized() ? ThreadPoolTaskExecutor.get()
            : Executors.newSingleThreadExecutor();
        this.setParagraphGraphicFactory(LineNumberFactory.get(this));
        subScript = this.multiPlainChanges().successionEnds(Duration.ofMillis(200)).retainLatestUntilLater(executor)
            .supplyTask(this::computeHighlightingAsync).awaitLatest(this.multiPlainChanges()).filterMap(t -> {
                if (t.isSuccess()) {
                    return Optional.of(t.get());
                } else {
                    StaticLog.error(t.getFailure());
                    return Optional.empty();
                }
            }).subscribe(this::applyHighlighting);

        // auto-indent: insert previous line's indents on enter
        final Pattern whiteSpace = Pattern.compile("^\\s+");
        this.addEventHandler(KeyEvent.KEY_PRESSED, KE -> {
            if (KE.getCode() == KeyCode.ENTER) {
                int caretPosition = this.getCaretPosition();
                int currentParagraph = this.getCurrentParagraph();
                Matcher m0 = whiteSpace.matcher(this.getParagraph(currentParagraph - 1).getSegments().get(0));
                if (m0.find()) {
                    Platform.runLater(() -> this.insertText(caretPosition, m0.group()));
                }
            }
        });
        this.setContextMenu(new CodeAreaDefaultContextMenu());
    }

    @Override
    public void dispose() {
        super.dispose();
        subScript.unsubscribe();
    }

    private Task<StyleSpans<Collection<String>>> computeHighlightingAsync() {
        String text = this.getText();
        Task<StyleSpans<Collection<String>>> task = new Task<StyleSpans<Collection<String>>>() {
            @Override
            protected StyleSpans<Collection<String>> call() throws Exception {
                return computeHighlighting(text);
            }
        };
        executor.execute(task);
        return task;
    }

    private void applyHighlighting(StyleSpans<Collection<String>> highlighting) {
        this.setStyleSpans(0, highlighting);
    }

    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        while (matcher.find()) {
            String styleClass = matcher.group("KEYWORD") != null ? "keyword"
                : matcher.group("PAREN") != null ? "paren"
                    : matcher.group("BRACE") != null ? "brace"
                        : matcher.group("BRACKET") != null ? "bracket"
                            : matcher.group("SEMICOLON") != null ? "semicolon"
                                : matcher.group("STRING") != null ? "string"
                                    : matcher.group("COMMENT") != null ? "comment" : null; /* never happens */
            assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }

}
