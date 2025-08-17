# Unison User Guide

## Installation Prerequisites
- **Java Development Kit (JDK) 17 or later**
- **Apache Maven** for building from source
- Network access to an NNTP server

## Building from Source
```bash
mvn clean package
```
This creates the standalone application JAR in `target/`.

## Launch Instructions
### Graphical Interface
```bash
java -jar target/unison-jar-with-dependencies.jar
```
### Command Line Interface
```bash
java -cp target/unison-jar-with-dependencies.jar uk.co.sleonard.unison.input.UNISoNCLI <COMMAND> <ARG>
```
Available commands:
- `FIND <pattern>` – list newsgroups matching a pattern
- `DOWNLOAD <newsgroup>` – download articles from a group
- `FINDDOWNLOAD <pattern>` – search for groups and download them
- `QUICKDOWNLOAD <newsgroup>` – minimal download for quick analysis

## Basic Workflow
1. Launch the GUI or CLI as above.
2. Connect to an NNTP server and search for newsgroups.
3. Download message headers or full content.
4. Export data for analysis in tools such as Pajek.

## Support and Resources
- Project page and documentation: [SourceForge – Unison-SNA](https://sourceforge.net/projects/unison-sna/)
- Bug reports and feature requests: [GitHub Issues](https://github.com/leonarduk/unison/issues)
- FAQ and additional guides: [SourceForge Wiki](https://sourceforge.net/p/unison-sna/wiki/FAQ/)
