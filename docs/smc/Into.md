# SMC Tool

> A comprehensive toolkit for Smart Configuration development

## Overview

The SMC (Smart Configuration) module is a powerful development tool designed to assist with Smart Configuration development workflows. It provides a suite of specialized utilities for code development, unit design documentation, unit testing, and code quality tools.

## Module Structure

The SMC module is organized into the following categories:

```
smc/
├── codeDev/           # Code Development Tools
│   ├── DmaTriggerSourceCode
│   ├── DtsTriggerSourceXml
│   └── ecm/           # ECM Scripts (U2A, U2C, C1M)
├── unitDesign/        # Unit Design Tools
│   ├── DtsTriggerSourceDoc
│   └── HconvertExcel
├── unitTest/          # Unit Testing Tools
│   └── SpecGeneralTest
├── tools/             # General Tools
│   ├── CodeStyleLength120
│   └── GirretReview
├── config/            # Configuration
├── provider/          # SPI Implementations
└── util/              # Utilities
```

## Available Tools

### Code Development (codeDev)

#### DMA Trigger Source Code
Generates setting, binding, and header code based on DMA trigger source documentation.

**Features:**
- Reads Excel-based trigger source specifications
- Generates XML configuration files (binding_trigger.xml, binding_selSetting.xml, etc.)
- Creates C header files (r_cg_dma.h)
- Supports customizable templates via Freemarker

**Usage:**
1. Select the input Excel file containing DMA trigger source data
2. Choose the output directory
3. Configure group columns and device settings
4. Specify sheet name, start/end rows
5. Click "Generate" to create output files

#### DTS Trigger Source XML
Generates XML data files based on DTS trigger source documentation to assist with CD development.

**Features:**
- Parses DTS Transfer Request Table Excel files
- Generates DTCTriggerSource XML files per device
- Supports multiple device configurations

**Usage:**
1. Select the Excel file (e.g., DTS_Transfer_request_Table.xlsx)
2. Configure output directory
3. Set group columns (e.g., B, C, D, E)
4. Add device configurations (format: `DeviceName;StartColumn`)
5. Click "Generate" to create XML files

#### ECM Scripts
A collection of scripts for ECM (Error Control Module) configuration across different microcontroller families:
- **U2A ECM Script**: For RH850/U2A family
- **U2C ECM Script**: For RH850/U2C family
- **C1M ECM Script**: For RH850/C1M family

### Unit Design (unitDesign)

#### DTS Trigger Source Doc
Generates Unit Design documents based on DTS trigger source documentation.

**Features:**
- Creates formatted Excel UD documents
- Supports template customization
- Downloads default template files
- Processes multiple device configurations

**Usage:**
1. Select the source Excel file
2. Choose output directory
3. Configure group columns and device names
4. Optionally select a custom template
5. Click "Generate" to create UD documents

#### H Convert Excel
Converts header files (.h) to Excel format for Unit Design documentation.

**Features:**
- Recursively scans directories for header files
- Parses #define macros and comments
- Generates formatted Excel output
- Supports file filtering (ignore/mark specific files)

**Usage:**
1. Select the directory containing header files
2. Configure file types to scan (default: h)
3. Optionally set files to ignore or mark
4. Click "Generate" to create Excel output

### Unit Testing (unitTest)

#### Spec General Test
Generates diff files for specGeneral testing documents to improve testing efficiency.

**Features:**
- Compares UD Excel content with generated files
- Creates HTML diff reports
- Supports merged overview report
- Can generate test files only (without comparison)

**Usage:**
1. Select the UD Excel file (e.g., TestSpec_General_RH850U2A.xlsx)
2. Choose the general files directory for comparison
3. Set output directory
4. Configure macro length and sheet filtering
5. Click "Diff" to generate comparison reports

### Tools

#### Code Style Length 120
Checks files for lines exceeding 120 characters to ensure code style compliance.

**Features:**
- Scans directories recursively
- Supports multiple file type filtering
- Generates Excel report with violation details
- Shows line number, file name, length, and content

**Usage:**
1. Select the directory to check
2. Specify file types (e.g., c, h)
3. Optionally set files to ignore
4. Click "Generate" to create the report

#### Girret Review
A code review tool for Girret-based code review workflows.

## Requirements

- **JDK 21+**: Required for running the SMC module
- **JavaFX 21**: UI framework
- **Maven 3.6.0+**: Build tool

## Building

```bash
# Build the SMC module
cd smc
mvn clean package

# Create platform-specific distribution
mvn -Djavafx.platform=win -Dmaven.test.skip=true -Pzip package
```

Replace `win` with `mac` or `linux` for other platforms.

## Configuration

User preferences and tool settings are automatically persisted in XML format and restored on application restart.

## Internationalization

The SMC module supports multiple languages:
- English
- Chinese (中文)
- Japanese (日本語)

Language can be configured through the application settings.
