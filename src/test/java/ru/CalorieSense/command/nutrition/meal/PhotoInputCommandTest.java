import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ru.CalorieSense.command.nutrition.meal.PhotoInputCommand;
import ru.CalorieSense.messaging.BotResponse;
import ru.CalorieSense.ui.KeyboardFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PhotoInputCommandTest {

    @Test
    void execute_ReturnsPhotoInputPlaceholderWithKeyboard() throws Exception {
        Long chatId = 456L;
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(chatId);

        ReplyKeyboardMarkup mockKeyboard = mock(ReplyKeyboardMarkup.class);

        try (MockedStatic<KeyboardFactory> keyboardFactoryMock = mockStatic(KeyboardFactory.class)) {
            keyboardFactoryMock.when(KeyboardFactory::getMealInputKeyboard).thenReturn(mockKeyboard);

            PhotoInputCommand command = new PhotoInputCommand();
            BotResponse response = command.execute(update);

            assertNotNull(response);
            assertEquals(chatId, response.getChatId());
            assertEquals("Markdown", response.getParseMode());
            assertSame(mockKeyboard, response.getReplyMarkup());
            assertTrue(response.getText().contains("Ввод по фото"));
            assertTrue(response.getText().toLowerCase().contains("разработке"));
        }
    }
}
