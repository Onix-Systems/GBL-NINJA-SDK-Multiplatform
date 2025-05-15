package parser.data.tag

import parser.data.type.GblType

data class Tag(
    override val tagData: ByteArray,
    override val tagHeader: TagHeader,
    override val tagType: GblType
): TagInterface {
    override fun copy(): Tag {
        return Tag(
            tagData = arrayOf<Byte>().toByteArray(),
            tagHeader = tagHeader,
            tagType = tagType
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Tag

        if (!tagData.contentEquals(other.tagData)) return false
        if (tagHeader != other.tagHeader) return false
        if (tagType != other.tagType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = tagData.contentHashCode()
        result = 31 * result + tagHeader.hashCode()
        result = 31 * result + tagType.hashCode()
        return result
    }
}
