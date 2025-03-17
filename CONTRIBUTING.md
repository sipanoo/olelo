# Contributing to Olelo

First off, thank you for considering contributing to Olelo. It's people like you that make Olelo such a great tool.

### 1. Where do I go from here?

If you've noticed a bug or have a feature request, make one! It's generally best if you get confirmation of your bug or approval for your feature request before diving into coding to avoid unnecessary duplicate work.

### 2. Fork & create a branch

If this is something you think you can fix, then fork Olelo and create a branch with a descriptive name.

A good branch name would be (where issue #325 is the ticket you're working on):

```sh
git checkout -b 325-add-dark-mode
```

### 3. Build & Test

Ensure your local changes don't break the build before submitting. Use Android Studio to verify functionality or run gradle tasks locally.

### 4. Commit and Push

Make your commit messages meaningful.

- **Bad**: `Fixing things`
- **Good**: `Fix: NullPointerException in ChatActivity when loading old messages`

Push your branch linearly to your fork.

### 5. Pull Request

Once ready, submit a Pull Request to the `main` branch. Ensure you fill out the pull request template so we can understand your changes!
