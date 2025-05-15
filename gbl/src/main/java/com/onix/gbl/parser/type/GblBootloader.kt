package parser.data.type

import parser.data.tag.TagHeader
import parser.data.tag.TagInterface

data class GblBootloader(
    override val tagHeader: TagHeader,
    override val tagType: GblType,
    val bootloaderVersion: UInt,
    val address: UInt,
    val data: ByteArray,
    override val tagData: ByteArray
): TagInterface {
    override fun copy(): TagInterface {
        return GblBootloader(
            tagHeader = tagHeader,
            tagType = tagType,
            bootloaderVersion = bootloaderVersion,
            address = address,
            data = data,
            tagData = arrayOf<Byte>().toByteArray()
        )
    }
}