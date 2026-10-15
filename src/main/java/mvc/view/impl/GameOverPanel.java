package mvc.view.impl;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import mvc.controller.api.AppController;

/**
 * Game over screen: final score, record of the level just played, play again or back to the menu.
 */
public final class GameOverPanel extends BackgroundPanel {

    private static final long serialVersionUID = 1L;
    private static final float TITLE_SIZE = 64f;
    private static final float SCORE_SIZE = 32f;
    private static final float BEST_SIZE = 20f;

    private final JLabel scoreLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel bestLabel = new JLabel("", SwingConstants.CENTER);

    /**
     * @param app the controller called by the buttons
     */
    public GameOverPanel(final AppController app) {
        super();
        final JLabel title = new JLabel("GAME OVER", SwingConstants.CENTER);
        title.setFont(UiStyle.font(TITLE_SIZE));
        title.setForeground(UiStyle.DARK);
        addRow(title);

        this.scoreLabel.setFont(UiStyle.font(SCORE_SIZE));
        this.scoreLabel.setForeground(UiStyle.DARK);
        addRow(this.scoreLabel);

        this.bestLabel.setFont(UiStyle.font(BEST_SIZE));
        this.bestLabel.setForeground(UiStyle.DARK);
        addRow(this.bestLabel);

        final JButton again = UiStyle.button("PLAY AGAIN");
        again.addActionListener(e -> {
            app.playClick();
            app.startGame();
        });
        addRow(again);

        final JButton menu = UiStyle.button("MENU");
        menu.addActionListener(e -> {
            app.playClick();
            app.backToMenu();
        });
        addRow(menu);
    }

    /**
     * Updates the labels with the score of the game just finished.
     *
     * @param score the final score
     * @param record the record of the level, this game included
     * @param newRecord true if this game set the record
     */
    public void showScore(final int score, final int record, final boolean newRecord) {
        this.scoreLabel.setText("SCORE " + score);
        this.bestLabel.setText(newRecord ? "NEW RECORD!" : "RECORD " + record);
    }
}
