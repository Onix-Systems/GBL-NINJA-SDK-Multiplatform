# GBL Java Library

This is a Java library for parsing and creating GBL files.

## Features

*   **Parse GBL files:** Read and parse GBL files into a list of tag objects.
*   **Create GBL files:** Programmatically create GBL files using a builder pattern.
*   **Encode GBL files:** Encode a list of tag objects into a byte array.

## Usage

### Parsing a GBL File

```java
import com.gblninja.Gbl;
import com.gblninja.results.ParseResult;
import com.gblninja.tag.Tag;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class GblParserExample {
    public static void main(String[] args) throws IOException {
        byte[] fileBytes = Files.readAllBytes(Paths.get("firmware.gbl"));
        Gbl gbl = new Gbl();
        ParseResult result = gbl.parseByteArray(fileBytes);

        if (result instanceof ParseResult.Success) {
            List<Tag> tags = ((ParseResult.Success) result).getResultList();
            for (Tag tag : tags) {
                System.out.println("Found tag: " + tag.getTagType());
            }
        } else if (result instanceof ParseResult.Fatal) {
            System.err.println("Failed to parse GBL file: " + ((ParseResult.Fatal) result).getError());
        }
    }
}
```

### Creating a GBL File

```java
import com.gblninja.Gbl;
import com.gblninja.tag.Tag;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class GblCreatorExample {
    public static void main(String[] args) throws IOException {
        Gbl.GblBuilder builder = Gbl.GblBuilder.create();
        builder.application();
        builder.prog(0x1000, new byte[]{0x01, 0x02, 0x03});

        byte[] gblBytes = builder.buildToByteArray();
        Files.write(Paths.get("new_firmware.gbl"), gblBytes);
    }
}
```