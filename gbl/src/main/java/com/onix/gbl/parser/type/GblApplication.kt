package parser.data.type

import parser.data.application.ApplicationData
import parser.data.tag.TagHeader
import parser.data.tag.TagInterface

data class GblApplication(
    override val tagHeader: TagHeader,
    override val tagType: GblType,
    val applicationData: ApplicationData,
    override val tagData: ByteArray
) : TagInterface {
    override fun copy(): TagInterface {
        return GblApplication(
            tagHeader = tagHeader,
            tagType = tagType,
            applicationData = applicationData,
            tagData = arrayOf<Byte>().toByteArray()
        )
    }
}
