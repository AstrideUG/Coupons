package de.gameteamspeak.coupons.functions

import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin

/*
 * Created on 27.06.2019 21:32.
 * @author Lars Artmann | LartyHD
 */

lateinit var javaPlugin: JavaPlugin

fun String.translateColors(altColorChar: Char = '&'): String = ChatColor.translateAlternateColorCodes(altColorChar, this)
