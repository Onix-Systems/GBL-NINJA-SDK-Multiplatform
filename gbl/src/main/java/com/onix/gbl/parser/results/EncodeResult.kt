package parser.data.results

sealed class EncodeResult {
    data class Success(
        val byteArray: ByteArray
    ) : ParseResult()

    object Fatal : ParseResult()
}