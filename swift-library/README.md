# GBL Swift Library

This is a Swift library for parsing and creating GBL files.

## Features

*   **Parse GBL files:** Read and parse GBL files into an array of tag objects.
*   **Create GBL files:** Programmatically create GBL files using a builder pattern.
*   **Encode GBL files:** Encode an array of tag objects into a `Data` object.

## Usage

### Parsing a GBL File

```swift
import Foundation
import Gbl

let gbl = Gbl()
let fileURL = URL(fileURLWithPath: "firmware.gbl")

do {
    let data = try Data(contentsOf: fileURL)
    let result = gbl.parseByteArray(data)

    switch result {
    case .success(let tags):
        for tag in tags {
            print("Found tag: \(tag.tagType)")
        }
    case .fatal(let error):
        print("Error parsing GBL file: \(error?.localizedDescription ?? "Unknown error")")
    }
} catch {
    print("Error reading file: \(error.localizedDescription)")
}
```

### Creating a GBL File

```swift
import Foundation
import Gbl

let builder = Gbl.GblBuilder.create()
builder.application()
builder.prog(flashStartAddress: 0x1000, data: Data([0x01, 0x02, 0x03]))

let gblData = builder.buildToByteArray()
let fileURL = URL(fileURLWithPath: "new_firmware.gbl")

do {
    try gblData.write(to: fileURL)
} catch {
    print("Error writing file: \(error.localizedDescription)")
}
```