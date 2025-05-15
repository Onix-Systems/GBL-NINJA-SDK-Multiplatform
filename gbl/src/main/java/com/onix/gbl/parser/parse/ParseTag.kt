package parser.data.parse

import parser.data.TAG_ID_SIZE
import parser.data.TAG_LENGTH_SIZE
import parser.data.results.ParseTagResult
import parser.data.tag.Tag
import parser.data.tag.TagHeader
import parser.data.type.GblType
import parser.data.utils.getFromBytes

fun parseTag(
    byteArray: ByteArray,
    offset: Int = 0,
): ParseTagResult {
    if (offset < 0 || offset + TAG_ID_SIZE + TAG_LENGTH_SIZE > byteArray.size) {
        return ParseTagResult.Fatal
    }

    val tagId = getFromBytes(byteArray, offset = offset, length = TAG_ID_SIZE).int

    val tagLength = getFromBytes(byteArray, offset = offset + TAG_ID_SIZE, length = TAG_LENGTH_SIZE).int

    if (offset + TAG_ID_SIZE + TAG_LENGTH_SIZE + tagLength > byteArray.size) {
        return ParseTagResult.Fatal
    }

    val tagData = byteArray.copyOfRange(
        offset + TAG_ID_SIZE + TAG_LENGTH_SIZE,
        offset + TAG_ID_SIZE + TAG_LENGTH_SIZE + tagLength
    )

    val tagHeader = TagHeader(
        id = tagId.toUInt(),
        length = tagLength.toUInt()
    )

    return ParseTagResult.Success(
        tag = Tag(
            tagHeader = tagHeader,
            tagType = GblType.TAG,
            tagData = tagData,
        )
    )
}
