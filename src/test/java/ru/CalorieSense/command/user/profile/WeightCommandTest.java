import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.command.user.profile.WeightCommand;
import ru.CalorieSense.messaging.BotResponse;
import ru.CalorieSense.model.UserProfile;
import ru.CalorieSense.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WeightCommandTest {

    @Test
    void execute_ValidWeight_SavesValue() throws Exception {
        Long chatId = 200L;
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(chatId);
        when(message.getText()).thenReturn("/weight 80");

        UserRepository repository = mock(UserRepository.class);
        UserProfile profile = mock(UserProfile.class);
        when(repository.findByChatId(chatId)).thenReturn(profile);

        doNothing().when(profile).setWeight(80);
        doNothing().when(repository).save(profile);

        WeightCommand command = new WeightCommand(repository);

        BotResponse response = command.execute(update);

        assertNotNull(response);
        verify(profile).setWeight(80);
        verify(repository).save(profile);
        assertTrue(response.getText().contains("✅"));
        assertTrue(response.getText().toLowerCase().contains("вес"));
    }

    @Test
    void applyValue_InvalidWeight_ThrowsException() {
        WeightCommand command = new WeightCommand(mock(UserRepository.class));
        UserProfile profile = mock(UserProfile.class);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            command.applyValue(profile, "5");
        });
        assertTrue(exception.getMessage().contains("Некорректный вес"));

        exception = assertThrows(IllegalArgumentException.class, () -> {
            command.applyValue(profile, "600");
        });
        assertTrue(exception.getMessage().contains("Некорректный вес"));
    }

    @Test
    void getPromptMessage_ReturnsCorrectPrompt() {
        WeightCommand command = new WeightCommand(mock(UserRepository.class));
        String prompt = command.getPromptMessage();

        assertNotNull(prompt);
        assertTrue(prompt.contains("Введите ваш вес"));
        assertTrue(prompt.contains("Допустимый диапазон: 10-500 кг"));
    }
}
