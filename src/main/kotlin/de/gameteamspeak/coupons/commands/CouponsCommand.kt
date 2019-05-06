package de.gameteamspeak.coupons.commands

import de.gameteamspeak.coupons.Coupons
import de.gameteamspeak.coupons.data.Coupon
import de.gameteamspeak.coupons.functions.success
import de.gameteamspeak.coupons.provider.ConfigProvider
import de.gameteamspeak.coupons.provider.ConfigProvider.Companion.messages
import de.tr7zw.itemnbtapi.NBTItem
import net.darkdevelopers.darkbedrock.darkness.spigot.builder.item.ItemBuilder
import net.darkdevelopers.darkbedrock.darkness.spigot.commands.Command
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.sendIfNotNull
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.toMaterial
import net.darkdevelopers.darkbedrock.darkness.spigot.utils.isPlayer
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

class CouponsCommand(javaPlugin: JavaPlugin) : Command(
        javaPlugin,
        commandName = "coupons",
        permission = "coupons.commands.coupons",
        usage = "List" +
                "|Reload" +
                "|Save" +
                "|Info <Name>" +
                "|create <Name>" +
                "|delete <Name>" +
                "|give <Name> [Amount]" +
                "|edit <Name> <DisplayName/Lore/ItemType/SubID/Command> <Value>",
        minLength = 1,
        maxLength = Int.MAX_VALUE

) {

    private val coupons get() = Coupons.instance.coupons

    override fun perform(sender: CommandSender, args: Array<String>) {

        val prefix = "Coupons.Commands.Coupons."

        if (args.size == 1) {

            when (args[0].toLowerCase()) {
                "list" -> "${prefix}List".success(sender) {
                    coupons.forEach { Bukkit.dispatchCommand(sender, "$commandName info ${it.name}") }
                }
                "reload" -> "${prefix}Reload".success(sender) {
                    Coupons.instance.coupons = ConfigProvider.instance.data.load().toMutableSet()
                }
                "save" -> "${prefix}Save".success(sender) {
                    ConfigProvider.instance.data.save(coupons)
                }
                else -> sendUseMessage(sender)
            }

            return

        }

        val name = args[1] //edit
        val transform: (String?) -> String? = { it?.replace("<Name>", name, true) }
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

                        messages["${prefix}Success"]?.map(transform)?.sendIfNotNull(player)
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
                        messages["${prefix}Successfully"]?.map(transform)?.sendIfNotNull(player)

                    } else messages["${prefix}Failed.CanNotFindCoupon"]?.map(transform)?.sendIfNotNull(player)

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

                } ?: messages["${prefix}Failed.CanNotFindCoupon"]?.map(transform)?.sendIfNotNull(sender)

            }

            "info" -> {

                @Suppress("NAME_SHADOWING")
                val prefix = "${prefix}Info."

                if (args.size == 2) {


                    val coupon = coupons.find { it.name == name }
                    if (coupon != null) {

                        messages["${prefix}Successfully"]
                                ?.map(transform)
                                ?.map {
                                    it?.replace("<DisplayName>", coupon.displayName, true)
                                            ?.replace("<ItemType>", coupon.itemType.toString(), true)
                                            ?.replace("<ItemSubID>", coupon.itemSubID.toString(), true)
                                            ?.replace("<Lore>", coupon.lore.toString(), true)
                                            ?.replace("<Commands>", coupon.commands.toString(), true)
                                }?.sendIfNotNull(sender)

                    } else messages["${prefix}Failed.CanNotFindCoupon"]?.map(transform)?.sendIfNotNull(sender)

                } else sendUseMessage(sender)

            }

            else -> sendUseMessage(sender)
        }

    }

}
