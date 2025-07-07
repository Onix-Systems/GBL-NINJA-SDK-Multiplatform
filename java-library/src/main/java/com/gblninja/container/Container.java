package com.gblninja.container;


import com.gblninja.tag.Tag;

import java.util.List;

public interface Container {
    ContainerResult<Void> create();
    ContainerResult<Void> add(Tag tag);
    ContainerResult<Void> remove(Tag tag);
    ContainerResult<List<Tag>> build();
    ContainerResult<byte[]> content();
}