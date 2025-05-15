package com.onix.gbl.parser.parse

import com.onix.gbl.parser.application.ApplicationData
import parser.data.tag.Tag
import parser.data.tag.TagHeader
import parser.data.tag.TagInterface
import parser.data.type.GblApplication
import parser.data.type.GblBootloader
import parser.data.type.GblEnd
import parser.data.type.GblEraseProg
import parser.data.type.GblHeader
import parser.data.type.GblMetadata
import parser.data.type.GblProg
import parser.data.type.GblProgLz4
import parser.data.type.GblProgLzma
import parser.data.type.GblSeUpgrade
import parser.data.type.GblType
import parser.data.utils.getFromBytes

fun parseTagType(
    tagId: UInt,
    length: UInt,
    byteArray: ByteArray,
): TagInterface {
    val tagType = GblType.fromValue(tagId.toLong())

    val tagHeader = TagHeader(
        id = tagId,
        length = length
    )

    return when (tagType) {
        GblType.HEADER_V3 -> {
            val version = getFromBytes(byteArray, offset = 0, length = 4).int.toUInt()
            val gblType = getFromBytes(byteArray, offset = 4, length = 4).int.toUInt()

            GblHeader(
                tagHeader = tagHeader,
                tagType = tagType,
                version = version,
                gblType = gblType,
                tagData = byteArray,
            )
        }

        GblType.BOOTLOADER -> {
            GblBootloader(
                tagHeader = tagHeader,
                tagType = tagType,
                bootloaderVersion = getFromBytes(byteArray, offset = 0, length = 4).int.toUInt(),
                address = getFromBytes(byteArray, offset = 4, length = 4).int.toUInt(),
                data = byteArray.copyOfRange(8, byteArray.size),
                tagData = byteArray
            )
        }

        GblType.APPLICATION -> {
            val appData = ApplicationData(
                type = getFromBytes(byteArray, offset = 0, length = 4).int.toUInt(),
                version = getFromBytes(byteArray, offset = 4, length = 4).int.toUInt(),
                capabilities = getFromBytes(byteArray, offset = 8, length = 4).int.toUInt(),
                productId = byteArray[12].toUByte(),
            )

            GblApplication(
                tagHeader = tagHeader,
                tagType = tagType,
                applicationData = appData,
                tagData = byteArray,
            )
        }

        GblType.METADATA -> {
            GblMetadata(
                tagHeader = tagHeader,
                tagType = tagType,
                metaData = byteArray,
                tagData = byteArray,
            )
        }

        GblType.PROG -> {
            GblProg(
                tagHeader = tagHeader,
                tagType = tagType,
                flashStartAddress = getFromBytes(byteArray, offset = 0, length = 4).int.toUInt(),
                data = byteArray.copyOfRange(4, byteArray.size),
                tagData = byteArray,
            )
        }

        GblType.PROG_LZ4 -> {
            GblProgLz4(
                tagHeader = tagHeader,
                tagType = tagType,
                tagData = byteArray,
            )
        }

        GblType.PROG_LZMA -> {
            GblProgLzma(
                tagHeader = tagHeader,
                tagType = tagType,
                tagData = byteArray,
            )
        }

        GblType.ERASEPROG -> {
            GblEraseProg(
                tagHeader = tagHeader,
                tagType = tagType,
                tagData = byteArray,
            )
        }

        GblType.SE_UPGRADE -> {
            GblSeUpgrade(
                tagHeader = tagHeader,
                tagType = tagType,
                blobSize = getFromBytes(byteArray, offset = 0, length = 4).int.toUInt(),
                version = getFromBytes(byteArray, offset = 4, length = 4).int.toUInt(),
                data = byteArray.copyOfRange(8, byteArray.size),
                tagData = byteArray,
            )
        }

        GblType.END -> {
            GblEnd(
                tagHeader = tagHeader,
                tagType = tagType,
                gblCrc = getFromBytes(byteArray, offset = 0, length = 4).int.toUInt(),
                tagData = byteArray,
            )
        }

        null -> {
            Tag(
                tagType = GblType.TAG,
                tagHeader = tagHeader,
                tagData = byteArray
            )
        }

        else -> {
            Tag(
                tagType = GblType.TAG,
                tagHeader = tagHeader,
                tagData = byteArray
            )
        }
    }
}