package parser.data.type

import parser.data.tag.TagHeader
import parser.data.tag.TagInterface

data class GblProgLzma(
    override val tagHeader: TagHeader,
    override val tagType: GblType,
    override val tagData: ByteArray
): TagInterface {
    override fun copy(): TagInterface {
        return GblProgLzma(
            tagHeader = tagHeader,
            tagType = tagType,
            tagData = arrayOf<Byte>().toByteArray()
        )
    }
}
