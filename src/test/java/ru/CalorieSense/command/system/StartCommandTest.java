package ru.CalorieSense.command.system;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.BotApp;

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
    }

    @Test
    void testExecute_SendsWelcomeMessage() throws Exception {
        startCommand.execute(update);

        ArgumentCaptor<SendMessage> messageCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(botApp).execute(messageCaptor.capture());

        SendMessage sentMessage = messageCaptor.getValue();
        assertEquals("123", sentMessage.getChatId());
        assertTrue(sentMessage.getText().contains("Добро пожаловать") ||
                sentMessage.getText().contains("Привет"));
        assertNotNull(sentMessage.getReplyMarkup());
    }

    @Test
    void testExecute_CallsOnce() throws Exception {
        startCommand.execute(update);

        verify(botApp, times(1)).execute(any(SendMessage.class));
    }
}
