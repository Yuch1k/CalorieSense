package ru.CalorieSense.command.user.profile;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.BotApp;
import ru.CalorieSense.model.UserProfile;
import ru.CalorieSense.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class HeightCommandTest {

    @Mock
    private BotApp botApp;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Update update;

    @Mock
    private Message message;

    private HeightCommand heightCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        heightCommand = new HeightCommand(botApp, userRepository);

        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(123L);
    }

    @Test
    void testExecute_WithoutParameter_ShowsPrompt() throws Exception {
        when(message.getText()).thenReturn("📏 Рост");

        heightCommand.execute(update);

        ArgumentCaptor<SendMessage> messageCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(botApp).execute(messageCaptor.capture());

        SendMessage sentMessage = messageCaptor.getValue();
        assertTrue(sentMessage.getText().contains("Введите ваш рост"));
        assertTrue(sentMessage.getText().contains("/height"));
    }

    @Test
    void testExecute_WithValidHeight_UpdatesProfile() throws Exception {
        when(message.getText()).thenReturn("/height 175");
        UserProfile profile = new UserProfile(123L);
        when(userRepository.findByChatId(123L)).thenReturn(profile);

        heightCommand.execute(update);

        assertEquals(175, profile.getHeight());
        verify(userRepository).save(profile);

        ArgumentCaptor<SendMessage> messageCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(botApp).execute(messageCaptor.capture());
        assertTrue(messageCaptor.getValue().getText().contains("обновлён"));
    }

    @Test
    void testExecute_WithHeightTooLow_ShowsError() throws Exception {
        when(message.getText()).thenReturn("/height 50");
        UserProfile profile = new UserProfile(123L);
        when(userRepository.findByChatId(123L)).thenReturn(profile);

        heightCommand.execute(update);

        assertEquals(0, profile.getHeight()); // Не должен измениться
        verify(userRepository, never()).save(any());

        ArgumentCaptor<SendMessage> messageCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(botApp).execute(messageCaptor.capture());
        assertTrue(messageCaptor.getValue().getText().contains("Ошибка"));
    }

    @Test
    void testExecute_WithHeightTooHigh_ShowsError() throws Exception {
        when(message.getText()).thenReturn("/height 300");
        UserProfile profile = new UserProfile(123L);
        when(userRepository.findByChatId(123L)).thenReturn(profile);

        heightCommand.execute(update);

        assertEquals(0, profile.getHeight());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testExecute_WithInvalidInput_ShowsError() throws Exception {
        when(message.getText()).thenReturn("/height abc");
        UserProfile profile = new UserProfile(123L);
        when(userRepository.findByChatId(123L)).thenReturn(profile);

        heightCommand.execute(update);

        verify(userRepository, never()).save(any());
        ArgumentCaptor<SendMessage> messageCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(botApp).execute(messageCaptor.capture());
        assertTrue(messageCaptor.getValue().getText().contains("Ошибка"));
    }

    @Test
    void testExecute_BoundaryValue_MinimumValid() throws Exception {
        when(message.getText()).thenReturn("/height 100");
        UserProfile profile = new UserProfile(123L);
        when(userRepository.findByChatId(123L)).thenReturn(profile);

        heightCommand.execute(update);

        assertEquals(100, profile.getHeight());
        verify(userRepository).save(profile);
    }

    @Test
    void testExecute_BoundaryValue_MaximumValid() throws Exception {
        when(message.getText()).thenReturn("/height 250");
        UserProfile profile = new UserProfile(123L);
        when(userRepository.findByChatId(123L)).thenReturn(profile);

        heightCommand.execute(update);

        assertEquals(250, profile.getHeight());
        verify(userRepository).save(profile);
    }
}
