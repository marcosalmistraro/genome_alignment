# Genome Alignment

![Java](https://img.shields.io/badge/Java-8%2B-007396?logo=openjdk&logoColor=white)
![Build](https://img.shields.io/badge/Build-javac-blue)
[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](./License)

An object-oriented Java project that models a bioinformatics team working on **genome alignments** of HIV sequences. It loads a set of aligned genomes from a FASTA file, scores them against a reference, lets different team roles edit and manage the alignments under access control, and can back up and restore the whole team state.

The project was built for an *Introduction to Object-Oriented Programming* course project (2022); the full report is in [`Java Project - Marco Salmistraro.pdf`](./Java%20Project%20-%20Marco%20Salmistraro.pdf).

---

## Table of Contents

- [Features](#features)
- [How It Works](#how-it-works)
- [Repository Structure](#repository-structure)
- [Requirements](#requirements)
- [Configuration](#configuration)
- [Build and Run](#build-and-run)
- [Output Files](#output-files)
- [Documentation](#documentation)
- [Author](#author)
- [License](#license)

---

## Features

- **Genome library**: genomes are read from a FASTA file (default dataset: an HIV sequence set, `data/hiv.fasta`) into an insertion-ordered id → sequence map.
- **Two alignment views**:
  - **Standard** (`StdAlignment`) — the genomes as stored.
  - **SNP-style** (`SNiPAlignment`) — each genome is printed relative to the reference: a `.` where a position matches the reference and the differing nucleotide otherwise.
- **Alignment scoring**: a similarity measure over the whole library. For every genome, the score counts the nucleotide positions that differ from a reference sequence (by default the first genome in the file), summed across all genomes (a Hamming-distance-to-reference total).
- **Alignment editing**: search for a sub-sequence, replace sequences within a genome or across the whole library, and add, remove or replace genomes.
- **Role-based access control**: operations are tied to team roles, and privileged actions performed by someone who does not belong to the team raise an exception (see [How It Works](#how-it-works)).
- **Back-up and restore**: the full team state can be written to a plain-text file and restored from it.
- **External configuration**: input file locations are set in `config.properties`, so the dataset can be changed without recompiling.

## How It Works

The program is organised around a `Team` built from a team file and a gene (FASTA) file, with members in distinct roles:

| Role | Responsibilities |
|---|---|
| **Team Lead** (`TeamLead`) | Reads the repository's optimal alignment and score, overwrites or promotes a bioinformatician's alignment, and exports team reports/data. Must belong to the team. |
| **Bioinformatician** (`BioInfo`) | Owns a personal `StdAlignment`, can edit it (search/replace/add/remove genomes), inspect its score, and export their own report/data. |
| **Technical Support** (`TechnicalSupport`) | Performs back-up, restore and clear operations on the team. Must belong to the team. |
| **Repository** (`Repository`) | Holds the optimal alignment plus each user's alignment and the associated scores. |

Access control is enforced in code: when a `TeamLead` or `TechnicalSupport` who is not part of the team attempts a privileged operation (overwrite, promote, back-up, export), the call raises an exception rather than performing the action.

The `Alignment` abstract class implements a `Functional` interface and provides the shared genome library and scoring logic; `StdAlignment` and `SNiPAlignment` are its concrete subclasses.

## Repository Structure

```
genome_alignment/
├── data/                              # Input data
│   ├── hiv.fasta                      #   HIV genome sequences (FASTA)
│   └── team.txt                       #   Team members
├── src/                               # Java source code
│   ├── Main/Main.java                 #   Entry point (scripted demonstration)
│   ├── Alignments/                    #   Alignment, StdAlignment, SNiPAlignment, Functional
│   └── Team/                          #   Member, TeamLead, BioInfo, TechnicalSupport, Team, Repository
├── config.properties                  # Paths to the input files
├── backup.txt                         # Example back-up output
├── <User>.alignment.txt               # Alignment result for a given user
├── <User>.score.txt                   # Alignment score for a given user
├── Java Project - Marco Salmistraro.pdf   # Project report
├── License
└── .gitignore
```

## Requirements

- **JDK 8 or later** (the code uses try-with-resources, generics and `java.util.Properties`).
- **No external libraries** — only the Java standard library (`java.io`, `java.nio`, `java.util`).

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

| Property   | Description                                          |
|------------|------------------------------------------------------|
| `teamFile` | File listing the team members                        |
| `geneFile` | FASTA file containing the genome sequences           |

Paths are relative to the directory the program is run from, so run it from the project root. To use a different dataset, point `geneFile` at another FASTA file (one `>id` line followed by one sequence line per genome, all sequences the same length).

## Build and Run

The sources are organised into packages (`Main`, `Alignments`, `Team`), and the entry point is `Main.Main`.

```bash
git clone https://github.com/marcosalmistraro/genome_alignment.git
cd genome_alignment

# Compile all sources into out/
mkdir -p out
javac -d out $(find src -name "*.java")

# Run from the project root so config.properties and data/ are found
java -cp out Main.Main
```

Alternatively, open the project in an IDE such as IntelliJ IDEA or Eclipse, mark `src` as the source root, and run `Main`.

Running `Main` executes a scripted demonstration: it builds the team, prints the members and the repository score, then walks through bioinformatician edits, Team Lead overwrite/promote actions, back-up/restore, clearing the repository, and report/data export — including the calls that are expected to raise exceptions when attempted by non-members.

## Output Files

For each user, the program writes two files in the project root:

| File                     | Contents                                  |
|--------------------------|-------------------------------------------|
| `<User>.alignment.txt`   | The resulting sequence alignment          |
| `<User>.score.txt`       | The alignment score, as a single integer  |

The repository includes sample outputs for two users, `MarcJanssens` and `JozefGroenewegen` (for example, `MarcJanssens.score.txt` contains `38023`). `backup.txt` holds an example back-up of the team state.

## Documentation

A full write-up of the design and implementation is in [`Java Project - Marco Salmistraro.pdf`](./Java%20Project%20-%20Marco%20Salmistraro.pdf).

## Author

**Marco Salmistraro**  
GitHub: [@marcosalmistraro](https://github.com/marcosalmistraro)

## License

This project is licensed under the MIT License. See the [License](./License) file for details.
