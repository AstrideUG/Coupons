package de.gameteamspeak.coupons.data

import org.bukkit.Material

data class Coupon(
        val name: String,
        val displayName: String = "DisplayName",
        val lore: List<String> = emptyList(),
        val itemType: Material = Material.STONE,
        val itemSubID: Short = 0,
        val command: List<String> = emptyList()
)