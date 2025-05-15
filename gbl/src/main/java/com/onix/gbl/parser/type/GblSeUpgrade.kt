package parser.data.type

import parser.data.tag.TagHeader
import parser.data.tag.TagInterface

data class GblSeUpgrade(
    override val tagHeader: TagHeader,
    override val tagType: GblType,
    val blobSize: UInt,
    val version: UInt,
    val data: ByteArray,
    override val tagData: ByteArray
): TagInterface {
    override fun copy(): TagInterface {
        return GblSeUpgrade(
            tagHeader = tagHeader,
            tagType = tagType,
            blobSize = blobSize,
            version = version,
            data = data,
            tagData = arrayOf<Byte>().toByteArray()
        )
    }
}
