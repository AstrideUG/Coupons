package de.gameteamspeak.coupons

import de.gameteamspeak.coupons.commands.CouponsCommand
import de.gameteamspeak.coupons.data.Coupon
import de.gameteamspeak.coupons.provider.ConfigProvider
import net.darkdevelopers.darkbedrock.darkness.spigot.plugin.DarkPlugin
import org.bukkit.Bukkit
import org.bukkit.plugin.ServicePriority

class Coupons : DarkPlugin() {

    val coupons = mutableSetOf<Coupon>()
    private val data get() = ConfigProvider.instance.data

    init {
        instance = this
    }

    override fun onLoad() = onLoad {
        Bukkit.getServicesManager().register(
                ConfigProvider::class.java,
                ConfigProvider(dataFolder),
                this,
                ServicePriority.Normal
        ) //Important for ConfigService.instance
        coupons.addAll(data.load())
    }

    override fun onEnable() = onEnable {

        CouponsCommand(this)

    }

    override fun onDisable() = onDisable {

      data.save(coupons)

    }

    companion object {
        lateinit var instance: Coupons
    }

}
