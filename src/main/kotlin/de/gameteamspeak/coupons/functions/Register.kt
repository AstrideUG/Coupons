package de.gameteamspeak.coupons.functions

import de.gameteamspeak.coupons.Coupons
import de.gameteamspeak.coupons.provider.ConfigProvider
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.ServicePriority
import org.bukkit.plugin.ServicesManager
import java.io.File

fun registerConfigProvider(
        plugin: Plugin = Coupons.instance,
        servicesManager: ServicesManager = plugin.server.servicesManager,
        dataFolder: File = plugin.dataFolder
): Unit = servicesManager.register(ConfigProvider::class.java, ConfigProvider(dataFolder), plugin, ServicePriority.Normal) //Important for ConfigProvider.instance

fun unregisterConfigProvider(
        plugin: Plugin = Coupons.instance,
        servicesManager: ServicesManager = plugin.server.servicesManager
): Unit = servicesManager.unregister(ConfigProvider::class.java, ConfigProvider.instance)

fun reloadConfigProvider(
        plugin: Plugin = Coupons.instance,
        servicesManager: ServicesManager = plugin.server.servicesManager,
        dataFolder: File = plugin.dataFolder
) {
    unregisterConfigProvider(plugin, servicesManager)
    registerConfigProvider(plugin, servicesManager, dataFolder)
}



