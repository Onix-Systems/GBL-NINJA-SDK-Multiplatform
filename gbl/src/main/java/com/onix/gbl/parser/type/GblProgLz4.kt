package parser.data.type

import parser.data.tag.TagHeader
import parser.data.tag.TagInterface

data class GblProgLz4(
    override val tagHeader: TagHeader,
    override val tagType: GblType,
    override val tagData: ByteArray
): TagInterface {
    override fun copy(): TagInterface {
        return GblProgLz4(
            tagHeader = tagHeader,
            tagType = tagType,
            tagData = arrayOf<Byte>().toByteArray()
        )
    }
}
