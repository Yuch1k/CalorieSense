package ru.CalorieSense.command.system;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.BotApp;
import ru.CalorieSense.messaging.BotResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StartCommandTest {

    @Mock
    private BotApp botApp;

    @Mock
    private Update update;

    @Mock
    private Message message;

    private StartCommand startCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startCommand = new StartCommand(botApp);

        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(123L);
        when(message.getText()).thenReturn("/start");
    }

    @Test
    void testExecute_ReturnsWelcomeResponse() throws Exception {
        BotResponse response = startCommand.execute(update);

        assertNotNull(response);
        assertEquals(123L, response.getChatId());
        String text = response.getText();
        assertTrue(text.contains("Добро пожаловать") || text.contains("Привет"));
        assertEquals("Markdown", response.getParseMode());
        assertNotNull(response.getReplyMarkup());
    }
}
