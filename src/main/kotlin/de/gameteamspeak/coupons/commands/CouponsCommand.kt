package de.gameteamspeak.coupons.commands

import de.gameteamspeak.coupons.Coupons
import de.gameteamspeak.coupons.data.Coupon
import de.gameteamspeak.coupons.provider.ConfigProvider.Companion.messages
import net.darkdevelopers.darkbedrock.darkness.spigot.commands.Command
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.sendIfNotNull
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

class CouponsCommand(javaPlugin: JavaPlugin) : Command(
        javaPlugin,
        commandName = "coupons",
        permission = "coupons.commands.coupons",
        usage = "create <Name>" +
                "|give <Name>" +
                "|delete <Name>" +
                "|edit <Name> <DisplayName, Lore, ItemType, SubID, Command> <Value>",
        minLength = 2,
        maxLength = Int.MAX_VALUE

) {

    override fun perform(sender: CommandSender, args: Array<String>) {

        if(args[0].equals("create", true)) {

            messages["Coupons.Commands.Coupons.Create.Success"].sendIfNotNull(sender)
            Coupons.instance.coupons.add(Coupon(args[1]))
            messages["Coupons.Commands.Coupons.Create.Successfully"].sendIfNotNull(sender)

        } else sendUseMessage(sender)


    }

}