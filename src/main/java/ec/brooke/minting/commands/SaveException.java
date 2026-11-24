package ec.brooke.minting.commands;

import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.chat.Component;

public class SaveException implements CommandExceptionType {
    private SaveException() {
    }

    public static CommandSyntaxException create(String message) {
        return new CommandSyntaxException(new SaveException(), Component.translatable("command.minting.render.save.failure", message));
    }
}
