package parser.data.results

import parser.data.tag.TagInterface

sealed class ParseResult {
    data class Success(
        val resultList: List<TagInterface>
    ) : ParseResult()

    object Fatal : ParseResult()
}


