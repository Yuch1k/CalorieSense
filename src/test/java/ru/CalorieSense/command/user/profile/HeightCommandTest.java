import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.command.user.profile.HeightCommand;
import ru.CalorieSense.messaging.BotResponse;
import ru.CalorieSense.model.UserProfile;
import ru.CalorieSense.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HeightCommandTest {

    @Test
    void execute_ValidHeight_SavesValue() throws Exception {
        Long chatId = 100L;
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(chatId);
        when(message.getText()).thenReturn("/height 180");

        UserRepository repository = mock(UserRepository.class);
        UserProfile profile = mock(UserProfile.class);
        when(repository.findByChatId(chatId)).thenReturn(profile);

        doNothing().when(profile).setHeight(180);
        doNothing().when(repository).save(profile);

        HeightCommand command = new HeightCommand(repository);

        BotResponse response = command.execute(update);

        assertNotNull(response);
        verify(profile).setHeight(180);
        verify(repository).save(profile);
        assertTrue(response.getText().contains("✅"));
        assertTrue(response.getText().toLowerCase().contains("рост"));
    }

    @Test
    void applyValue_InvalidHeight_ThrowsException() {
        HeightCommand command = new HeightCommand(mock(UserRepository.class));
        UserProfile profile = mock(UserProfile.class);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            command.applyValue(profile, "90");
        });
        assertTrue(exception.getMessage().contains("Некорректный рост"));

        exception = assertThrows(IllegalArgumentException.class, () -> {
            command.applyValue(profile, "300");
        });
        assertTrue(exception.getMessage().contains("Некорректный рост"));
    }

    @Test
    void getPromptMessage_ReturnsCorrectPrompt() {
        HeightCommand command = new HeightCommand(mock(UserRepository.class));
        String prompt = command.getPromptMessage();

        assertNotNull(prompt);
        assertTrue(prompt.contains("Введите ваш рост"));
        assertTrue(prompt.contains("Допустимый диапазон: 100-250 см"));
    }
}
