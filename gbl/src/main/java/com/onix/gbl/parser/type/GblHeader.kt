package parser.data.type

import parser.data.tag.TagHeader
import parser.data.tag.TagInterface

data class GblHeader(
    override val tagHeader: TagHeader,
    override val tagType: GblType = GblType.HEADER_V3,
    val version: UInt,
    val gblType: UInt,
    override val tagData: ByteArray
): TagInterface {
    override fun copy(): TagInterface {
        return GblHeader(
            tagHeader = tagHeader,
            version = version,
            gblType = gblType,
            tagData = arrayOf<Byte>().toByteArray()
        )
    }
}
