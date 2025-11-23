import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.command.user.profile.AgeCommand;
import ru.CalorieSense.messaging.BotResponse;
import ru.CalorieSense.model.UserProfile;
import ru.CalorieSense.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AgeCommandTest {

    @Test
    void execute_ValidAge_SavesToProfile() throws Exception {
        Long chatId = 222L;
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(chatId);
        when(message.getText()).thenReturn("/age 27");

        UserRepository repository = mock(UserRepository.class);
        UserProfile profile = mock(UserProfile.class);
        when(repository.findByChatId(chatId)).thenReturn(profile);

        doNothing().when(profile).setAge(27);
        doNothing().when(repository).save(profile);

        AgeCommand command = new AgeCommand(repository);

        BotResponse response = command.execute(update);

        assertNotNull(response);
        verify(profile).setAge(27);
        verify(repository).save(profile);
        assertTrue(response.getText().contains("✅"));
        assertTrue(response.getText().contains("Возраст обновлён"));
    }

    @Test
    void execute_InvalidAge_ReturnsError() throws Exception {
        Long chatId = 222L;
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(chatId);
        when(message.getText()).thenReturn("/age 3"); // недопустимо

        UserRepository repository = mock(UserRepository.class);
        UserProfile profile = mock(UserProfile.class);
        when(repository.findByChatId(chatId)).thenReturn(profile);

        AgeCommand command = new AgeCommand(repository);

        BotResponse response = command.execute(update);

        assertNotNull(response);
        assertTrue(response.getText().toLowerCase().contains("ошибка"));
        assertTrue(response.getText().contains("10-120"));
    }

    @Test
    void execute_PromptMessage_ReturnsHelp() throws Exception {
        UserRepository repository = mock(UserRepository.class);
        AgeCommand command = new AgeCommand(repository);

        String promptMsg = command.getPromptMessage();

        assertTrue(promptMsg.contains("Введите ваш возраст"));
        assertTrue(promptMsg.contains("Допустимый диапазон"));
    }
}
