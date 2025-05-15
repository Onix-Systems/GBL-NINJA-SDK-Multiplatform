package parser.data.type

import parser.data.tag.TagHeader
import parser.data.tag.TagInterface

data class GblMetadata(
    override val tagHeader: TagHeader,
    override val tagType: GblType,
    val metaData: ByteArray,
    override val tagData: ByteArray
): TagInterface {
    override fun copy(): TagInterface {
        return GblMetadata(
            tagHeader = tagHeader,
            tagType = tagType,
            metaData = metaData,
            tagData = arrayOf<Byte>().toByteArray()
        )
    }
}
