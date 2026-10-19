# Genome Alignment

A Java program for genome sequence alignment, with controlled user access management and back-up functionality.

Users authenticate against a team file, run alignments on a gene sequence in FASTA format, and get their results (an alignment and a score) written to per-user output files. Session data can be backed up to a plain-text file.

> **Note:** sections marked `TODO` need details only you know (exact algorithm, class names, build setup). Fill them in or delete them before publishing.

---

## Table of Contents

- [Features](#features)
- [Repository Structure](#repository-structure)
- [Requirements](#requirements)
- [Configuration](#configuration)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [Output Files](#output-files)
- [Documentation](#documentation)
- [Author](#author)

---

## Features

- **Genome alignment**: aligns genome sequences read from a FASTA file (the default dataset is an HIV sequence, `data/hiv.fasta`).
- **Alignment scoring**: computes an alignment score for each run.
- **User access management**: access is restricted to the users listed in a team file (`data/team.txt`).
- **Per-user results**: each user's alignment and score are saved to their own files.
- **Back-up**: program data can be saved to `backup.txt`.
- **External configuration**: input file locations are set in `config.properties`, so no recompilation is needed to change datasets.

`TODO`: name the alignment algorithm (e.g. global / local, scoring scheme: match, mismatch, gap penalties).

## Repository Structure

```
genome_alignment/
├── data/                              # Input data (team list, FASTA gene file)
├── src/                               # Java source code
├── config.properties                  # Paths to input files
├── backup.txt                         # Back-up output
├── <User>.alignment.txt               # Alignment result for a given user
├── <User>.score.txt                   # Alignment score for a given user
├── Java Project - Marco Salmistraro.pdf   # Project report
└── .gitignore
```

## Requirements

- Java Development Kit (JDK) 8 or later (`TODO`: confirm the minimum version you built with)
- No external libraries are required (`TODO`: confirm, or list any dependencies)

Check your installation:

```bash
java -version
javac -version
```

## Configuration

Input files are defined in `config.properties` at the project root:

```properties
teamFile = data/team.txt
geneFile = data/hiv.fasta
```

| Property   | Description                                         |
|------------|-----------------------------------------------------|
| `teamFile` | File listing the users allowed to access the program |
| `geneFile` | FASTA file containing the genome sequence(s)        |

Paths are relative to the directory the program is run from, so run it from the project root.

To use a different dataset, change `geneFile` to point at another FASTA file.

## Getting Started

Clone the repository:

```bash
git clone https://github.com/marcosalmistraro/genome_alignment.git
cd genome_alignment
```

Compile (`TODO`: adjust if the sources use packages or a different layout):

```bash
mkdir -p out
javac -d out $(find src -name "*.java")
```

Run (`TODO`: replace `Main` with the actual entry-point class, including its package if any):

```bash
java -cp out Main
```

Alternatively, open the project in an IDE such as IntelliJ IDEA or Eclipse, mark `src` as the source root, and run the main class from there.

## Usage

1. Start the program from the project root so `config.properties` and `data/` are found.
2. Log in as a user listed in `data/team.txt`. Users not in the team file are denied access.
3. Run the alignment on the gene sequence loaded from `geneFile`.
4. Review the results in the generated files (see below).
5. Use the back-up option to save the current data to `backup.txt`.

`TODO`: add a short example session (menu options and sample output) once you've confirmed the exact prompts.

## Output Files

For each user, the program writes two files in the project root:

| File                     | Contents                                  |
|--------------------------|-------------------------------------------|
| `<User>.alignment.txt`   | The resulting sequence alignment          |
| `<User>.score.txt`       | The alignment score, as a single integer  |

The repository includes sample outputs for two users, `MarcJanssens` and `JozefGroenewegen`. For example, `MarcJanssens.score.txt` contains a single score value (`38023`).

`backup.txt` holds the back-up data. In the committed example it is a large text file (around 400 lines, close to 1 MB).

## Documentation

A full write-up of the design and implementation is in [`Java Project - Marco Salmistraro.pdf`](./Java%20Project%20-%20Marco%20Salmistraro.pdf).

## Author

**Marco Salmistraro**  
GitHub: [@marcosalmistraro](https://github.com/marcosalmistraro)

## License

This project is licensed under the MIT License. See the [LICENSE](./LICENSE) file for details.
