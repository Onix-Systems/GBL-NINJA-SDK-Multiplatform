package parser.data.type

import parser.data.tag.TagHeader
import parser.data.tag.TagInterface

data class GblEnd(
    override val tagHeader: TagHeader,
    override val tagType: GblType,
    val gblCrc: UInt,
    override val tagData: ByteArray
): TagInterface {
    override fun copy(): TagInterface {
        return GblEnd(
            tagHeader = tagHeader,
            tagType = tagType,
            gblCrc = gblCrc,
            tagData = arrayOf<Byte>().toByteArray()
        )
    }
}