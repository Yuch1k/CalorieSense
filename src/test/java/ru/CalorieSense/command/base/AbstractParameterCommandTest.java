package ru.CalorieSense.command.base;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.messaging.BotResponse;
import ru.CalorieSense.model.UserProfile;
import ru.CalorieSense.repository.UserRepository;
import ru.CalorieSense.ui.KeyboardFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AbstractParameterCommandTest {

    // Простейшая конкретная команда для теста
    static class TestParamCommand extends AbstractParameterCommand {

        public TestParamCommand(UserRepository repository) {
            super(repository, "/test", "параметр");
        }

        @Override
        protected void applyValue(UserProfile profile, String value) throws Exception {
            if ("bad".equals(value)) {
                throw new IllegalArgumentException("некорректное значение");
            }
            profile.setGoal(value); // просто любое поле, чтобы было действие
        }

        @Override
        protected String getExampleValue() {
            return "ok";
        }
    }

    private Update buildUpdate(long chatId, String text) {
        Message msg = new Message();
        msg.setChat(new Chat(chatId, "private"));
        msg.setText(text);

        Update u = new Update();
        u.setMessage(msg);
        return u;
    }

    @Test
    void whenNoArgument_sendsPromptWithEditKeyboard() throws Exception {
        UserRepository repo = mock(UserRepository.class);
        TestParamCommand cmd = new TestParamCommand(repo);

        BotResponse resp = cmd.execute(buildUpdate(1L, "/test"));

        assertTrue(resp.getText().contains("Введите параметр"));
        // клавиатуру проверяем только на то, что она не null
        assertNotNull(resp.getReplyMarkup());
        verify(repo, never()).save(any());
    }

    @Test
    void whenValidArgument_updatesProfileAndReturnsSuccess() throws Exception {
        UserRepository repo = mock(UserRepository.class);
        UserProfile profile = new UserProfile(1L);
        when(repo.findByChatId(1L)).thenReturn(profile);

        TestParamCommand cmd = new TestParamCommand(repo);

        BotResponse resp = cmd.execute(buildUpdate(1L, "/test newGoal"));

        assertTrue(resp.getText().contains("обновлён"));
        assertEquals("newGoal", profile.getGoal());
        verify(repo, times(1)).save(profile);
        assertNotNull(resp.getReplyMarkup()); // главное меню
    }

    @Test
    void whenApplyThrowsException_returnsErrorAndDoesNotSave() throws Exception {
        UserRepository repo = mock(UserRepository.class);
        UserProfile profile = new UserProfile(2L);
        when(repo.findByChatId(2L)).thenReturn(profile);

        TestParamCommand cmd = new TestParamCommand(repo);

        BotResponse resp = cmd.execute(buildUpdate(2L, "/test bad"));

        assertTrue(resp.getText().contains("Ошибка"));
        assertTrue(resp.getText().contains("некорректное значение"));
        verify(repo, never()).save(any());
        assertNotNull(resp.getReplyMarkup()); // клавиатура редактирования
    }
}
