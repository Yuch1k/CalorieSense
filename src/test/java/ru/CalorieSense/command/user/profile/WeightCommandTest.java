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

class WeightCommandTest {

    @Mock
    private BotApp botApp;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Update update;

    @Mock
    private Message message;

    private WeightCommand weightCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        weightCommand = new WeightCommand(botApp, userRepository);

        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(123L);
    }

    @Test
    void testExecute_WithValidWeight_UpdatesProfile() throws Exception {
        when(message.getText()).thenReturn("/weight 75.5");
        UserProfile profile = new UserProfile(123L);
        when(userRepository.findByChatId(123L)).thenReturn(profile);

        weightCommand.execute(update);

        assertEquals(75.5, profile.getWeight());
        verify(userRepository).save(profile);

        ArgumentCaptor<SendMessage> messageCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(botApp).execute(messageCaptor.capture());
        assertTrue(messageCaptor.getValue().getText().contains("обновлён"));
    }

    @Test
    void testExecute_WithDecimalWeight_UpdatesProfile() throws Exception {
        when(message.getText()).thenReturn("/weight 72.3");
        UserProfile profile = new UserProfile(123L);
        when(userRepository.findByChatId(123L)).thenReturn(profile);

        weightCommand.execute(update);

        assertEquals(72.3, profile.getWeight(), 0.01);
        verify(userRepository).save(profile);
    }

    @Test
    void testExecute_WithWeightTooLow_ShowsError() throws Exception {
        when(message.getText()).thenReturn("/weight 5");
        UserProfile profile = new UserProfile(123L);
        when(userRepository.findByChatId(123L)).thenReturn(profile);

        weightCommand.execute(update);

        assertEquals(0, profile.getWeight());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testExecute_WithWeightTooHigh_ShowsError() throws Exception {
        when(message.getText()).thenReturn("/weight 600");
        UserProfile profile = new UserProfile(123L);
        when(userRepository.findByChatId(123L)).thenReturn(profile);

        weightCommand.execute(update);

        assertEquals(0, profile.getWeight());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testExecute_BoundaryValue_JustAboveMinimum() throws Exception {
        when(message.getText()).thenReturn("/weight 10.1");
        UserProfile profile = new UserProfile(123L);
        when(userRepository.findByChatId(123L)).thenReturn(profile);

        weightCommand.execute(update);

        assertEquals(10.1, profile.getWeight(), 0.01);
        verify(userRepository).save(profile);
    }

    @Test
    void testExecute_BoundaryValue_Maximum() throws Exception {
        when(message.getText()).thenReturn("/weight 500");
        UserProfile profile = new UserProfile(123L);
        when(userRepository.findByChatId(123L)).thenReturn(profile);

        weightCommand.execute(update);

        assertEquals(500, profile.getWeight());
        verify(userRepository).save(profile);
    }
}
