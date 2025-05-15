package parser

import parser.data.GBL_TAG_ID_HEADER_V3
import parser.data.HEADER_SIZE
import parser.data.results.ParseResult
import parser.data.results.ParseTagResult
import parser.data.TAG_ID_SIZE
import parser.data.TAG_LENGTH_SIZE
import parser.data.parse.parseTag
import com.onix.gbl.parser.parse.parseTagType
import parser.data.tag.TagInterface
import parser.data.utils.getFromBytes

class GblParser {
    fun parseFile(byteArray: ByteArray): ParseResult {
        var offset = 0
        val size = byteArray.size
        val resultList: MutableList<TagInterface> = mutableListOf()

        if(byteArray.size < HEADER_SIZE) {
            return ParseResult.Fatal
        }

        if(getFromBytes(byteArray, offset = 0, length = TAG_ID_SIZE).int != GBL_TAG_ID_HEADER_V3) {
            return ParseResult.Fatal
        }

        while (offset < size) {
            when (val result = parseTag(byteArray, offset)) {
                is ParseTagResult.Fatal -> {
                    break
                }

                is ParseTagResult.Success -> {
                    val tag = result.tag

                    try {
                        val parsedTag = parseTagType(
                            tagId = tag.tagHeader.id,
                            length = tag.tagHeader.length,
                            byteArray = tag.tagData
                        )

                        resultList.add(parsedTag)

                        offset += TAG_ID_SIZE + TAG_LENGTH_SIZE + tag.tagHeader.length.toInt()
                    } catch (e: Exception) {
                        break
                    }
                }
            }
        }

        resultList.forEach {
            println(it)
        }

        return ParseResult.Success(resultList)
    }

    fun encode(tags: List<TagInterface>) {

    }

}