package ru.CalorieSense.router;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.CalorieSense.command.Command;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Message;

import static org.mockito.Mockito.*;

public class CommandRouterTest {

    @Test
    void testCommandRouting() throws Exception {
        Command fallback = mock(Command.class);
        Command testCommand = mock(Command.class);

        CommandRouter router = new CommandRouter(fallback);
        router.register("/hello", testCommand);

        Update update = new Update();
        Message msg = new Message();
        msg.setText("/hello");
        update.setMessage(msg);

        router.handle(update);

        verify(testCommand, times(1)).execute(update);
        verify(fallback, never()).execute(update);
    }
}
