package ru.CalorieSense.router;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.command.Command;

import static org.mockito.Mockito.*;

class CommandRouterTest {

    @Mock
    private Command fallbackCommand;

    @Mock
    private Command startCommand;

    @Mock
    private Command helpCommand;

    @Mock
    private Update update;

    @Mock
    private Message message;

    private CommandRouter router;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        router = new CommandRouter(fallbackCommand);

        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(123L);
    }

    @Test
    void testHandle_RegisteredCommand_ExecutesCommand() throws Exception {
        router.register("/start", startCommand);
        when(message.getText()).thenReturn("/start");

        router.handle(update);

        verify(startCommand).execute(update);
        verify(fallbackCommand, never()).execute(any());
    }

    @Test
    void testHandle_UnregisteredCommand_ExecutesFallback() throws Exception {
        when(message.getText()).thenReturn("/unknown");

        router.handle(update);

        verify(fallbackCommand).execute(update);
    }

    @Test
    void testHandle_ButtonText_TransformsToCommand() throws Exception {
        router.register("/help", helpCommand);
        when(message.getText()).thenReturn("❓ Помощь");

        router.handle(update);

        verify(helpCommand).execute(update);
    }

    @Test
    void testHandle_CommandWithParameters_ExecutesCommand() throws Exception {
        Command weightCommand = mock(Command.class);
        router.register("/weight", weightCommand);
        when(message.getText()).thenReturn("/weight 75");

        router.handle(update);

        verify(weightCommand).execute(update);
    }

    @Test
    void testHandle_CaseInsensitive_ExecutesCommand() throws Exception {
        router.register("/start", startCommand);
        when(message.getText()).thenReturn("/START");

        router.handle(update);

        verify(startCommand).execute(update);
    }

    @Test
    void testHandle_NonCommand_ExecutesFallback() throws Exception {
        when(message.getText()).thenReturn("Hello");

        router.handle(update);

        verify(fallbackCommand).execute(update);
    }

    @Test
    void testRegister_MultipleCommands() throws Exception {
        router.register("/start", startCommand);
        router.register("/help", helpCommand);

        when(message.getText()).thenReturn("/help");

        router.handle(update);

        verify(helpCommand).execute(update);
        verify(startCommand, never()).execute(any());
    }
}
