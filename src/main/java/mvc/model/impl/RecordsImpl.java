package mvc.model.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mvc.model.api.DifficultyEnum;
import mvc.model.api.Records;

/**
 * Implementation of Records saved in a properties file, one line per difficulty level.
 * If the file can't be read or written the game keeps working: the records only last for the session.
 */
public final class RecordsImpl implements Records {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecordsImpl.class);

    private final Path file;
    private final Map<DifficultyEnum, Integer> records = new EnumMap<>(DifficultyEnum.class);

    /**
     * Loads the records already saved, if any.
     *
     * @param file where the records are saved
     */
    public RecordsImpl(final Path file) {
        this.file = file;
        load();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized int getRecord(final DifficultyEnum difficulty) {
        return this.records.getOrDefault(difficulty, 0);
    }

    /**
     * {@inheritDoc}
     * Chiamato dal game loop a fine partita, mentre il menu può leggere i record dal thread di Swing:
     * per questo i metodi sono synchronized.
     */
    @Override
    public synchronized boolean submit(final DifficultyEnum difficulty, final int score) {
        if (score <= getRecord(difficulty)) {
            return false;
        }
        this.records.put(difficulty, score);
        save();
        return true;
    }

    private void load() {
        if (!Files.exists(this.file)) {
            return;
        }
        final Properties properties = new Properties();
        try (InputStream in = Files.newInputStream(this.file)) {
            properties.load(in);
        } catch (final IOException e) {
            LOGGER.warn("impossible to read the records from {}", this.file, e);
            return;
        }
        for (final DifficultyEnum difficulty : DifficultyEnum.values()) {
            final String value = properties.getProperty(difficulty.name());
            if (value != null) {
                try {
                    this.records.put(difficulty, Integer.parseInt(value.trim()));
                } catch (final NumberFormatException e) {
                    LOGGER.warn("invalid record for {}: {}", difficulty, value);
                }
            }
        }
    }

    private void save() {
        final Properties properties = new Properties();
        this.records.forEach((difficulty, score) -> properties.setProperty(difficulty.name(), String.valueOf(score)));
        try {
            final Path folder = this.file.getParent();
            if (folder != null) {
                Files.createDirectories(folder);
            }
            try (OutputStream out = Files.newOutputStream(this.file)) {
                properties.store(out, "Poly-Cut records");
            }
        } catch (final IOException e) {
            LOGGER.warn("impossible to save the records in {}", this.file, e);
        }
    }
}
