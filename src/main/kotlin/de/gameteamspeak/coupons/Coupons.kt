package de.gameteamspeak.coupons

import de.gameteamspeak.coupons.data.Coupon
import de.gameteamspeak.coupons.provider.ConfigProvider
import net.darkdevelopers.darkbedrock.darkness.spigot.plugin.DarkPlugin

class Coupons : DarkPlugin() {

    private val coupons = mutableSetOf<Coupon>()
    private val configProvider = ConfigProvider(dataFolder)

    override fun onEnable() = onEnable {
        // Plugin startup logic
        //GUI create, give, delete, edit
        //Coupons create <glkhklmhklgöhm>
        //Coupons give <ID>
        //Coupons delete <ID>
        //Coupons edit <ID> <rgbnfjgh>

        coupons.addAll(configProvider.data.load())

    }

    override fun onDisable() = onDisable {

        configProvider.data.save(coupons)

    }

}
