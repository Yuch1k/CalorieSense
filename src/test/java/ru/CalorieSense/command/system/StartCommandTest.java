import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ru.CalorieSense.command.system.StartCommand;
import ru.CalorieSense.messaging.BotResponse;
import ru.CalorieSense.ui.KeyboardFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StartCommandTest {

    @Test
    void execute_ReturnsWelcomeMessageAndMainKeyboard() throws Exception {
        Long chatId = 123L;
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(chatId);

        ReplyKeyboardMarkup mockKeyboard = mock(ReplyKeyboardMarkup.class);
        try (MockedStatic<KeyboardFactory> keyboardFactoryMock = mockStatic(KeyboardFactory.class)) {
            keyboardFactoryMock.when(KeyboardFactory::getMainKeyboard).thenReturn(mockKeyboard);

            StartCommand command = new StartCommand();

            // Act
            BotResponse response = command.execute(update);

            // Assert
            assertNotNull(response);
            assertEquals(chatId, response.getChatId());
            assertEquals("Markdown", response.getParseMode());
            assertSame(mockKeyboard, response.getReplyMarkup());
            assertTrue(response.getText().contains("Добро пожаловать в CalorieSense"));
        }
    }
}
