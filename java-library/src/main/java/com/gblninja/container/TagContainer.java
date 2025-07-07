package com.gblninja.container;


import com.gblninja.encode.EncodeTags;
import com.gblninja.tag.GblType;
import com.gblninja.tag.Tag;
import com.gblninja.tag.TagHeader;
import com.gblninja.tag.type.GblEnd;
import com.gblninja.tag.type.GblHeader;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TagContainer implements Container {
    private static final long GBL_TAG_ID_HEADER_V3 = 0x03A617EBL;
    private static final int HEADER_SIZE = 8;
    private static final long HEADER_VERSION = 50331648L;
    private static final long HEADER_GBL_TYPE = 0L;
    private static final Set<GblType> PROTECTED_TAG_TYPES = Set.of(GblType.HEADER_V3, GblType.END);

    private final Set<Tag> content = new HashSet<>();
    private boolean isCreated = false;

    @Override
    public ContainerResult<Void> create() {
        try {
            if (isCreated) {
                return new ContainerResult.Success<>(null);
            }

            content.clear();

            Tag headerTag = createHeaderTag();
            content.add(headerTag);

            Tag endTag = createEndTag();
            content.add(endTag);

            isCreated = true;
            return new ContainerResult.Success<>(null);

        } catch (Exception e) {
            return new ContainerResult.Error(
                    "Failed to create container: " + e.getMessage(),
                    ContainerErrorCode.INTERNAL_ERROR
            );
        }
    }

    @Override
    public ContainerResult<Void> add(Tag tag) {
        try {
            if (!isCreated) {
                return new ContainerResult.Error(
                        "Container must be created before adding tags. Call create() first.",
                        ContainerErrorCode.CONTAINER_NOT_CREATED
                );
            }

            if (isProtectedTag(tag)) {
                return new ContainerResult.Error(
                        "Cannot add protected tag: " + tag.getTagType() + ". Protected tags are managed automatically.",
                        ContainerErrorCode.PROTECTED_TAG_VIOLATION
                );
            }

            content.add(tag);
            return new ContainerResult.Success<>(null);

        } catch (Exception e) {
            return new ContainerResult.Error(
                    "Failed to add tag: " + e.getMessage(),
                    ContainerErrorCode.INTERNAL_ERROR
            );
        }
    }

    @Override
    public ContainerResult<Void> remove(Tag tag) {
        try {
            if (!isCreated) {
                return new ContainerResult.Error(
                        "Container must be created before removing tags. Call create() first.",
                        ContainerErrorCode.CONTAINER_NOT_CREATED
                );
            }

            if (isProtectedTag(tag)) {
                return new ContainerResult.Error(
                        "Cannot remove protected tag: " + tag.getTagType() + ". Protected tags are managed automatically.",
                        ContainerErrorCode.PROTECTED_TAG_VIOLATION
                );
            }

            boolean removed = content.remove(tag);
            if (!removed) {
                return new ContainerResult.Error(
                        "Tag not found in container: " + tag.getTagType(),
                        ContainerErrorCode.TAG_NOT_FOUND
                );
            }

            return new ContainerResult.Success<>(null);

        } catch (Exception e) {
            return new ContainerResult.Error(
                    "Failed to remove tag: " + e.getMessage(),
                    ContainerErrorCode.INTERNAL_ERROR
            );
        }
    }

    @Override
    public ContainerResult<List<Tag>> build() {
        try {
            if (!isCreated) {
                return new ContainerResult.Error(
                        "Container must be created before building. Call create() first.",
                        ContainerErrorCode.CONTAINER_NOT_CREATED
                );
            }

            List<Tag> sortedTags = new ArrayList<>();

            content.stream()
                    .filter(tag -> tag.getTagType() == GblType.HEADER_V3)
                    .findFirst()
                    .ifPresent(sortedTags::add);

            List<Tag> nonProtectedTags = content.stream()
                    .filter(tag -> !PROTECTED_TAG_TYPES.contains(tag.getTagType()))
                    .sorted(Comparator.comparingLong(tag -> tag.getTagType().getValue()))
                    .collect(Collectors.toList());
            sortedTags.addAll(nonProtectedTags);

            content.stream()
                    .filter(tag -> tag.getTagType() == GblType.END)
                    .findFirst()
                    .ifPresent(sortedTags::add);

            return new ContainerResult.Success<>(sortedTags);

        } catch (Exception e) {
            return new ContainerResult.Error(
                    "Failed to build container: " + e.getMessage(),
                    ContainerErrorCode.INTERNAL_ERROR
            );
        }
    }

    @Override
    public ContainerResult<byte[]> content() {
        try {
            if (!isCreated) {
                return new ContainerResult.Error(
                        "Container must be created before exporting content. Call create() first.",
                        ContainerErrorCode.CONTAINER_NOT_CREATED
                );
            }

            ContainerResult<List<Tag>> tags = build();
            if (tags instanceof ContainerResult.Success) {
                List<Tag> tagList = ((ContainerResult.Success<List<Tag>>) tags).getData();
                List<Tag> tagsWithoutEnd = tagList.stream()
                        .filter(tag -> !(tag instanceof GblEnd))
                        .collect(Collectors.toList());
                GblEnd endTag = EncodeTags.createEndTagWithCrc(tagsWithoutEnd);
                List<Tag> finalTags = new ArrayList<>(tagsWithoutEnd);
                finalTags.add(endTag);

                byte[] byteArray = EncodeTags.encodeTags(finalTags);
                return new ContainerResult.Success<>(byteArray);
            } else {
                ContainerResult.Error error = (ContainerResult.Error) tags;
                return new ContainerResult.Error(
                        "Failed to build tags for content export: " + error.getMessage(),
                        error.getCode()
                );
            }
        } catch (Exception e) {
            return new ContainerResult.Error(
                    "Failed to export container content: " + e.getMessage(),
                    ContainerErrorCode.INTERNAL_ERROR
            );
        }
    }

    public boolean hasTag(GblType tagType) {
        return isCreated && content.stream().anyMatch(tag -> tag.getTagType() == tagType);
    }

    public Tag getTag(GblType tagType) {
        if (!isCreated) return null;
        return content.stream()
                .filter(tag -> tag.getTagType() == tagType)
                .findFirst()
                .orElse(null);
    }

    public List<Tag> getAllTags(GblType tagType) {
        if (!isCreated) return new ArrayList<>();
        return content.stream()
                .filter(tag -> tag.getTagType() == tagType)
                .collect(Collectors.toList());
    }

    public boolean isEmpty() {
        return !isCreated || content.size() == PROTECTED_TAG_TYPES.size();
    }

    public int size() {
        return isCreated ? content.size() : 0;
    }

    public Set<GblType> getTagTypes() {
        if (!isCreated) return new HashSet<>();
        return content.stream()
                .map(Tag::getTagType)
                .collect(Collectors.toSet());
    }

    public boolean isCreated() {
        return isCreated;
    }

    public ContainerResult<Void> clear() {
        try {
            if (!isCreated) {
                return new ContainerResult.Error(
                        "Container must be created before clearing. Call create() first.",
                        ContainerErrorCode.CONTAINER_NOT_CREATED
                );
            }

            content.removeIf(tag -> !isProtectedTag(tag));
            return new ContainerResult.Success<>(null);

        } catch (Exception e) {
            return new ContainerResult.Error(
                    "Failed to clear container: " + e.getMessage(),
                    ContainerErrorCode.INTERNAL_ERROR
            );
        }
    }

    private boolean isProtectedTag(Tag tag) {
        return PROTECTED_TAG_TYPES.contains(tag.getTagType());
    }

    private Tag createHeaderTag() {
        GblHeader header = new GblHeader(
                new TagHeader(
                        GBL_TAG_ID_HEADER_V3,
                        HEADER_SIZE
                ),
                GblType.HEADER_V3,
                HEADER_VERSION,
                HEADER_GBL_TYPE,
                new byte[0]
        );

        return new GblHeader(
                header.getTagHeader(),
                header.getTagType(),
                header.getVersion(),
                header.getGblType(),
                header.content()
        );
    }

    private Tag createEndTag() {
        return new GblEnd(
                new TagHeader(
                        GblType.END.getValue(),
                        0L
                ),
                GblType.END,
                0L,
                new byte[0]
        );
    }
}
