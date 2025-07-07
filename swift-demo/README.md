# Face Comparison Demo (Swift)

iOS app for comparing faces between two photo groups using PerchEye SDK.

## Requirements

- iOS 18.0+
- Xcode 16.0+
- Swift 5.0+

## Installation

```bash
# Clone and open
git clone <repository-url>
cd swift-demo
open DemoApp.xcodeproj
```

## Project Structure

```
DemoApp/
├── Components/
│   ├── DashedBorderView.swift     # Custom border UI
│   ├── ErrorAlertView.swift       # Error alerts
│   └── PhotosFolderView.swift     # Photo picker UI
├── Extensions/
│   └── UIColor+Extension.swift    # Custom colors
├── Resources/
│   └── Assets.xcassets           # Images and icons
├── ContentView.swift             # Main UI
├── DemoAppApp.swift             # App entry point  
├── FacesAnalyzer.swift          # PerchEye wrapper
├── ViewModel.swift              # UI state management
└── Info.plist                  # App configuration
```

## Dependencies

- **PerchEyeiOS** - Face recognition SDK (local package)
- **PhotosUI** - System photo picker
- **SwiftUI** - UI framework

## Features

- Select up to 10 photos per group
- Face detection and comparison
- Similarity score calculation
- Error handling for missing faces
- Photo deletion functionality

## Usage

1. **Select Photos**: Tap photo placeholders to pick images
2. **Compare**: Press "Compare Photos" button
3. **View Score**: Similarity score appears (0.0-1.0 scale)
4. **Threshold**: Score >0.8 shows green, ≤0.8 shows red

## Key Components

### FacesAnalyzer
```swift
// Analyze faces between two image groups
func analyzeFaces(in firstImages: [UIImage], and secondImages: [UIImage]) async throws(CustomError) -> Float
```

### ViewModel
```swift
@Published var firstImagesGroup: [UIImage]    // First photo group
@Published var secondImagesGroup: [UIImage]   // Second photo group  
@Published var similarityScore: Float?        // Comparison result
@Published var error: CustomError?            // Error state
```

### PhotoFolderView
- Multi-photo selection (max 10)
- Visual photo stack display
- Photo count indicator

## Error Handling

- **SDK Not Initialized**: PerchEye setup issue
- **Face Not Found**: No face detected in images
- **Transaction Error**: SDK transaction problems
- **Images Missing**: No photos selected for comparison

## Build Configuration

### Debug
- Development team: `3TVZ6QJSU6`
- Bundle ID: `Onix-Systems.DemoApp`
- Photo library permission required

### Release
- Same as Debug with optimizations

## Permissions

```xml
<key>NSPhotoLibraryUsageDescription</key>
<string>We need access so you can select the photos you want to compare.</string>
```

## Color Scheme

- **Dark Blue**: `#006f85` - Primary buttons
- **Light Blue**: `#a8ced5` - Accents
- **Bluish Grey**: `#7c9a9c` - Disabled states
- **Sky Blue**: `#99c5ce` - Borders