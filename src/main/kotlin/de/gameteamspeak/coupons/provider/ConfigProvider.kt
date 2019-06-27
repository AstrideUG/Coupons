package de.gameteamspeak.coupons.provider

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import de.gameteamspeak.coupons.data.Coupon
import net.darkdevelopers.darkbedrock.darkness.general.functions.load
import net.darkdevelopers.darkbedrock.darkness.general.functions.save
import net.darkdevelopers.darkbedrock.darkness.general.functions.toConfigData
import net.darkdevelopers.darkbedrock.darkness.general.functions.toNonNull
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.toMaterial
import net.darkdevelopers.darkbedrock.darkness.spigot.messages.SpigotGsonMessages
import org.bukkit.Bukkit
import org.bukkit.Material
import java.io.File

class ConfigProvider(private val directory: File) {

	val data by lazy { Data() }
	val messages by lazy { Messages() }

	inner class Messages internal constructor() {

		private val configData = "messages".toConfigData(directory)
		private val spigotGsonMessages = SpigotGsonMessages(configData)
		val messages = spigotGsonMessages.availableMessages

	}

	inner class Data internal constructor() {

		/* Main */
		private val configData = "data".toConfigData(directory)
		private val jsonObject = configData.load<JsonObject>()

		fun load(): Set<Coupon> {

			val coupons = jsonObject["coupons"]?.asJsonArray ?: return emptySet()
			return coupons.mapNotNull { it as? JsonObject }.mapNotNull { element ->
				val name = element.get("name")?.asString ?: return@mapNotNull null
				val displayName = element.get("display-name")?.asString ?: "DisplayName"
				val lore = (element.get("lore") as? JsonArray)?.map { it.asString } ?: emptyList()
				val type = element.get("type")?.asString?.toMaterial() ?: Material.STONE
				val subId = element.get("sub-id")?.asShort ?: 0
				val command = (element.get("commands") as? JsonArray)?.map { it.asString } ?: emptyList()

				return@mapNotNull Coupon(name, displayName, lore, type, subId, command)
			}.toSet()

		}

		fun save(coupons: Set<Coupon>) {

			val entries = JsonArray().apply {
				add(JsonObject().apply {
					coupons.forEach { coupon ->
						addProperty("name", coupon.name)
						addProperty("display-name", coupon.displayName)
						add("lore", JsonArray().apply { coupon.lore.forEach { add(JsonPrimitive(it)) } })
						addProperty("type", coupon.itemType.name)
						addProperty("sub-id", coupon.itemSubID)
						add("commands", JsonArray().apply { coupon.commands.forEach { add(JsonPrimitive(it)) } })
					}
				})
			}

			val jsonObject = JsonObject().apply { add("coupons", entries) }
			configData.save(jsonObject)

		}

	}

	companion object {
		val instance: ConfigProvider
			get() = Bukkit.getServicesManager()?.getRegistration(ConfigProvider::class.java)?.provider.toNonNull("ConfigProvider")
		val messages get() = instance.messages.messages
	}

}