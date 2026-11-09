package ru.CalorieSense;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BotAppSettingsTest {

    @Test
    void environmentVariableHasPriority() {
        assertEquals("env-token", BotApp.resolveSetting("env-token", "file-token"));
    }

    @Test
    void fallsBackToPropertiesFile() {
        assertEquals("file-token", BotApp.resolveSetting(null, "file-token"));
        assertEquals("file-token", BotApp.resolveSetting("   ", "file-token"));
    }

    @Test
    void valuesAreTrimmed() {
        assertEquals("abc", BotApp.resolveSetting("  abc  ", null));
    }

    @Test
    void returnsNullWhenNothingIsSet() {
        assertNull(BotApp.resolveSetting(null, null));
        assertNull(BotApp.resolveSetting("", ""));
    }
}
