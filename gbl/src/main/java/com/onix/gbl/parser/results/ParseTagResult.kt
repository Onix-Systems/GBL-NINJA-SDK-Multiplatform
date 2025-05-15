package parser.data.results

import parser.data.tag.TagInterface

sealed class ParseTagResult {
    data class Success(
        val tag: TagInterface
    ) : ParseTagResult()

    object Fatal : ParseTagResult()
}