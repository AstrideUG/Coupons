package de.gameteamspeak.coupons.listeners

import de.gameteamspeak.coupons.Coupons
import de.tr7zw.itemnbtapi.NBTItem
import net.darkdevelopers.darkbedrock.darkness.spigot.listener.Listener
import net.darkdevelopers.darkbedrock.darkness.spigot.utils.removeItemInHand
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.plugin.java.JavaPlugin


class CouponListener(javaPlugin: JavaPlugin) : Listener(javaPlugin) {


//    @EventHandler
//    fun onInventoryClickEvent(event: InventoryClickEvent) {
//
//        val currentItem = event.currentItem ?: return
//        val nbtItem = NBTItem(currentItem)
////        if(!nbtItem.hasKey("Coupon")) return
//        val name = nbtItem.getString("Coupon") ?: return
//        val coupon = Coupons.instance.coupons.find { it.name == name } ?: return
//        val player = event.whoClicked as? Player ?:return
//        val itemInHand = player.itemInHand ?: return
//        if(itemInHand.amount < 1) player.itemInHand = null else player.itemInHand.amount -= 1
//
//        coupon.commands.forEach {  Bukkit.dispatchCommand(Bukkit.getConsoleSender(), it) }
//
//    }


    @EventHandler
    fun onPlayerInteractEvent(event: PlayerInteractEvent) {

        val currentItem = event.item ?: return
        val nbtItem = NBTItem(currentItem)
        val name = nbtItem.getString("Coupon") ?: return
        val coupon = Coupons.instance.coupons.find { it.name == name } ?: return
        val player = event.player ?: return

        player.removeItemInHand()

        coupon.commands.forEach { Bukkit.dispatchCommand(Bukkit.getConsoleSender(), it) }

    }


}