package de.gameteamspeak.coupons.commands

import de.gameteamspeak.coupons.Coupons
import de.gameteamspeak.coupons.data.Coupon
import de.gameteamspeak.coupons.functions.success
import de.gameteamspeak.coupons.provider.ConfigProvider.Companion.messages
import de.tr7zw.itemnbtapi.NBTItem
import net.darkdevelopers.darkbedrock.darkness.spigot.builder.item.ItemBuilder
import net.darkdevelopers.darkbedrock.darkness.spigot.commands.Command
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.sendIfNotNull
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.toMaterial
import net.darkdevelopers.darkbedrock.darkness.spigot.utils.isPlayer
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

class CouponsCommand(javaPlugin: JavaPlugin) : Command(
        javaPlugin,
        commandName = "coupons",
        permission = "coupons.commands.coupons",
        usage = "create <Name>" +
                "|delete <Name>" +
                "|give <Name> [Amount]" +
                "|edit <Name> <DisplayName/Lore/ItemType/SubID/Command> <Value>",
        minLength = 2,
        maxLength = Int.MAX_VALUE

) {

    private val coupons get() = Coupons.instance.coupons

    override fun perform(sender: CommandSender, args: Array<String>) {


        //... edit Hallo Displayname Hallo Welt
        //... edit Hallo Command say Soos
        val name = args[1] //edit
        val prefix = "Coupons.Commands.Coupons."
        when (args[0].toLowerCase()) { //edit
            "create" -> {

                if (args.size == 2) {

                    "${prefix}Create".success(sender) {
                        coupons += Coupon(name)
                    }

                } else sendUseMessage(sender)

            }
            "delete" -> {

                if (args.size == 2) "${prefix}Delete".success(sender) {
                    coupons.removeIf { it.name == name }
                } else sendUseMessage(sender)

            }
            "give" -> sender.isPlayer { player ->

                @Suppress("NAME_SHADOWING")
                val prefix = "${prefix}Give."

                if (args.size <= 3) {

                    val coupon = coupons.find { it.name == name }
                    if (coupon != null) {

                        messages["${prefix}Success"]?.replace("<Name>", name, true).sendIfNotNull(player)
//                    val amount = if(args.size == 3) args[2].toIntOrNull() ?: 1 else 1
                        val amount = try {
                            args[2].toInt()
                        } catch (ex: Exception) {
                            1
                        }
                        val item = ItemBuilder(coupon.itemType)
                                .setAmount(amount)
                                .setDamage(coupon.itemSubID)
                                .setDisplayName(coupon.displayName)
                                .setLore(coupon.lore)
                                .build()

                        player.inventory.addItem(NBTItem(item).apply { setString("Coupon", coupon.name) }.item)
                        messages["${prefix}Successfully"]?.replace("<Name>", name, true).sendIfNotNull(player)

                    } else messages["${prefix}Failed.CanNotFindCoupon"]?.replace("<Name>", name, true).sendIfNotNull(player)

                } else sendUseMessage(sender)

            }
            "edit" -> {

                if (args.size < 4) {
                    sendUseMessage(sender)
                    return
                }

                val value: String = args.drop(3).joinToString(" ")

                @Suppress("NAME_SHADOWING")
                val prefix = "${prefix}Edit."

                coupons.find { it.name == name }?.apply {

                    when (args[2].toLowerCase()) {
                        "displayname" -> "${prefix}DisplayName".success(sender) {

                            displayName = ChatColor.translateAlternateColorCodes('&', value)

                        }
                        "lore" -> "${prefix}Lore".success(sender) {

                            lore += ChatColor.translateAlternateColorCodes('&', value)  //TODO: Add remove

                        }
                        "itemtype" -> "${prefix}ItemType".success(sender) {

                            itemType = args[3].toMaterial() ?: Material.CACTUS

                        }
                        "subid" -> "${prefix}SubID".success(sender) {

                            itemSubID = args[3].toShortOrNull() ?: 0

                        }
                        "command" -> "${prefix}Command".success(sender) {

                            commands += value //TODO: Add remove

                        }
                    }

                } ?: messages["${prefix}Failed.CanNotFindCoupon"]?.replace("<Name>", name, true).sendIfNotNull(sender)

            }
            else -> sendUseMessage(sender)
        }

    }

}
