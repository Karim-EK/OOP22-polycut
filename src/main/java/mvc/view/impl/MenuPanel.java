package mvc.view.impl;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.GridLayout;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import mvc.controller.api.AppController;
import mvc.model.api.DifficultyEnum;

/**
 * Main menu: play, difficulty, options, rules and exit.
 */
public final class MenuPanel extends BackgroundPanel {

    private static final long serialVersionUID = 1L;
    private static final float TITLE_SIZE = 72f;
    private static final float OPTION_SIZE = 18f;

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

        Durante la partita, ESC torna al menu.
        """;

    private final transient AppController app;
    private final JButton difficultyButton;

    /**
     * @param app the controller called by the buttons
     */
    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP2",
        justification = "The menu calls the application controller shared by all the screens"
    )
    public MenuPanel(final AppController app) {
        super();
        this.app = app;

        final JLabel title = new JLabel("POLY-CUT", SwingConstants.CENTER);
        title.setFont(UiStyle.font(TITLE_SIZE));
        title.setForeground(UiStyle.DARK);
        addRow(title);

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
            this.difficultyButton.setText(difficultyText());
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
    }

    private String difficultyText() {
        return "DIFFICULTY: " + this.app.getDifficulty().name();
    }

    // I livelli si scorrono in ordine: EASY, NORMAL, HARD e poi di nuovo EASY
    private static DifficultyEnum next(final DifficultyEnum current) {
        final DifficultyEnum[] levels = DifficultyEnum.values();
        return levels[(current.ordinal() + 1) % levels.length];
    }

    // Le caselle applicano la scelta subito, senza bisogno di confermare
    private void showOptions() {
        final JCheckBox music = new JCheckBox("Music", this.app.isMusicEnabled());
        final JCheckBox effects = new JCheckBox("Sound effects", this.app.isEffectsEnabled());
        music.setFont(UiStyle.font(OPTION_SIZE));
        effects.setFont(UiStyle.font(OPTION_SIZE));
        music.addActionListener(e -> this.app.setMusicEnabled(music.isSelected()));
        effects.addActionListener(e -> this.app.setEffectsEnabled(effects.isSelected()));
        final JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(music);
        panel.add(effects);
        JOptionPane.showMessageDialog(this, panel, "Options", JOptionPane.PLAIN_MESSAGE);
    }
}
