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

class GoalCommandTest {

    @Mock
    private BotApp botApp;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Update update;

    @Mock
    private Message message;

    private GoalCommand goalCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        goalCommand = new GoalCommand(botApp, userRepository);

        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(123L);
    }

    @Test
    void testExecute_ButtonClick_ShowsPrompt() throws Exception {
        when(message.getText()).thenReturn("🎯 Цель");
        UserProfile profile = new UserProfile(123L);
        when(userRepository.findByChatId(123L)).thenReturn(profile);

        goalCommand.execute(update);

        ArgumentCaptor<SendMessage> messageCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(botApp).execute(messageCaptor.capture());

        SendMessage sentMessage = messageCaptor.getValue();
        assertTrue(sentMessage.getText().contains("Текущая цель"));
        assertTrue(sentMessage.getText().contains("/goal 1"));
    }

    @Test
    void testExecute_WithGoal1_SetsLose() throws Exception {
        when(message.getText()).thenReturn("/goal 1");
        UserProfile profile = new UserProfile(123L);
        when(userRepository.findByChatId(123L)).thenReturn(profile);

        goalCommand.execute(update);

        assertEquals("lose", profile.getGoal());
        verify(userRepository).save(profile);

        ArgumentCaptor<SendMessage> messageCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(botApp).execute(messageCaptor.capture());
        assertTrue(messageCaptor.getValue().getText().contains("Похудение"));
    }

    @Test
    void testExecute_WithGoal2_SetsMaintain() throws Exception {
        when(message.getText()).thenReturn("/goal 2");
        UserProfile profile = new UserProfile(123L);
        when(userRepository.findByChatId(123L)).thenReturn(profile);

        goalCommand.execute(update);

        assertEquals("maintain", profile.getGoal());
        verify(userRepository).save(profile);
    }

    @Test
    void testExecute_WithGoal3_SetsGain() throws Exception {
        when(message.getText()).thenReturn("/goal 3");
        UserProfile profile = new UserProfile(123L);
        when(userRepository.findByChatId(123L)).thenReturn(profile);

        goalCommand.execute(update);

        assertEquals("gain", profile.getGoal());
        verify(userRepository).save(profile);
    }

    @Test
    void testExecute_WithTextLose_SetsLose() throws Exception {
        when(message.getText()).thenReturn("/goal похудение");
        UserProfile profile = new UserProfile(123L);
        when(userRepository.findByChatId(123L)).thenReturn(profile);

        goalCommand.execute(update);

        assertEquals("lose", profile.getGoal());
        verify(userRepository).save(profile);
    }

    @Test
    void testExecute_WithInvalidGoal_ShowsError() throws Exception {
        when(message.getText()).thenReturn("/goal 5");
        UserProfile profile = new UserProfile(123L);
        profile.setGoal("maintain");
        when(userRepository.findByChatId(123L)).thenReturn(profile);

        goalCommand.execute(update);

        assertEquals("maintain", profile.getGoal()); // Не изменилась
        verify(userRepository, never()).save(any());

        ArgumentCaptor<SendMessage> messageCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(botApp).execute(messageCaptor.capture());
        assertTrue(messageCaptor.getValue().getText().contains("Некорректный"));
    }
}
