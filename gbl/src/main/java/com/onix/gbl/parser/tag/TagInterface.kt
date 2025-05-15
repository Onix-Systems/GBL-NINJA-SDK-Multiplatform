package parser.data.tag

import parser.data.type.GblType

interface TagInterface {
    val tagHeader: TagHeader
    val tagType: GblType
    @OptIn(ExperimentalUnsignedTypes::class)
    val tagData: ByteArray

    fun copy(): TagInterface
}