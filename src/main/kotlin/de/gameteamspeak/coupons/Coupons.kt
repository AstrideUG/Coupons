package de.gameteamspeak.coupons

import de.gameteamspeak.coupons.commands.CouponsCommand
import de.gameteamspeak.coupons.data.Coupon
import de.gameteamspeak.coupons.functions.registerConfigProvider
import de.gameteamspeak.coupons.listeners.CouponListener
import de.gameteamspeak.coupons.provider.ConfigProvider
import net.darkdevelopers.darkbedrock.darkness.spigot.plugin.DarkPlugin

class Coupons : DarkPlugin() {

    val coupons = mutableSetOf<Coupon>()
    private val data get() = ConfigProvider.instance.data

    init {
        instance = this
    }

    override fun onLoad() = onLoad {
        registerConfigProvider()
        data.save(coupons)
    }

    override fun onEnable() = onEnable {

        coupons.addAll(data.load())
        CouponsCommand(this)
        CouponListener(this)

    }

    override fun onDisable() = onDisable {

      data.save(coupons)

    }

    companion object {
        lateinit var instance: Coupons
    }

}
