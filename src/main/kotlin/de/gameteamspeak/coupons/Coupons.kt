package de.gameteamspeak.coupons

import de.gameteamspeak.coupons.commands.CouponsCommand
import de.gameteamspeak.coupons.functions.javaPlugin
import de.gameteamspeak.coupons.functions.registerConfigProvider
import de.gameteamspeak.coupons.listeners.CouponListener
import de.gameteamspeak.coupons.provider.ConfigProvider
import net.darkdevelopers.darkbedrock.darkness.spigot.plugin.DarkPlugin

class Coupons : DarkPlugin() {

    init {
        javaPlugin = this
    }

    override fun onLoad() = onLoad {
        registerConfigProvider()
    }

    override fun onEnable() = onEnable {

        coupons.addAll(data.load())
        CouponsCommand(this)
        CouponListener(this)
        data.save(coupons)

    }

    override fun onDisable() = onDisable {
      data.save(coupons)
    }

	companion object{
		val data get() = ConfigProvider.instance.data
	}

}
