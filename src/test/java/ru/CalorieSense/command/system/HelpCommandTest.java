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

class HelpCommandTest {

    @Mock
    private BotApp botApp;

    @Mock
    private Update update;

    @Mock
    private Message message;

    private HelpCommand helpCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        helpCommand = new HelpCommand(botApp);

        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(123L);
    }

    @Test
    void testExecute_SendsHelpMessage() throws Exception {
        helpCommand.execute(update);

        ArgumentCaptor<SendMessage> messageCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(botApp).execute(messageCaptor.capture());

        SendMessage sentMessage = messageCaptor.getValue();
        assertTrue(sentMessage.getText().contains("команд") ||
                sentMessage.getText().contains("Помощь"));
        assertEquals("Markdown", sentMessage.getParseMode());
    }

    @Test
    void testExecute_ContainsKeyboardMarkup() throws Exception {
        helpCommand.execute(update);

        ArgumentCaptor<SendMessage> messageCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(botApp).execute(messageCaptor.capture());

        assertNotNull(messageCaptor.getValue().getReplyMarkup());
    }
}
