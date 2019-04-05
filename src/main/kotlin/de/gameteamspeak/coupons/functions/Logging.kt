package de.gameteamspeak.coupons.functions

import de.gameteamspeak.coupons.provider.ConfigProvider.Companion.messages
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.sendIfNotNull
import org.bukkit.command.CommandSender

//TODO: change the fun name
inline fun String.success(sender: CommandSender, block: () -> Unit) {
    messages["$this.Success"].sendIfNotNull(sender)
    block()
    messages["$this.Successfully"].sendIfNotNull(sender)
}