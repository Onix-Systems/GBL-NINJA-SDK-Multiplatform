package parser.data.encode

import parser.data.TAG_ID_SIZE
import parser.data.TAG_LENGTH_SIZE
import parser.data.application.ApplicationData
import parser.data.tag.TagInterface
import parser.data.type.*
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.zip.CRC32

/**
 * Кодує список тегів у ByteArray відповідно до формату GBL.
 *
 * @param tags Список тегів для кодування
 * @return ByteArray, що містить серіалізовані дані GBL
 */
fun encodeTags(tags: List<TagInterface>): ByteArray {
    val totalSize = calculateTotalSize(tags)
    val buffer = ByteBuffer.allocate(totalSize)
    buffer.order(ByteOrder.LITTLE_ENDIAN)

    // Обробляємо всі теги послідовно
    for (tag in tags) {
        // Записуємо ID тега (4 байти)
        buffer.putInt(tag.tagHeader.id.toInt())

        // Записуємо довжину payload тега (4 байти)
        buffer.putInt(tag.tagHeader.length.toInt())

        // Генеруємо та записуємо актуальні дані тега на основі його типу
        val tagData = generateTagData(tag)
        buffer.put(tagData)
    }

    return buffer.array()
}

/**
 * Генерує актуальні дані тега залежно від його типу.
 *
 * @param tag Тег для генерації даних
 * @return ByteArray з актуальними даними тега
 */
private fun generateTagData(tag: TagInterface): ByteArray {
    return when (tag) {
        is GblHeader -> {
            val buffer = ByteBuffer.allocate(8)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putInt(tag.version.toInt())
                .putInt(tag.gblType.toInt())
            buffer.array()
        }
        is GblBootloader -> {
            val buffer = ByteBuffer.allocate(tag.tagHeader.length.toInt())
                .order(ByteOrder.LITTLE_ENDIAN)
                .putInt(tag.bootloaderVersion.toInt())
                .putInt(tag.address.toInt())

            // Додаємо дані
            buffer.put(tag.data)
            buffer.array()
        }
        is GblApplication -> {
            val appData = tag.applicationData
            val buffer = ByteBuffer.allocate(tag.tagHeader.length.toInt())
                .order(ByteOrder.LITTLE_ENDIAN)
                .putInt(appData.type.toInt())
                .putInt(appData.version.toInt())
                .putInt(appData.capabilities.toInt())
                .put(appData.productId.toByte())

            // Додаємо залишок даних, якщо вони є
            if (tag.tagHeader.length > 13u) {
                val remainingData = tag.tagData.copyOfRange(13, tag.tagData.size)
                buffer.put(remainingData)
            }
            buffer.array()
        }
        is GblProg -> {
            val buffer = ByteBuffer.allocate(tag.tagHeader.length.toInt())
                .order(ByteOrder.LITTLE_ENDIAN)
                .putInt(tag.flashStartAddress.toInt())

            // Додаємо дані
            buffer.put(tag.data)
            buffer.array()
        }
        is GblSeUpgrade -> {
            val buffer = ByteBuffer.allocate(tag.tagHeader.length.toInt())
                .order(ByteOrder.LITTLE_ENDIAN)
                .putInt(tag.blobSize.toInt())
                .putInt(tag.version.toInt())

            // Додаємо дані
            buffer.put(tag.data)
            buffer.array()
        }
        is GblEnd -> {
            val buffer = ByteBuffer.allocate(4)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putInt(tag.gblCrc.toInt())
            buffer.array()
        }
        is GblMetadata -> {
            tag.metaData
        }
        is GblProgLz4 -> {
            tag.tagData
        }
        is GblProgLzma -> {
            tag.tagData
        }
        is GblEraseProg -> {
            tag.tagData
        }
        else -> {
            tag.tagData
        }
    }
}

/**
 * Обчислює загальний розмір усіх тегів у байтах.
 *
 * @param tags Список тегів
 * @return Загальний розмір у байтах
 */
private fun calculateTotalSize(tags: List<TagInterface>): Int {
    return tags.sumOf { tag ->
        TAG_ID_SIZE + TAG_LENGTH_SIZE + tag.tagHeader.length.toInt()
    }
}

/**
 * Альтернативна версія функції encodeTags з обчисленням CRC.
 * Генерує CRC для кожного тега, якщо це необхідно за форматом GBL.
 *
 * @param tags Список тегів для кодування
 * @param includeCrc Чи включати CRC в кінці кожного тега
 * @return ByteArray, що містить серіалізовані дані GBL з CRC
 */
