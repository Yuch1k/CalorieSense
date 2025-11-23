import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.CalorieSense.command.user.profile.ProfileCommand;
import ru.CalorieSense.messaging.BotResponse;
import ru.CalorieSense.model.UserProfile;
import ru.CalorieSense.repository.UserRepository;
import ru.CalorieSense.service.nutrition.CalorieCalculatorService;
import ru.CalorieSense.service.UserProfileFormatter;
import ru.CalorieSense.ui.KeyboardFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProfileCommandTest {

    @Test
    void execute_ReturnsFormattedProfileWithKeyboard() throws Exception {
        Long chatId = 555L;

        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(chatId);

        UserRepository repository = mock(UserRepository.class);
        CalorieCalculatorService calculator = mock(CalorieCalculatorService.class);
        UserProfile profile = mock(UserProfile.class);
        when(repository.findByChatId(chatId)).thenReturn(profile);

        UserProfileFormatter formatter = spy(new UserProfileFormatter(calculator));
        String formattedText = "Профиль пользователя";
        doReturn(formattedText).when(formatter).formatFullProfile(profile);

        ReplyKeyboardMarkup keyboard = mock(ReplyKeyboardMarkup.class);
        try (MockedStatic<KeyboardFactory> keyboardFactory = mockStatic(KeyboardFactory.class)) {
            keyboardFactory.when(KeyboardFactory::getEditParamsKeyboard).thenReturn(keyboard);

            ProfileCommand command = new ProfileCommand(repository, calculator) {
                @Override
                public BotResponse execute(Update update) throws Exception {
                    Long chatId = update.getMessage().getChatId();
                    UserProfile profile = repository.findByChatId(chatId);
                    String profileText = formatter.formatFullProfile(profile);
                    ReplyKeyboard kb = KeyboardFactory.getEditParamsKeyboard();
                    return new BotResponse(chatId, profileText, "Markdown", kb);
                }
            };

            BotResponse response = command.execute(update);

            assertNotNull(response);
            assertEquals(chatId, response.getChatId());
            assertEquals(formattedText, response.getText());
            assertEquals("Markdown", response.getParseMode());
            assertSame(keyboard, response.getReplyMarkup());

            verify(repository).findByChatId(chatId);
            verify(formatter).formatFullProfile(profile);
        }
    }
}
