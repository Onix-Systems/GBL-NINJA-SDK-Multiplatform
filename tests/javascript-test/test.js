const fs = require('fs');
const path = require('path');

const gblLibPath = path.resolve(__dirname, '../../javascript-library/gbl.js');
require(gblLibPath);

async function testGblParsing() {
    console.log('=== JavaScript GBL Test ===');

    try {
        const filePath = path.join(__dirname, '../test.gbl');
        const fileData = new Uint8Array(fs.readFileSync(filePath));

        console.log(`Loaded test.gbl: ${fileData.length} bytes`);

        const gbl = new Gbl();
        const result = gbl.parseByteArray(fileData);

        if (result.type === 'Success') {
            console.log(`✓ Successfully parsed ${result.resultList.length} tags:`);
            result.resultList.forEach((tag, index) => {
                console.log(`  ${index + 1}. ${tag.tagType.name}`);
            });
        } else {
            console.log(`✗ Parse failed: ${result.error}`);
        }

    } catch (error) {
        console.error('✗ Error:', error.message);
    }
}

testGblParsing();