package eu.suro.command

import cloud.commandframework.annotations.CommandMethod
import com.velocitypowered.api.command.CommandSource
import net.kyori.adventure.text.Component

class TestCommand {

    @CommandMethod("test")
    fun test(sender: CommandSource){
        sender.sendMessage(Component.text("Test"))
    }
}