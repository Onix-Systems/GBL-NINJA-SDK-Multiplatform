package parser.data.type

import parser.data.tag.TagHeader
import parser.data.tag.TagInterface

data class GblProg(
    override val tagHeader: TagHeader,
    override val tagType: GblType,
    val flashStartAddress: UInt,
    val data: ByteArray,
    override val tagData: ByteArray
): TagInterface  {
    override fun copy(): TagInterface {
        return GblProg(
            tagHeader = tagHeader,
            tagType = tagType,
            flashStartAddress = flashStartAddress,
            data = data,
            tagData = arrayOf<Byte>().toByteArray()
        )
    }
}
