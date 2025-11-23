import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.command.user.profile.SexCommand;
import ru.CalorieSense.messaging.BotResponse;
import ru.CalorieSense.model.UserProfile;
import ru.CalorieSense.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SexCommandTest {

    @Test
    void execute_ValidSex_SavesValue() throws Exception {
        Long chatId = 123L;
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(chatId);
        when(message.getText()).thenReturn("/sex М");

        UserRepository repository = mock(UserRepository.class);
        UserProfile profile = mock(UserProfile.class);
        when(repository.findByChatId(chatId)).thenReturn(profile);

        doNothing().when(profile).setSex("М");
        doNothing().when(repository).save(profile);

        SexCommand command = new SexCommand(repository);

        BotResponse response = command.execute(update);

        assertNotNull(response);
        verify(profile).setSex("М");
        verify(repository).save(profile);
        assertTrue(response.getText().contains("✅"));
        assertTrue(response.getText().toLowerCase().contains("пол"));
    }

    @Test
    void applyValue_InvalidSex_ThrowsException() {
        SexCommand command = new SexCommand(mock(UserRepository.class));
        UserProfile profile = mock(UserProfile.class);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            command.applyValue(profile, "X");
        });
        assertTrue(exception.getMessage().contains("Введите М или Ж"));
    }

    @Test
    void getPromptMessage_ReturnsCorrectPrompt() {
        SexCommand command = new SexCommand(mock(UserRepository.class));
        String prompt = command.getPromptMessage();

        assertNotNull(prompt);
        assertTrue(prompt.contains("Введите ваш пол"));
        assertTrue(prompt.contains("М или Ж"));
    }
}
