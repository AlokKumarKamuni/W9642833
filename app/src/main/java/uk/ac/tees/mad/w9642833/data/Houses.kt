package uk.ac.tees.mad.w9642833.data

data class Houses(
    val properties: List<Properties> = emptyList()
)

data class Properties(
    var id:String? = null,
    var bedrooms: Int = 0,
    var bathrooms: Int = 0,
    var numberOfImages: Int = 0,
    var summary: String? = null,
    var displayAddress: String? = null,
    var location: Location? = null,
    var propertyImages: PropertyImages? = null,
    var price:Price? = null,
    var contactTelephone:String? = null
)

data class Location(
    var latitude: Double? = 0.0,
    var longitude: Double? = 0.0
)

data class PropertyImages(
    var images: List<Images>? = emptyList(),
    var mainImageSrc: String? = null
)

data class Images(
    var srcUrl: String? = null
)

data class Price(
    var displayPrices : List<DisplayPrices>? = emptyList()
)


data class DisplayPrices(
    var displayPrice : String? = null
)

object HouseObject {
    var house:Houses? = Houses()
}