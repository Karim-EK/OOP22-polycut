package mvc.view.impl;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.List;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import mvc.controller.api.AppController;
import mvc.controller.api.AudioManager;
import mvc.model.api.DifficultyEnum;
import mvc.view.api.GameFrame;

/**
 * Main menu: play, difficulty with its record, options, rules and exit.
 */
public final class MenuPanel extends BackgroundPanel {

    private static final long serialVersionUID = 1L;
    private static final float TITLE_SIZE = 72f;
    private static final float RECORD_SIZE = 20f;
    private static final float OPTION_SIZE = 16f;
    private static final int OPTION_GAP = 10;
    private static final int VOLUME_TICKS = 25;

    // Risoluzioni della finestra in 16:9
    private static final List<Dimension> RESOLUTIONS = List.of(
        new Dimension(1280, 720),
        new Dimension(1600, 900),
        new Dimension(1920, 1080),
        new Dimension(2560, 1440)
    );

    private static final String RULES = """
        Taglia i poligoni trascinando il mouse con il tasto sinistro premuto:
        il taglio vale solo se il movimento attraversa la figura.

        Ogni poligono tagliato vale 1 punto.
        Hai 3 vite: ne perdi una se tagli una bomba o se lasci cadere un poligono.

        Power-up:
        - Fiocco di neve (Freeze): blocca tutto per 3 secondi.
        - Cuore (1-Up): una vita in più.
        - Scudo (Safe Zone): per 5 secondi non perdi vite, né per le bombe né per i poligoni caduti.
        - x2: punti doppi per 10 secondi.

        Ogni livello di difficoltà ha il suo record.
        Durante la partita, ESC torna al menu.
        """;

    private final transient AppController app;
    private final transient GameFrame frame;
    private final JButton difficultyButton;
    private final JLabel recordLabel = new JLabel("", SwingConstants.CENTER);
    private int resolutionIndex;

    /**
     * @param app the controller called by the buttons
     * @param frame the window, for resolution and full screen
     */
    @SuppressFBWarnings(
        value = {"EI_EXPOSE_REP2", "SE_TRANSIENT_FIELD_NOT_RESTORED"},
        justification = "The menu calls the application controller and the window shared by all the screens;"
            + " the panels of the game are never serialized"
    )
    public MenuPanel(final AppController app, final GameFrame frame) {
        super();
        this.app = app;
        this.frame = frame;

        final JLabel title = new JLabel("POLY-CUT", SwingConstants.CENTER);
        title.setFont(UiStyle.font(TITLE_SIZE));
        title.setForeground(UiStyle.DARK);
        addRow(title);

        // Il record mostrato, sopra PLAY, è sempre quello del livello selezionato
        this.recordLabel.setFont(UiStyle.font(RECORD_SIZE));
        this.recordLabel.setForeground(UiStyle.DARK);
        addRow(this.recordLabel);

        final JButton play = UiStyle.button("PLAY");
        play.addActionListener(e -> {
            app.playClick();
            app.startGame();
        });
        addRow(play);

        this.difficultyButton = UiStyle.button(difficultyText());
        this.difficultyButton.addActionListener(e -> {
            app.playClick();
            app.setDifficulty(next(app.getDifficulty()));
            refresh();
        });
        addRow(this.difficultyButton);

        final JButton options = UiStyle.button("OPTIONS");
        options.addActionListener(e -> {
            app.playClick();
            showOptions();
        });
        addRow(options);

        final JButton rules = UiStyle.button("RULES");
        rules.addActionListener(e -> {
            app.playClick();
            JOptionPane.showMessageDialog(this, RULES, "Rules", JOptionPane.INFORMATION_MESSAGE);
        });
        addRow(rules);

        final JButton exit = UiStyle.button("EXIT");
        exit.addActionListener(e -> app.quit());
        addRow(exit);

        refresh();
    }

    /**
     * Updates the difficulty and its record: called when the level changes and every time the menu is shown.
     */
    public void refresh() {
        this.difficultyButton.setText(difficultyText());
        this.recordLabel.setText("RECORD: " + this.app.getRecord(this.app.getDifficulty()));
    }

    private String difficultyText() {
        return "DIFFICULTY: " + this.app.getDifficulty().name();
    }

    // I livelli si scorrono in ordine: EASY, NORMAL, HARD e poi di nuovo EASY
    private static DifficultyEnum next(final DifficultyEnum current) {
        final DifficultyEnum[] levels = DifficultyEnum.values();
        return levels[(current.ordinal() + 1) % levels.length];
    }

    // I volumi cambiano subito mentre si muove il cursore, così si sente il risultato;
    // risoluzione e schermo intero si applicano con OK, perché ricostruiscono la finestra
    private void showOptions() {
        final List<Dimension> resolutions = availableResolutions();
        final JComboBox<String> resolution = new JComboBox<>(
            resolutions.stream().map(r -> r.width + " x " + r.height).toArray(String[]::new));
        resolution.setSelectedIndex(Math.min(this.resolutionIndex, resolutions.size() - 1));
        final JCheckBox fullScreen = new JCheckBox("", this.frame.isFullScreen());
        resolution.setEnabled(!fullScreen.isSelected());
        fullScreen.addActionListener(e -> resolution.setEnabled(!fullScreen.isSelected()));

        final JSlider music = volumeSlider(this.app.getMusicVolume());
        music.addChangeListener(e -> this.app.setMusicVolume(music.getValue()));
        final JSlider effects = volumeSlider(this.app.getEffectsVolume());
        effects.addChangeListener(e -> {
            this.app.setEffectsVolume(effects.getValue());
            // Anteprima del volume degli effetti quando si rilascia il cursore
            if (!effects.getValueIsAdjusting()) {
                this.app.playClick();
            }
        });

        final JPanel panel = new JPanel(new GridLayout(0, 2, OPTION_GAP, OPTION_GAP));
        addOption(panel, "Resolution", resolution);
        addOption(panel, "Full screen", fullScreen);
        addOption(panel, "Music", music);
        addOption(panel, "Sound effects", effects);

        final int choice = JOptionPane.showConfirmDialog(this, panel, "Options",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (choice == JOptionPane.OK_OPTION) {
            this.resolutionIndex = resolution.getSelectedIndex();
            final Dimension size = resolutions.get(this.resolutionIndex);
            this.frame.setWindowSize(size.width, size.height);
            this.frame.setFullScreen(fullScreen.isSelected());
        }
    }

    // Solo le risoluzioni che entrano nello schermo; la prima c'è sempre, anche su schermi piccoli
    private static List<Dimension> availableResolutions() {
        final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        return RESOLUTIONS.stream()
            .filter(r -> r.equals(RESOLUTIONS.get(0)) || r.width <= screen.width && r.height <= screen.height)
            .toList();
    }

    private static void addOption(final JPanel panel, final String name, final JComponent control) {
        final JLabel label = new JLabel(name);
        label.setFont(UiStyle.font(OPTION_SIZE));
        panel.add(label);
        panel.add(control);
    }

    private static JSlider volumeSlider(final int value) {
        final JSlider slider = new JSlider(0, AudioManager.MAX_VOLUME, value);
        slider.setMajorTickSpacing(VOLUME_TICKS);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        return slider;
    }
}
