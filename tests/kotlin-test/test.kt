import java.io.File
import java.nio.file.Paths

fun testGblParsing() {
    println("=== Kotlin GBL Test ===")

    try {
        val currentDir = System.getProperty("user.dir")
        val filePath = Paths.get(currentDir, "tests", "test.gbl").toString()
        val file = File(filePath)

        if (!file.exists()) {
            println("✗ Error: test.gbl file not found at $filePath")
            return
        }

        val fileData = file.readBytes()
        println("Loaded test.gbl: ${fileData.size} bytes")

        val gbl = Gbl()
        val result = gbl.parseByteArray(fileData)

        when (result) {
            is ParseResult.Success -> {
                println("✓ Successfully parsed ${result.resultList.size} tags:")
                result.resultList.forEachIndexed { index, tag ->
                    println("  ${index + 1}. ${tag.tagType.name}")
                }
            }
            is ParseResult.Fatal -> {
                println("✗ Parse failed: ${result.error}")
            }
        }

    } catch (e: Exception) {
        println("✗ Error: ${e.message}")
        println("Make sure kotlin-library is compiled and in classpath")
    }
}

fun main() {
    testGblParsing()
}