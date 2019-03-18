package de.gameteamspeak.coupons.data

import org.bukkit.Material

data class Coupon(
        val name: String,
        var displayName: String = "DisplayName",
        var lore: List<String> = emptyList(),
        var itemType: Material = Material.STONE,
        var itemSubID: Short = 0,
        var command: List<String> = emptyList()
)