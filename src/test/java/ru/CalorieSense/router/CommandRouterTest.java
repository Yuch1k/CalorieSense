package ru.CalorieSense.router;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.command.Command;
import ru.CalorieSense.messaging.BotResponse;
import ru.CalorieSense.messaging.MessageSender;
import ru.CalorieSense.router.CommandRouter;

import static org.junit.jupiter.api.Assertions.*;

class CommandRouterTest {

    @Test
    void testRegisterAndHandleKnownCommand() throws Exception {
        Command fallback = Mockito.mock(Command.class);
        MessageSender sender = Mockito.mock(MessageSender.class);
        CommandRouter router = new CommandRouter(fallback, sender);

        Command testCommand = Mockito.mock(Command.class);
        BotResponse response = new BotResponse(1L, "test", null, null);
        Mockito.when(testCommand.execute(Mockito.any(Update.class))).thenReturn(response);

        router.register("/test", testCommand);

        Message message = Mockito.mock(Message.class);
        Mockito.when(message.getText()).thenReturn("/test");
        Mockito.when(message.getChatId()).thenReturn(1L);

        Update update = Mockito.mock(Update.class);
        Mockito.when(update.getMessage()).thenReturn(message);

        router.handle(update);
        Mockito.verify(testCommand).execute(update);
        Mockito.verify(sender).sendBotResponse(response);
    }

    @Test
    void testFallbackForUnknownCommand() throws Exception {
        Command fallback = Mockito.mock(Command.class);
        MessageSender sender = Mockito.mock(MessageSender.class);
        CommandRouter router = new CommandRouter(fallback, sender);

        BotResponse response = new BotResponse(2L, "fallback", null, null);
        Mockito.when(fallback.execute(Mockito.any(Update.class))).thenReturn(response);

        Message message = Mockito.mock(Message.class);
        Mockito.when(message.getText()).thenReturn("/unknown");
        Mockito.when(message.getChatId()).thenReturn(2L);

        Update update = Mockito.mock(Update.class);
        Mockito.when(update.getMessage()).thenReturn(message);

        router.handle(update);
        Mockito.verify(fallback).execute(update);
        Mockito.verify(sender).sendBotResponse(response);
    }

    @Test
    void testButtonToCommandMapping() throws Exception {
        Command fallback = Mockito.mock(Command.class);
        MessageSender sender = Mockito.mock(MessageSender.class);
        CommandRouter router = new CommandRouter(fallback, sender);

        Command caloriesCommand = Mockito.mock(Command.class);
        BotResponse resp = new BotResponse(3L, "calories", null, null);
        Mockito.when(caloriesCommand.execute(Mockito.any(Update.class))).thenReturn(resp);

        router.register("/calories", caloriesCommand);

        Message message = Mockito.mock(Message.class);
        Mockito.when(message.getText()).thenReturn("📊 Мои калории");
        Mockito.when(message.getChatId()).thenReturn(3L);

        Update update = Mockito.mock(Update.class);
        Mockito.when(update.getMessage()).thenReturn(message);

        router.handle(update);
        Mockito.verify(caloriesCommand).execute(update);
        Mockito.verify(sender).sendBotResponse(resp);
    }

    @Test
    void testScenarioCommandAndActiveState() throws Exception {
        Command fallback = Mockito.mock(Command.class);
        MessageSender sender = Mockito.mock(MessageSender.class);
        CommandRouter router = new CommandRouter(fallback, sender);

        Command scenarioCommand = Mockito.mock(Command.class);
        Mockito.when(scenarioCommand.execute(Mockito.any(Update.class)))
                .thenReturn(new BotResponse(5L, "step 1", null, null))
                .thenReturn(new BotResponse(5L, "step 2", null, null));

        router.register("/register", scenarioCommand);

        Message msg1 = Mockito.mock(Message.class);
        Mockito.when(msg1.getText()).thenReturn("/register");
        Mockito.when(msg1.getChatId()).thenReturn(5L);
        Update upd1 = Mockito.mock(Update.class);
        Mockito.when(upd1.getMessage()).thenReturn(msg1);
        router.handle(upd1);
        Mockito.verify(sender).sendBotResponse(Mockito.argThat(r -> r.getText().equals("step 1")));

        Message msg2 = Mockito.mock(Message.class);
        Mockito.when(msg2.getText()).thenReturn("180");
        Mockito.when(msg2.getChatId()).thenReturn(5L);
        Update upd2 = Mockito.mock(Update.class);
        Mockito.when(upd2.getMessage()).thenReturn(msg2);
        router.handle(upd2);
        Mockito.verify(sender).sendBotResponse(Mockito.argThat(r -> r.getText().equals("step 2")));
    }

    @Test
    void testScenarioDeactivationOnNonScenarioCommand() throws Exception {
        Command fallback = Mockito.mock(Command.class);
        MessageSender sender = Mockito.mock(MessageSender.class);
        CommandRouter router = new CommandRouter(fallback, sender);

        Command scenarioCommand = Mockito.mock(Command.class);
        Mockito.when(scenarioCommand.getClass().getSimpleName()).thenReturn("ManualInputCommand");
        Mockito.when(scenarioCommand.execute(Mockito.any(Update.class)))
                .thenReturn(new BotResponse(6L, "manual step", null, null));

        Command regularCommand = Mockito.mock(Command.class);
        Mockito.when(regularCommand.execute(Mockito.any(Update.class)))
                .thenReturn(new BotResponse(6L, "regular", null, null));
        router.register("/manualinput", scenarioCommand);
        router.register("/profile", regularCommand);

        Message msgStart = Mockito.mock(Message.class);
        Mockito.when(msgStart.getText()).thenReturn("/manualinput");
        Mockito.when(msgStart.getChatId()).thenReturn(6L);
        Update updStart = Mockito.mock(Update.class);
        Mockito.when(updStart.getMessage()).thenReturn(msgStart);
        router.handle(updStart);

        Message msgStop = Mockito.mock(Message.class);
        Mockito.when(msgStop.getText()).thenReturn("/profile");
        Mockito.when(msgStop.getChatId()).thenReturn(6L);
        Update updStop = Mockito.mock(Update.class);
        Mockito.when(updStop.getMessage()).thenReturn(msgStop);
        router.handle(updStop);

        Message msgFree = Mockito.mock(Message.class);
        Mockito.when(msgFree.getText()).thenReturn("Something");
        Mockito.when(msgFree.getChatId()).thenReturn(6L);
        Update updFree = Mockito.mock(Update.class);
        Mockito.when(updFree.getMessage()).thenReturn(msgFree);
        router.handle(updFree);

        Mockito.verify(fallback).execute(updFree);
    }
}
