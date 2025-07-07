# GBL-Ninja JavaScript Library

JavaScript implementation of the GBL (Gecko Bootloader) file parser and builder.

## Installation

**CDN:**
```html
<!-- jsDelivr CDN -->
<script src="https://cdn.jsdelivr.net/gh/VladimirOnix/gbl-ninja@main/javascript-library/gbl-library.js"></script>
```

**Local:**
```html
<script src="gbl-library.js"></script>
```

**Node.js:**
```javascript
const fs = require('fs');
// npm install gbl-ninja
// const { Gbl } = require('gbl-ninja');
```

## Quick Start

**Parse GBL file:**
```javascript
const gbl = new Gbl();
const parseResult = gbl.parseByteArray(fileData);

if (parseResult.type === 'Success') {
    parseResult.resultList.forEach(tag => {
        console.log(`${tag.tagType.name}: ${tag.tagHeader.length} bytes`);
    });
}
```

**Create GBL file:**
```javascript
const builder = Gbl.GblBuilder.create()
    .application(32, 0x10000, 0, 54)
    .prog(0x1000, new Uint8Array([0x00, 0x01, 0x02]));

const gblBytes = builder.buildToByteArray();
```

## API

**Gbl class:**
```javascript
gbl.parseByteArray(uint8Array)  // Returns ParseResult
gbl.encode(tags)                // Returns Uint8Array
```

**GblBuilder class:**
```javascript
Gbl.GblBuilder.create()
  .application(type, version, capabilities, productId)
  .bootloader(version, address, data)
  .prog(address, data)
  .metadata(data)
  .eraseProg()
  .buildToByteArray()
```

## Tag Types

HEADER_V3, APPLICATION, BOOTLOADER, PROG, PROG_LZ4, PROG_LZMA, ERASEPROG, METADATA, ENCRYPTION_DATA, ENCRYPTION_INIT, SIGNATURE_ECDSA_P256, CERTIFICATE_ECDSA_P256, SE_UPGRADE, VERSION_DEPENDENCY, END

## License

Apache License 2.0