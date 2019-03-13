package de.gameteamspeak.coupons.provider

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import de.gameteamspeak.coupons.data.Coupon
import net.darkdevelopers.darkbedrock.darkness.general.configs.ConfigData
import net.darkdevelopers.darkbedrock.darkness.general.configs.gson.GsonService
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.toMaterial
import java.io.File

class ConfigProvider(private val directory: File) {

    val data by lazy { Data() }

    inner class Data internal constructor() {

        /* Main */
        private val configData = ConfigData(directory, "data.json")
        private val jsonObject = GsonService.load(configData) as? JsonObject ?: JsonObject()

        fun load(): Set<Coupon> {

            val coupons = jsonObject["coupons"]?.asJsonArray ?: return emptySet()
            return coupons.mapNotNull { it as? JsonObject }.mapNotNull { element ->
                val name = element.get("name")?.asString ?: return@mapNotNull null
                val displayName = element.get("display-name")?.asString ?: return@mapNotNull null
                val lore = (element.get("lore") as? JsonArray)?.map { it.asString } ?: listOf()
                val type = element.get("type")?.asString?.toMaterial() ?: return@mapNotNull null
                val subId = element.get("sub-id")?.asShort ?: 0
                val command = element.get("command")?.asString ?: return@mapNotNull null

                return@mapNotNull Coupon(name, displayName, lore, type, subId, command)
            }.toSet()

        }

        fun save(coupons: Set<Coupon>) {

            val entries = JsonArray().apply {
                add(JsonObject().apply {
                    coupons.forEach { coupon ->
                        addProperty("name", coupon.name)
                        addProperty("display-name", coupon.displayName)
                        add("lore", JsonArray().apply { coupon.lore.forEach { add(it) } })
                        addProperty("type", coupon.itemType.name)
                        addProperty("sub-id", coupon.itemSubID)
                        addProperty("command", coupon.command)
                    }
                })
            }

            val jsonObject = JsonObject().apply { add("coupons", entries) }
            GsonService.save(configData, jsonObject)

        }


    }

}