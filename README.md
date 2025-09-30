# 🎵 iSongly

A Java-based music playlist application that allows users to add, organize, and sort songs dynamically based on multiple musical attributes. The system uses a self-balancing **Red–Black Tree (RBT)** to efficiently manage song data, ensuring fast insertions, deletions, and sorted traversal.

---

## 🚀 Overview

**iSongly** provides an interactive interface for managing playlists while exploring how advanced tree-based data structures can power dynamic sorting and retrieval.  
Each song is stored as an object containing attributes such as **title**, **artist**, **danceability**, **energy**, **loudness**, and **tempo**.

The project demonstrates efficient organization and retrieval of data through a custom-built **iterable Red–Black Tree**, supporting flexible traversal and comparison logic.

---

## ⚙️ Features

- 🎶 **Dynamic Playlist Management** — Add, remove, and view songs in a continuously sorted structure.  
- 🌳 **Red–Black Tree Implementation** — Ensures balanced insertion, deletion, and lookup operations.  
- 🧩 **Multi-Attribute Sorting** — Sorts and filters songs by metrics such as **danceability**, **energy**, **loudness**, and **tempo**.  
- 🔄 **Iterable Data Structure** — Implements a custom `IterableSortedCollection` interface, enabling ordered iteration across multiple criteria.  
- 🧠 **Modular Architecture** — Separates backend tree logic, data model (`Song.java`), and frontend interaction for easy testing and extension.  
- 🧪 **Comprehensive Testing** — Includes **JUnit** and integration tests for Red–Black Tree operations, frontend behavior, and combined workflows.

---
Testing Framework
1.ComboTests.java — Validates combined sorting and comparison logic.
2.IntegrationTests.java — Tests end-to-end functionality across frontend and backend.
3.FrontendTests.java — Checks input/output handling and user prompts.
4.JUnit 5 — Ensures correctness, stability, and regression safety.

🙌 Acknowledgments

This project was developed as part of the University of Wisconsin–Madison computer science coursework.
Special thanks to the CS400 instructional staff for providing foundational guidance, testing frameworks, and dataset resources.


