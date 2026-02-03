# QE Tool

> A collection of utilities for QE (Quality Engineering) development

## Overview

The QE module provides a set of development tools designed to assist with quality engineering workflows. It includes utilities for serial communication, code compression, testing, and various development helper tools.

## Module Structure

The QE module is organized as follows:

```
qe/
├── tools/             # Development Tools
│   ├── SerialPortTool
│   ├── Compress
│   ├── RcpIconTool
│   ├── FxmlDemo
│   ├── DaliDemo
│   └── TestTool
├── config/            # Configuration
├── provider/          # SPI Implementations
└── util/              # Utilities
```

## Available Tools

### Serial Port Tool
A serial communication assistant for debugging and testing serial devices.

**Features:**
- Automatically detects available serial ports
- Configurable communication parameters:
  - Baud rate (100 - 256000)
  - Parity (None, Odd, Even, Mark, Space)
  - Data bits (5, 6, 7, 8)
  - Stop bits (1, 2)
  - Flow control
- Send/receive data in text or hexadecimal format
- Display timestamps for received data
- Pause/resume reception
- Clear send/receive buffers
- Track byte counts

**Usage:**
1. Select the target serial port from the dropdown
2. Configure communication parameters (baud rate, parity, etc.)
3. Click "Open" to establish connection
4. Enter data in the send area and click "Send"
5. Received data appears in the receive area
6. Use "Close" to disconnect when done

### Compress (JS/CSS Compression)
A minification tool for JavaScript and CSS files using YUI Compressor.

**Features:**
- JavaScript compression with configurable options:
  - Munge: Obfuscate local symbols
  - Verbose: Display informational messages
  - Preserve semicolons: Keep all semicolons
  - Disable optimizations: Skip micro optimizations
  - Line break position: Set column for line breaks
- CSS compression with line break control
- Input via text area or drag-and-drop file
- Copy compressed result to clipboard

**Usage:**
1. Paste or drag JavaScript/CSS code into the input area
2. Configure compression options as needed
3. Click "Compress" to minify the code
4. Result is displayed and copied to clipboard

**Compression Options:**
- **Munge**: Obfuscates local variable and function names for smaller output
- **Verbose**: Shows detailed compression information
- **Preserve Semicolons**: Keeps all semicolons (useful for debugging)
- **Disable Optimizations**: Skips micro optimizations for safer compression
- **Line Break Position**: Inserts line breaks after specified column

### RCP Icon Tool
A utility for managing Eclipse RCP application icons.

### FXML Demo
Demonstrates FXML usage patterns in JavaFX applications.

### DALI Demo
Showcases DALI (Digital Addressable Lighting Interface) features and controls.

### Test Tool
A general-purpose testing utility for development workflows.

## Requirements

- **JDK 21+**: Required for running the QE module
- **JavaFX 21**: UI framework
- **Maven 3.6.0+**: Build tool

## Building

```bash
# Build the QE module
cd qe
mvn clean package

# Create platform-specific distribution
mvn -Djavafx.platform=win -Dmaven.test.skip=true -Pzip package
```

Replace `win` with `mac` or `linux` for other platforms.

## Dependencies

The QE module uses the following key libraries:
- **jSerialComm**: For serial port communication
- **YUI Compressor**: For JavaScript/CSS minification
- **ControlsFX**: For enhanced JavaFX controls

## Configuration

User preferences and tool settings are automatically persisted in XML format and restored on application restart.

## Internationalization

The QE module supports multiple languages:
- English
- Chinese (中文)
- Japanese (日本語)

Language can be configured through the application settings.

## Developer Notes

### Adding New Tools

To add a new tool to the QE module:

1. Create a new class extending `QeSample`
2. Implement required methods:
   - `getSampleId()`: Unique identifier
   - `getSampleName()`: Display name
   - `getSampleDescription()`: Tool description
   - `getSampleVersion()`: Version string
   - `getPanel(Stage stage)`: UI panel
   - `getOrderKey()`: Menu ordering key

3. The tool will be automatically discovered via ServiceLoader

### Visibility Control

Tools can control their visibility based on environment:
```java
@Override
public boolean isVisible() {
    String value = System.getProperty(CoreConstant.JVM_WORKENV);
    return CoreConstant.JVM_WORKENV_DEV.equals(value);
}
```

This allows hiding development/testing tools from production builds.
