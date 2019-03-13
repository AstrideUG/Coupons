package de.gameteamspeak.coupons.data

import org.bukkit.Material

data class Coupon(
        val name: String,
        val displayName: String,
        val lore: List<String>,
        val itemType: Material,
        val itemSubID: Short,
        val command: String
)