fun encodeTagsWithCrc(tags: List<TagInterface>, includeCrc: Boolean = false): ByteArray {
    val crcSize = if (includeCrc) 4 else 0

    // Обчислюємо загальний розмір з урахуванням CRC
    val totalSize = tags.sumOf { tag ->
        TAG_ID_SIZE + TAG_LENGTH_SIZE + tag.tagHeader.length.toInt() + crcSize
    }

    val buffer = ByteBuffer.allocate(totalSize)
    buffer.order(ByteOrder.LITTLE_ENDIAN)

    // Обчислюємо загальний CRC для всього файлу
    val fileCrc = CRC32()

    for (tag in tags) {
        val tagIdBytes = ByteBuffer.allocate(4)
            .order(ByteOrder.LITTLE_ENDIAN)
            .putInt(tag.tagHeader.id.toInt())
            .array()

        val tagLengthBytes = ByteBuffer.allocate(4)
            .order(ByteOrder.LITTLE_ENDIAN)
            .putInt(tag.tagHeader.length.toInt())
            .array()

        // Генеруємо актуальні дані тега
        val tagData = generateTagData(tag)

        // Обчислюємо CRC для даного тега
        if (includeCrc) {
            val crc = CRC32()
            crc.update(tagIdBytes)
            crc.update(tagLengthBytes)
            crc.update(tagData)

            // Оновлюємо загальний CRC
            fileCrc.update(tagIdBytes)
            fileCrc.update(tagLengthBytes)
            fileCrc.update(tagData)

            // Записуємо дані тега в буфер
            buffer.put(tagIdBytes)
            buffer.put(tagLengthBytes)
            buffer.put(tagData)

            // Додаємо CRC для тега
            buffer.putInt(crc.value.toInt())
        } else {
            // Якщо CRC не потрібен, просто додаємо дані до буфера
            buffer.put(tagIdBytes)
            buffer.put(tagLengthBytes)
            buffer.put(tagData)

            // Оновлюємо загальний CRC
            fileCrc.update(tagIdBytes)
            fileCrc.update(tagLengthBytes)
            fileCrc.update(tagData)
        }
    }

    // Якщо серед тегів є GblEnd, то замінюємо його CRC на обчислений для всього файлу
    // Це актуально якщо потрібно оновити значення CRC в тезі END
    if (!includeCrc) {
        val endTagIndex = tags.indexOfFirst { it is GblEnd }
        if (endTagIndex != -1) {
            val endTag = tags[endTagIndex] as GblEnd
            val endTagPosition = tags.subList(0, endTagIndex).sumOf {
                TAG_ID_SIZE + TAG_LENGTH_SIZE + it.tagHeader.length.toInt()
            }

            // Позиція, куди записати CRC в буфері
            val crcPosition = endTagPosition + TAG_ID_SIZE + TAG_LENGTH_SIZE

            // Записуємо обчислений CRC в буфер на позицію тега END
            buffer.position(crcPosition)
            buffer.putInt(fileCrc.value.toInt())
        }
    }

    return buffer.array()
}

/**
 * Створює новий об'єкт GblEnd з коректним CRC для заданого списку тегів.
 *
 * @param tags Список тегів (без END тега)
 * @return Новий об'єкт GblEnd з коректним CRC
 */
fun createEndTagWithCrc(tags: List<TagInterface>): GblEnd {
    val crc = CRC32()

    for (tag in tags) {
        val tagIdBytes = ByteBuffer.allocate(4)
            .order(ByteOrder.LITTLE_ENDIAN)
            .putInt(tag.tagHeader.id.toInt())
            .array()

        val tagLengthBytes = ByteBuffer.allocate(4)
            .order(ByteOrder.LITTLE_ENDIAN)
            .putInt(tag.tagHeader.length.toInt())
            .array()

        // Використовуємо актуальні дані тега
        val tagData = generateTagData(tag)

        crc.update(tagIdBytes)
        crc.update(tagLengthBytes)
        crc.update(tagData)
    }

    // Додаємо ID і довжину END тега до CRC
    val endTagId = GblType.END.value.toInt()
    val endTagLength = 4 // Довжина CRC

    val endTagIdBytes = ByteBuffer.allocate(4)
        .order(ByteOrder.LITTLE_ENDIAN)
        .putInt(endTagId)
        .array()

    val endTagLengthBytes = ByteBuffer.allocate(4)
        .order(ByteOrder.LITTLE_ENDIAN)
        .putInt(endTagLength)
        .array()

    crc.update(endTagIdBytes)
    crc.update(endTagLengthBytes)

    // Створюємо байтовий масив для CRC
    val crcValue = crc.value.toInt()
    val crcBytes = ByteBuffer.allocate(4)
        .order(ByteOrder.LITTLE_ENDIAN)
        .putInt(crcValue)
        .array()

    // Створюємо новий END тег
    return GblEnd(
        tagHeader = parser.data.tag.TagHeader(
            id = GblType.END.value.toUInt(),
            length = 4u
        ),
        tagType = GblType.END,
        gblCrc = crcValue.toUInt(),
        tagData = crcBytes
    )
}

/**
 * Кодує список тегів в ByteArray та додає END тег з правильним CRC.
 *
 * @param tags Список тегів (без END тега)
 * @return ByteArray із закодованими тегами та END тегом
 */
fun encodeTagsWithEndTag(tags: List<TagInterface>): ByteArray {
    // Фільтруємо існуючі END теги, щоб додати свій з правильним CRC
    val tagsWithoutEnd = tags.filter { it !is GblEnd }

    // Створюємо END тег з правильним CRC
    val endTag = createEndTagWithCrc(tagsWithoutEnd)

    // Додаємо END тег до списку
    val finalTags = tagsWithoutEnd + endTag

    // Кодуємо всі теги
    return encodeTags(finalTags)
}