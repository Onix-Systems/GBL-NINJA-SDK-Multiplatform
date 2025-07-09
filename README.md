# GBL-Ninja

[![Main Site](https://img.shields.io/badge/Website-Gecko%20Bootloader%20Parser%20SDK-blue?logo=web)](https://onix-systems-android-gbl-ninja.dev.onix.team/)
[![Constructor](https://img.shields.io/badge/Tool-GBL%20File%20Parser%20%26%20Builder-green?logo=tools)](https://onix-systems-android-gbl-ninja.dev.onix.team/constructor/)

**GBL-Ninja** is a comprehensive toolkit for working with GBL (Gecko Bootloader) files.

## Project Structure

This repository contains:

- **[kotlin-library/](kotlin-library/)** - Core Kotlin library for parsing and creating GBL files
- **[python-library/](python-library/)** - Python implementation of the GBL library
- **[javascript-library/](javascript-library/)** - JavaScript implementation with web interface
- **[gbl-tool-cli/](gbl-tool-cli/)** - Command-line tool for GBL file manipulation
- **[swift-library/](swift-library/)** - Swift port for Apple platform

## Language Support

Choose your preferred implementation:

### Kotlin Library (Original)
Full-featured implementation with advanced container management and JSON serialization.

**Gradle (Kotlin DSL)**:
```kotlin
dependencies {
    implementation(files("libs/gbl-ninja-kotlin.jar"))
}
```

### Java Library
Java port of the Kotlin library with identical functionality.

**Gradle**:
```gradle
dependencies {
    implementation files('libs/gbl-ninja-java.jar')
}
```

### Python Library
Python port of the Kotlin library with identical functionality.

```bash
pip install gbl-ninja==1.0.0
```

### JavaScript Library
Browser and Node.js compatible implementation with interactive web interface.

**Node.js:**
```bash
npm install gbl-ninja
```

### Swift Library
Swift port with basic support for parsing, creating, and modifying GBL files. SwiftPM-compatible.

```swift
.package(url: "https://github.com/Onix-Systems/GBL-NINJA-SDK-Multiplatform", branch: "main")
```

## Quick Start

### Kotlin Example

```kotlin
import Gbl
import results.ParseResult

// Parse existing GBL file
val parser = Gbl()
val result = parser.parseByteArray(gblFileBytes)

when (result) {
    is ParseResult.Success -> {
        println("Parsed ${result.resultList.size} tags")
    }
    is ParseResult.Fatal -> {
        println("Parse error: ${result.error}")
    }
}

// Create new GBL file
val builder = Gbl.GblBuilder.create()
    .application(type = 32U, version = 0x10000U)
    .prog(flashStartAddress = 0x1000U, data = firmwareData)

val gblBytes = builder.buildToByteArray()
```

### Java Example

```java
import com.gblninja.Gbl;
import com.gblninja.results.ParseResult;

// Parse existing GBL file
Gbl gblParser = new Gbl();
ParseResult result = gblParser.parseByteArray(gblFileBytes);

if (result instanceof ParseResult.Success) {
    System.out.println("Parsed " + ((ParseResult.Success) result).getResultList().size() + " tags");
} else {
    System.out.println("Parse error: " + ((ParseResult.Fatal) result).getError());
}

// Create new GBL file
Gbl.GblBuilder builder = Gbl.GblBuilder.create();
builder.application(32, 0x10000, 0, (byte) 54);
builder.prog(0x1000, firmwareData);

byte[] gblBytes = builder.buildToByteArray();
```

### Python Example

```python
from gbl import Gbl
from results.parse_result import ParseResult

# Parse existing GBL file
gbl_parser = Gbl()
result = gbl_parser.parse_byte_array(gbl_data)

if isinstance(result, ParseResult.Success):
    print(f"Successfully parsed {len(result.result_list)} tags")
else:
    print(f"Parse failed: {result.error}")

# Create new GBL file
builder = Gbl().GblBuilder.create()
builder.application(type_val=32, version=0x10000)
builder.prog(flash_start_address=0x1000, data=firmware_data)

gbl_bytes = builder.build_to_byte_array()
```

### JavaScript Example

```javascript
// Parse existing GBL file
const gbl = new Gbl();
const parseResult = gbl.parseByteArray(gblFileBytes);

if (parseResult.type === 'Success') {
    console.log(`Parsed ${parseResult.resultList.length} tags`);
} else {
    console.error(`Parse error: ${parseResult.error}`);
}

// Create new GBL file
const builder = Gbl.GblBuilder.create()
    .application(32, 0x10000, 0, 54)
    .prog(0x1000, firmwareData);

const gblBytes = builder.buildToByteArray();
```

### Swift Example

```Swift
let gbl = Gbl()
let data: Data = ... // Load GBL file
let result = gbl.parseByteArray(data)

switch result {
case .success(let tags):
    print("Parsed \(tags.count) tags")
case .fatal(let error):
    print("Error: \(error?.localizedDescription ?? "unknown")")
}

let builder = Gbl.GblBuilder.create()
    .application(type: 32, version: 0x10000)
    .prog(flashStartAddress: 0x1000, data: firmwareData)

let newFile = builder.buildToByteArray()
```

## Documentation

- **[Kotlin Library Documentation](kotlin-library/README.md)** - Original Kotlin library
- **[Java Library Documentation](java-library/README.md)** - Java implementation
- **[Python Library Documentation](python-library/README.md)** - Python implementation
- **[JavaScript Library Documentation](javascript-library/README.md)** - JavaScript implementation with web interface
- **[CLI Tool Documentation](gbl-tool-cli/README.md)** - Command-line interface
- **[Swift Library Documentation](swift-library/README.md)** - Swift implementation

## About GBL Format

GBL (Gecko Bootloader) is a binary file format used for firmware updates on Silicon Labs devices. It consists of tagged data blocks that contain:

- Application code and data
- Bootloader updates
- Security certificates and signatures
- Metadata and version information

## Features

- **Parse** existing GBL files into structured data
- **Create** new GBL files from scratch
- **Modify** existing GBL files
- **Validate** file integrity with CRC checks
- **Support** for compression (LZ4, LZMA)
- **Security** features (ECDSA signatures, encryption)
- **Multi-language** support (Kotlin, Python, JavaScript)
- **Web Interface** for interactive GBL file manipulation
- **Cross-platform** compatibility (JVM, Python, Browser, Node.js)

## License

Apache License 2.0
