import Foundation

func testGblParsing() {
    print("=== Swift GBL Test ===")

    do {
        let currentDir = FileManager.default.currentDirectoryPath
        let filePath = "\(currentDir)/tests/test.gbl"
        let fileURL = URL(fileURLWithPath: filePath)

        let fileData = try Data(contentsOf: fileURL)
        print("Loaded test.gbl: \(fileData.count) bytes")

        let gbl = Gbl()
        let result = gbl.parseByteArray(fileData)

        switch result {
        case .success(let tags):
            print("✓ Successfully parsed \(tags.count) tags:")
            for (index, tag) in tags.enumerated() {
                print("  \(index + 1). \(tag.tagType)")
            }
        case .fatal(let error):
            print("✗ Parse failed: \(error?.localizedDescription ?? "Unknown error")")
        }

    } catch {
        print("✗ Error: \(error.localizedDescription)")
        print("Make sure swift-library is compiled and linked properly")
    }
}

testGblParsing()