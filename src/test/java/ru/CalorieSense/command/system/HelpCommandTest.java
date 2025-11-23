import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ru.CalorieSense.command.system.HelpCommand;
import ru.CalorieSense.messaging.BotResponse;
import ru.CalorieSense.ui.KeyboardFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HelpCommandTest {

    @Test
    void execute_ReturnsHelpMessageWithKeyboard() throws Exception {
        Long chatId = 123L;
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(chatId);

        ReplyKeyboardMarkup mockKeyboard = mock(ReplyKeyboardMarkup.class);
        try (MockedStatic<KeyboardFactory> keyboardFactoryMock = mockStatic(KeyboardFactory.class)) {
            keyboardFactoryMock.when(KeyboardFactory::getMainKeyboard).thenReturn(mockKeyboard);

            HelpCommand helpCommand = new HelpCommand();

            BotResponse response = helpCommand.execute(update);

            assertNotNull(response);
            assertEquals(chatId, response.getChatId());
            assertEquals("Markdown", response.getParseMode());
            assertSame(mockKeyboard, response.getReplyMarkup());
            assertTrue(response.getText().toLowerCase().contains("помощь")
                    || response.getText().toLowerCase().contains("команд"));
        }
    }
}
